/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rider.controller;


import rider.Icons;
import rider.Localization;
import rider.core.bookmarks.IBookmark;
import rider.core.bookmarks.IBookmarkCollection;
import rider.utils.InternalLogger;
import rider.utils.ViewHelper;
import rider.view.DisplayThreadSafe;
import rider.view.IView;
import rider.view.standard.AddBookmarkView;
import rider.view.standard.BookmarksListView;
import rider.view.standard.RenameBookmarkView;
import rider.view.standard.RssFeedsListView;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class BookmarksListController implements CommandListener, IController {

    // commands
    private Command _backCommand = new Command(Localization.getInstance().strings().get("kBack"), Command.CANCEL, 1);
    private Command _loadBookmark = new Command(Localization.getInstance().strings().get("kViewBookmark"), Command.ITEM, 2);
    private Command _renameBookmark = new Command(Localization.getInstance().strings().get("kRenameBookmark"), Command.ITEM, 3);
    private Command _addBookmark = new Command(Localization.getInstance().strings().get("kAddBookmark"), Command.SCREEN, 4);
    private Command _removeBookmark = new Command(Localization.getInstance().strings().get("kRemoveBookmark"), Command.SCREEN, 5);
    // views
    private BookmarksListView _bookmarksListView = null;
    private IBookmarkCollection _bookmarks = null;

    public BookmarksListController(BookmarksListView bookmarksListView, IBookmarkCollection bookmarks) {
        if (bookmarksListView == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("BookmarksListController.BookmarksListController"));
            //#endif
            throw new NullPointerException("BookmarksListController.BookmarksListController");
        }
        this._bookmarksListView = bookmarksListView;
        this._bookmarks = bookmarks;
        updateFrom(this._bookmarks);
        addCommands(this._bookmarksListView);
    }

    /**
     * @param bookmarks
     * @see IBookmarkCollection
     */
    private void updateFrom(IBookmarkCollection bookmarks) {
        this._bookmarksListView.saveSelection();
        this._bookmarksListView.deleteAll();
        for (int bookmarkIndex = 0; bookmarkIndex < bookmarks.getCount(); ++bookmarkIndex) {
            _bookmarksListView.append(bookmarks.getBookmark(bookmarkIndex).getTitle(),
                    Icons.getInstance().getBookmarkIcon());
        }
        this._bookmarksListView.rollbackSelection();
    }

    /**
     * @see IController
     */
    public void updateView() {
        updateFrom(this._bookmarks);
    }

    /**
     * @param bookmarksListView
     * @see BookmarksListView
     */
    private void addCommands(BookmarksListView bookmarksListView) {
        bookmarksListView.addCommand(this._backCommand);
        bookmarksListView.addCommand(this._renameBookmark);
        bookmarksListView.addCommand(this._addBookmark);
        bookmarksListView.addCommand(this._removeBookmark);
        bookmarksListView.setSelectCommand(this._loadBookmark);
        bookmarksListView.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == this._backCommand) {
            backCommand(this._bookmarksListView);
        } else if (c == this._loadBookmark) {
            this._bookmarksListView.saveSelection();
            loadBookmarkCommand();
        } else if (c == this._renameBookmark) {
            this._bookmarksListView.saveSelection();
            renameBookmarkCommand();
        } else if (c == this._addBookmark) {
            this._bookmarksListView.saveSelection();
            addBookmark();
        } else if (c == this._removeBookmark) {
            this._bookmarksListView.saveSelection();
            removeBookmark();
        }
    }

    private void addBookmark() {
        AddBookmarkView addBookmarkView = new AddBookmarkView(
                this._bookmarksListView.getMIDlet(), this._bookmarksListView);
        addBookmarkView.attachController(new AddBookmarkController(addBookmarkView, _bookmarks));
        this._bookmarksListView.getMIDlet().getDisplay().setCurrent(addBookmarkView);
    }

    private void removeBookmark() {
        if (_bookmarks.getCount() == 0) {
            showEmptyListInfo(this._bookmarksListView.getMIDlet().getDisplay());
            return;
        }
        int selectedItem = this._bookmarksListView.getSelectedIndex();
        this._bookmarks.removeBookmark(selectedItem);
        this._bookmarksListView.delete(selectedItem);
    }

    private void renameBookmarkCommand() {
        DisplayThreadSafe display = this._bookmarksListView.getMIDlet().getDisplay();
        if (_bookmarks.getCount() == 0) {
            showEmptyListInfo(display);
            return;
        }
        int selectedItem = this._bookmarksListView.getSelectedIndex();
        IBookmark bookmark = _bookmarks.getBookmark(selectedItem);

        RenameBookmarkView renameBookmarkView = new RenameBookmarkView(
                this._bookmarksListView.getMIDlet(), this._bookmarksListView);
        renameBookmarkView.attachController(new RenameBookmarkController(renameBookmarkView, bookmark));

        display.setCurrent(renameBookmarkView);
    }

    private void loadBookmarkCommand() {
        if (_bookmarks.getCount() == 0) {
            showEmptyListInfo(this._bookmarksListView.getMIDlet().getDisplay());
            return;
        }
        int selectedItem = this._bookmarksListView.getSelectedIndex();
        IBookmark bookmark = _bookmarks.getBookmark(selectedItem);

        RssFeedsListView feedsView = new RssFeedsListView(
                bookmark.getTitle(), this._bookmarksListView.getMIDlet(), this._bookmarksListView);
        feedsView.attachController(new RssFeedsListController(feedsView, bookmark.getRssFeeds()));

        this._bookmarksListView.getMIDlet().getDisplay().setCurrent(feedsView);
    }

    /**
     * @param view
     * @see DisplayThreadSafe
     */
    private void backCommand(IView view) {
        IView backDisplay = view.getBackDisplay();
        backDisplay.updateView();
        view.getMIDlet().getDisplay().setCurrent(backDisplay.asDisplayable());
    }

    /**
     * @param display
     * @see DisplayThreadSafe
     */
    private void showEmptyListInfo(DisplayThreadSafe display) {
        ViewHelper.showMessageBox(
                display,
                Localization.getInstance().strings().get("kWarning"),
                Localization.getInstance().strings().get("kEmptyBookmarksList"),
                null,
                AlertType.INFO
        );
    }
}
