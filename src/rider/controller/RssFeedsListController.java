/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rider.controller;


import rider.Icons;
import rider.Localization;
import rider.core.rss.feed.IRssFeed;
import rider.core.rss.feed.IRssFeedCollection;
import rider.core.rss.feed.IRssFeedState;
import rider.core.rss.loader.IRssContentListener;
import rider.core.rss.loader.RssContentLoader;
import rider.core.rss.loader.RssInputStream;
import rider.core.rss.parser.IRssParser;
import rider.core.rss.parser.RssParserProvider;
import rider.utils.InternalLogger;
import rider.utils.ViewHelper;
import rider.view.DisplayThreadSafe;
import rider.view.IView;
import rider.view.standard.AddFeedView;
import rider.view.standard.RenameFeedView;
import rider.view.standard.RssFeedsListView;
import rider.view.standard.RssItemsListView;

import javax.microedition.lcdui.*;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;


/**
 * Provide operation over RssFeedsListView
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see RssFeedsListView
 */
public class RssFeedsListController implements CommandListener, IController, IRssContentListener {

    // commands
    private Command _backCommand = new Command(Localization.getInstance().strings().get("kBack"), Command.CANCEL, 1);
    private Command _loadFeed = new Command(Localization.getInstance().strings().get("kLoadFeed"), Command.ITEM, 2);
    private Command _addFeed = new Command(Localization.getInstance().strings().get("kAddFeed"), Command.SCREEN, 4);
    private Command _removeFeed = new Command(Localization.getInstance().strings().get("kRemoveFeed"), Command.SCREEN, 6);
    private Command _renameFeed = new Command(Localization.getInstance().strings().get("kChangeFeed"), Command.SCREEN, 5);
    // views
    private RssFeedsListView _rssFeedsList = null;
    private IRssFeedCollection _feeds = null;

