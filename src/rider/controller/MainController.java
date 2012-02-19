/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rider.controller;


import rider.Icons;
import rider.Localization;
import rider.core.RssCoreSettings;
import rider.core.RssProvider;
import rider.core.bookmarks.IBookmarkCollection;
import rider.core.bookmarks.serialization.xml.BookmarkCollectionXmlDeserializer;
import rider.core.bookmarks.serialization.xml.BookmarkCollectionXmlSerializer;
import rider.core.file.FileReader;
import rider.core.file.FileReaderListener;
import rider.core.rss.feed.IRssFeed;
import rider.core.rss.feed.RssFeed;
import rider.core.rss.loader.IRssContentListener;
import rider.core.rss.loader.RssContentLoader;
import rider.core.rss.loader.RssInputStream;
import rider.core.rss.parser.IRssParser;
import rider.core.rss.parser.RssParserProvider;
import rider.utils.InternalLogger;
import rider.utils.ViewHelper;
import rider.view.DisplayThreadSafe;
import rider.view.MIDletThreadSafe;
import rider.view.standard.*;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 * Rider MainView controller
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class MainController
        implements
        CommandListener,
        IFileManagerListener,
        IFolderManagerListerner,
        IController,
        IRssContentListener {

    // commands
    private Command _exitCommand = new Command(Localization.getInstance().strings().get("kExit"), Command.EXIT, 1);
    private Command _loadFeedCommand = new Command(Localization.getInstance().strings().get("kLoadFeed"), Command.SCREEN, 2);
    private Command _loadBookmarkCommand = new Command(Localization.getInstance().strings().get("kBookmarks"), Command.SCREEN, 3);
    private Command _importBookmarks = new Command(Localization.getInstance().strings().get("kImportBookmarks"), Command.SCREEN, 4);
    private Command _exportBookmarks = new Command(Localization.getInstance().strings().get("kExportBookmarks"), Command.SCREEN, 5);
    private Command _aboutCommand = new Command("About", Command.SCREEN, 6);
    // views
    private MainView _mainForm = null;
    private RssProvider _rssProvider = null;

    public MainController(MainView mainForm, RssProvider rssProvider) throws NullPointerException {
        if (mainForm == null || rssProvider == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("MainController.MainController"));
            //#endif
            throw new NullPointerException("MainController.MainController");
        }
        this._mainForm = mainForm;
        this._rssProvider = rssProvider;
        addCommands(this._mainForm);

        loadBookmarks();
    }

    public void updateView() {
    }

    private void addCommands(MainView mainForm) {
        mainForm.setCommandListener(this);
        mainForm.addCommand(this._exitCommand);
        mainForm.addCommand(this._loadFeedCommand);
        mainForm.addCommand(this._loadBookmarkCommand);
        mainForm.addCommand(this._importBookmarks);
        mainForm.addCommand(this._aboutCommand);
        mainForm.addCommand(_exportBookmarks);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == this._exitCommand) {
            exitCommand(this._mainForm.getMIDlet());
        } else if (c == this._loadFeedCommand) {
            loadFeedCommand();
        } else if (c == this._loadBookmarkCommand) {
            loadBookmarksCommand();
        } else if (c == this._aboutCommand) {
            showAboutRider(this._mainForm.getMIDlet().getDisplay());
        } else if (c == this._importBookmarks) {
            importBookmarks();
        } else if (c == _exportBookmarks) {
            exportBookmarks();
        }
    }

    public void onFolderSelected(String folder) {
    }

    public void onFolderManagerError(Exception e) {
        showErrorMessage(this._mainForm.getMIDlet().getDisplay(), Localization.getInstance().strings().get("kUnableToExecuteFolderOperation"));
    }

    private void exportBookmarks() {
        try {
            ExportBookmarkView view = new ExportBookmarkView(this._mainForm.getMIDlet(), this._mainForm);
            view.attachController(new ExportBookmarkController(view, this._rssProvider.getBookmarks()));
        } catch (Exception ex) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(ex);
            //#endif
            showAboutUnableToExport(_mainForm.getMIDlet().getDisplay());
        }
    }

    private void importBookmarks() {
        DisplayThreadSafe display = this._mainForm.getMIDlet().getDisplay();
        try {
            FileManagerView view = new FileManagerView(this._mainForm.getMIDlet(), this._mainForm.getBackDisplay());
            Vector fileTypeFilter = new Vector();
            fileTypeFilter.addElement("xml");

            FileManagerController fileManagerController = new FileManagerController(view, fileTypeFilter);
            fileManagerController.attachFileManagerListener(this);
            view.attachController(fileManagerController);

            display.setCurrent(view);
        } catch (Exception ex) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(ex);
            //#endif
            showAboutUnableToImport(display);
        }
    }

    private void exitCommand(MIDletThreadSafe midlet) {
        saveBookmarks();
        midlet.notifyDestroyed();
    }

    private void saveBookmarks() {
        RssCoreSettings settings = this._rssProvider.getSettings();
        if (settings != null) {
            BookmarkCollectionXmlSerializer serialzer = new BookmarkCollectionXmlSerializer();
            String xmlBookmarks = serialzer.serialize(_rssProvider.getBookmarks());
            settings.saveBookmarks(xmlBookmarks);
            settings.save();
        }
    }

    private void loadBookmarks() {
        RssCoreSettings settings = this._rssProvider.getSettings();
        if (settings != null) {
            String bookmarks = settings.loadBookmarks();
            if (bookmarks != null) {
                BookmarkCollectionXmlDeserializer deserializer = new BookmarkCollectionXmlDeserializer();
                try {
                    IBookmarkCollection collection = deserializer.deserialize(bookmarks);

                    for (int bookmarkIndex = 0; bookmarkIndex < collection.getCount(); ++bookmarkIndex) {
                        _rssProvider.getBookmarks().addBookmark(collection.getBookmark(bookmarkIndex));
                    }
                } catch (UnsupportedEncodingException e) {
                    //#ifdef rider.debugEnabled
                    InternalLogger.getInstance().fatal(e);
                    //#endif
                    showAboutUnableToImport(_mainForm.getMIDlet().getDisplay());
                }
            }
        }
    }

    private void loadFeedCommand() {
        DisplayThreadSafe display = this._mainForm.getMIDlet().getDisplay();
        String rssFieldContent = this._mainForm.getRssFieldContent();
        if (rssFieldContent.length() > 0) {

            RssFeed rssFeed = new RssFeed(rssFieldContent);
            RssContentLoader loader = new RssContentLoader(rssFeed, this);
            loader.start();
        } else {
            alertAboutEmptyFeed(display);
        }
    }

    public void onFileSelected(String filename) {
        class BookmarksImporter implements FileReaderListener {

            private IBookmarkCollection _bookmarks = null;
            private MainView _mainForm = null;

            public BookmarksImporter(IBookmarkCollection bookmarks, MainView mainForm) {
                if (bookmarks == null || mainForm == null) {
                    //#ifdef rider.debugEnabled
                    InternalLogger.getInstance().fatal(new NullPointerException("BookmarksImporter.BookmarksImporter"));
                    //#endif
                    throw new NullPointerException("BookmarksImporter.BookmarksImporter");
                }
                this._bookmarks = bookmarks;
                this._mainForm = mainForm;
            }

            public void onFileRead(String content) {

                BookmarkCollectionXmlDeserializer deserializer = new BookmarkCollectionXmlDeserializer();
                try {
                    IBookmarkCollection collection = deserializer.deserialize(content);
                    _bookmarks.removeAllBookmarks();

                    for (int bookmarkIndex = 0; bookmarkIndex < collection.getCount(); ++bookmarkIndex) {
                        _bookmarks.addBookmark(collection.getBookmark(bookmarkIndex));
                    }

                    BookmarksListView bookmarksView =
                            new BookmarksListView(_mainForm.getMIDlet(), _mainForm);
                    bookmarksView.attachController(new BookmarksListController(bookmarksView, _bookmarks));

                    _mainForm.getMIDlet().getDisplay().setCurrent(bookmarksView);
                } catch (UnsupportedEncodingException e) {
                    //#ifdef rider.debugEnabled
                    InternalLogger.getInstance().fatal(e);
                    //#endif
                    showAboutUnableToImport(_mainForm.getMIDlet().getDisplay());
                }
            }

            public void onFileReadError(Exception e) {
                showAboutUnableToImport(_mainForm.getMIDlet().getDisplay());
            }

            private void showAboutUnableToImport(DisplayThreadSafe display) {
                ViewHelper.showMessageBox(
                        display,
                        Localization.getInstance().strings().get("kWarning"),
                        Localization.getInstance().strings().get("kUnableToImportBookmarks"),
                        null,
                        AlertType.ERROR
                );
            }
        }

        BookmarksImporter importer = new BookmarksImporter(_rssProvider.getBookmarks(), this._mainForm);
        FileReader reader = new FileReader(filename, importer);
        reader.start();
    }

    public void onFileManagerError(Exception e) {
        showErrorMessage(this._mainForm.getMIDlet().getDisplay(), Localization.getInstance().strings().get("kUnableToExecuteFileOperation"));
    }

    private void loadBookmarksCommand() {
        BookmarksListView bookmarksView =
                new BookmarksListView(this._mainForm.getMIDlet(), this._mainForm);
        bookmarksView.attachController(new BookmarksListController(bookmarksView, this._rssProvider.getBookmarks()));

        DisplayThreadSafe display = this._mainForm.getMIDlet().getDisplay();
        display.setCurrent(bookmarksView);
    }

    private void alertAboutEmptyFeed(DisplayThreadSafe display) {
        ViewHelper.showMessageBox(
                display,
                Localization.getInstance().strings().get("kWarning"),
                Localization.getInstance().strings().get("kPleaseInputRssFeed"),
                null,
                AlertType.INFO
        );
    }

    private void showAboutRider(DisplayThreadSafe display) {
        ViewHelper.showMessageBox(
                display,
                Localization.getInstance().strings().get("kAbout"),
                Localization.getInstance().strings().get("kAboutDescription"),
                Icons.getInstance().getAboutIcon(),
                AlertType.INFO
        );
    }

    private void showAboutUnableToImport(DisplayThreadSafe display) {
        ViewHelper.showMessageBox(
                display,
                Localization.getInstance().strings().get("kWarning"),
                Localization.getInstance().strings().get("kUnableToImportBookmarks"),
                null,
                AlertType.ERROR
        );
    }

    private void showAboutUnableToExport(DisplayThreadSafe display) {
        ViewHelper.showMessageBox(
                display,
                Localization.getInstance().strings().get("kWarning"),
                Localization.getInstance().strings().get("kUnableToExportBookmarks"),
                null,
                AlertType.ERROR
        );
    }

    /**
     * @param rssFeed
     * @see IRssFeed
     */
    public void onContentLoaded(IRssFeed rssFeed, String loadedContent) {
        byte[] xmlByteArray = loadedContent.getBytes();
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xmlByteArray);
        InputStreamReader xmlReader = new InputStreamReader(xmlStream);

        rssFeed.removeAllRssItems();

        IRssParser parser;
        try {
            parser = RssParserProvider.createParser(
                    RssParserProvider.getRssVersion(loadedContent));
            parser.parse(new RssInputStream(xmlReader), rssFeed);

            RssItemsListView itemsView = new RssItemsListView(
                    rssFeed.getLink(), this._mainForm.getMIDlet(), this._mainForm);
            itemsView.attachController(new RssItemsListController(itemsView, rssFeed));
            itemsView.updateView();

            this._mainForm.getMIDlet().getDisplay().setCurrent(itemsView);
        } catch (Exception e) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(e);
            //#endif
            showErrorMessage(this._mainForm.getMIDlet().getDisplay(), Localization.getInstance().strings().get("kUnableToLoadRss"));
        }
    }

    public void onContentLoadError(IRssFeed rssFeed, Exception exception) {
        showErrorMessage(this._mainForm.getMIDlet().getDisplay(), Localization.getInstance().strings().get("kUnableToLoadRss"));
    }

    private void showErrorMessage(DisplayThreadSafe display, String message) {
        ViewHelper.showMessageBox(
                display,
                Localization.getInstance().strings().get("kError"),
                message,
                null,
                AlertType.ERROR
        );
    }
}