    public RssFeedsListController(RssFeedsListView feedsList, IRssFeedCollection feeds) throws NullPointerException {
        if (feedsList == null || feeds == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("RssFeedsListController.RssFeedsListController"));
            //#endif
            throw new NullPointerException("RssFeedsListController.RssFeedsListController");
        }
        this._rssFeedsList = feedsList;
        this._feeds = feeds;
        updateFrom(this._feeds);
        addCommands(this._rssFeedsList);
    }

    /**
     * @see IController
     */
    public void updateView() {
        updateFrom(this._feeds);
    }

    /**
     * @param itemsList
     * @see RssFeedsListView
     */
    private void addCommands(RssFeedsListView itemsList) {
        itemsList.addCommand(_backCommand);
        itemsList.addCommand(_addFeed);
        itemsList.addCommand(_renameFeed);
        itemsList.addCommand(_removeFeed);
        itemsList.setSelectCommand(_loadFeed);
        itemsList.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == _backCommand) {
            backCommand(this._rssFeedsList);
        } else if (c == _loadFeed) {
            this._rssFeedsList.saveSelection();
            loadFeedCommand();
        } else if (c == _removeFeed) {
            this._rssFeedsList.saveSelection();
            removeFeedCommand();
        } else if (c == _addFeed) {
            this._rssFeedsList.saveSelection();
            addFeedCommand();
        } else if (c == _renameFeed) {
            this._rssFeedsList.saveSelection();
            renameFeed();
        }
    }

    private void renameFeed() {
        if (this._rssFeedsList.size() == 0) {
            showEmptyTitleField(this._rssFeedsList.getMIDlet().getDisplay());
            return;
        }
        int selectedIndex = this._rssFeedsList.getSelectedIndex();

        RenameFeedView view = new RenameFeedView(
                this._rssFeedsList.getMIDlet(), _rssFeedsList);
        view.attachController(new RenameFeedController(view, _feeds.getRssFeed(selectedIndex)));
        this._rssFeedsList.getMIDlet().getDisplay().setCurrent(view);
    }

    private void showEmptyTitleField(DisplayThreadSafe display) {
        ViewHelper.showMessageBox(
                display,
                Localization.getInstance().strings().get("kWarning"),
                Localization.getInstance().strings().get("kEmptyFeedName"),
                null,
                AlertType.WARNING
        );
    }

    private void addFeedCommand() {
        AddFeedView view = new AddFeedView(this._rssFeedsList.getMIDlet(), _rssFeedsList);
        view.attachController(new AddFeedController(view, this._feeds));
        this._rssFeedsList.getMIDlet().getDisplay().setCurrent(view);
    }

    private void loadFeedCommand() {
        if (this._rssFeedsList.size() == 0) {
            showEmptyTitleField(this._rssFeedsList.getMIDlet().getDisplay());
            return;
        }
        int selectedIndex = this._rssFeedsList.getSelectedIndex();
        IRssFeed rssFeed = _feeds.getRssFeed(selectedIndex);

        if (rssFeed.getRssFeedState().getState() == IRssFeedState.kLoadingState) {
            showAlert(this._rssFeedsList.getMIDlet().getDisplay(),
                    Localization.getInstance().strings().get("kWarning"),
                    Localization.getInstance().strings().get("kRssFeedAlreadyLoading"),
                    AlertType.INFO);
        } else if (rssFeed.getRssFeedState().getState() == IRssFeedState.kLoadedState) {
            showAlreadyLoadedRssItems(rssFeed);
        } else {
            rssFeed.getRssFeedState().setState(IRssFeedState.kLoadingState);
            RssContentLoader loader = new RssContentLoader(rssFeed, this);
            loader.start();

            updateFrom(this._feeds);
        }
    }

    private void showAlreadyLoadedRssItems(IRssFeed feed) {
        RssItemsListView itemsView = new RssItemsListView(
                feed.getLink(), this._rssFeedsList.getMIDlet(), this._rssFeedsList);
        itemsView.attachController(new RssItemsListController(itemsView, feed));
        itemsView.updateView();

        this._rssFeedsList.getMIDlet().getDisplay().setCurrent(itemsView);
    }

    private void loadAllFeedsCommand() {

    }

    private void removeFeedCommand() {
        if (this._rssFeedsList.size() == 0) {
            showAlert(this._rssFeedsList.getMIDlet().getDisplay(),
                    Localization.getInstance().strings().get("kWarning"),
                    Localization.getInstance().strings().get("kEmptyRssFeedsList"),
                    AlertType.INFO);
            return;
        }
        int selectedFeed = this._rssFeedsList.getSelectedIndex();

        this._feeds.removeRssFeed(selectedFeed);
        this._rssFeedsList.delete(selectedFeed);
    }

    private void backCommand(IView view) {
        IView backDisplay = view.getBackDisplay();
        backDisplay.updateView();
        view.getMIDlet().getDisplay().setCurrent(backDisplay.asDisplayable());
        backDisplay.rollbackSelection();
    }

    /**
     * @param feeds
     * @see IRssFeedCollection
     */
    private void updateFrom(IRssFeedCollection feeds) {
        this._rssFeedsList.saveSelection();
        this._rssFeedsList.deleteAll();
        for (int feedIndex = 0; feedIndex < feeds.getCount(); ++feedIndex) {
            IRssFeed feed = feeds.getRssFeed(feedIndex);

            Image icon = null;
            if (feed.getRssFeedState().getState() == IRssFeedState.kLoadedState) {
                icon = Icons.getInstance().getRssFeedLoadedIcon();    
            } else if (feed.getRssFeedState().getState() == IRssFeedState.kLoadingState) {
                icon = Icons.getInstance().getRssLoadingIcon();      
            } else if (feed.getRssFeedState().getState() == IRssFeedState.kNotLoadedState) {
                icon = Icons.getInstance().getRssNotLoadedIcon();
            } else if (feed.getRssFeedState().getState() == IRssFeedState.kLoadingErrorState) {
                icon = Icons.getInstance().getRssLoadingErrorIcon();
            }
            this._rssFeedsList.append(feed.getLink(), icon);
        }
        this._rssFeedsList.rollbackSelection();
    }

    /**
     * @param feed
     * @param loadedContent
     * @see IRssFeed
     */
    public void onContentLoaded(IRssFeed feed, String loadedContent) {
        feed.getRssFeedState().setState(IRssFeedState.kLoadedState);

        byte[] xmlByteArray = loadedContent.getBytes();
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xmlByteArray);
        InputStreamReader xmlReader = new InputStreamReader(xmlStream);

        feed.removeAllRssItems(); // todo swap loaded and old

        IRssParser parser;
        try {
            parser = RssParserProvider.createParser(
                    RssParserProvider.getRssVersion(loadedContent));
            parser.parse(new RssInputStream(xmlReader), feed);

            RssItemsListView itemsView = new RssItemsListView(
                    feed.getLink(), this._rssFeedsList.getMIDlet(), this._rssFeedsList);
            itemsView.attachController(new RssItemsListController(itemsView, feed));
            itemsView.updateView();

            this._rssFeedsList.getMIDlet().getDisplay().setCurrent(itemsView);
        } catch (Exception e) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(e);
            //#endif
            onContentLoadError(feed, e);
        }
    }

    /**
     * @param feed
     * @param exception
     * @see IRssFeed
     */
    public void onContentLoadError(IRssFeed feed, Exception exception) {
        feed.getRssFeedState().setState(IRssFeedState.kLoadingErrorState);
        updateFrom(this._feeds);
        showAlert(this._rssFeedsList.getMIDlet().getDisplay(),
                Localization.getInstance().strings().get("kError"),
                Localization.getInstance().strings().get("kUnableToLoadRss"),
                AlertType.ERROR);
    }

    /**
     * @param display
     * @see DisplayThreadSafe
     */
    private void showAlert(DisplayThreadSafe display, String title, String text, AlertType alertType) {
        ViewHelper.showMessageBox(
                display,
                title,
                text,
                null,
                alertType
        );
    }
}
