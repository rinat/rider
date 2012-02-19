/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rider.controller;


import rider.Icons;
import rider.Localization;
import rider.core.browser.DefaultBrowser;
import rider.core.rss.feed.IRssFeed;
import rider.core.rss.item.IRssItem;
import rider.core.rss.item.IRssItemState;
import rider.utils.InternalLogger;
import rider.utils.ViewHelper;
import rider.view.DisplayThreadSafe;
import rider.view.IView;
import rider.view.standard.RssItemView;
import rider.view.standard.RssItemsListView;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.*;

/**
 * Provide operation over RssItemsListView
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see RssItemsListView
 */
public class RssItemsListController implements CommandListener, IController {

    // commands
    private Command _backCommand = new Command(Localization.getInstance().strings().get("kBack"), Command.BACK, 1);
    private Command _viewItemCommand = new Command(Localization.getInstance().strings().get("kViewItem"), Command.ITEM, 2);
    private Command _openOnDefaultBrowserCommand = new Command(Localization.getInstance().strings().get("kGoToFullDescription"), Command.ITEM, 3);
    // views
    private RssItemsListView _itemsList = null;
    private IRssFeed _feed = null;

    /**
     * @param itemsView must be not null
     * @param feed      must be not null
     * @throws NullPointerException
     */
    public RssItemsListController(RssItemsListView itemsView, IRssFeed feed) throws NullPointerException {
        if (itemsView == null || feed == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("RssItemsListController.RssItemsListController"));
            //#endif
            throw new NullPointerException("RssItemsListController.RssItemsListController");
        }
        this._feed = feed;
        this._itemsList = itemsView;
        addCommands(this._itemsList);
    }

    /**
     * @see IController
     */
    public void updateView() {
        this._itemsList.saveSelection();
        this._itemsList.deleteAll();
        for (int itemIndex = 0; itemIndex < _feed.getRssItems().getCount(); ++itemIndex) {
            Image readUnreadState = null;
            IRssItem rssItem = this._feed.getRssItems().getRssItem(itemIndex);
            if (rssItem != null) {
                if (rssItem.getRssItemState().getState() == IRssItemState.kUnreadState) {
                    readUnreadState = Icons.getInstance().getUnreadRssItemIcon();
                } else {
                    readUnreadState = Icons.getInstance().getReadRssItemIcon();
                }
            }
            this._itemsList.append(_feed.getRssItems().getRssItem(itemIndex).getTitle(), readUnreadState);
        }
        this._itemsList.rollbackSelection();
    }

    /**
     * @param itemsList
     * @see RssItemsListView
     */
    private void addCommands(RssItemsListView itemsList) {
        itemsList.addCommand(_backCommand);
        itemsList.setSelectCommand(_viewItemCommand);
        itemsList.addCommand(_openOnDefaultBrowserCommand);
        itemsList.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == _backCommand) {
            backCommand(this._itemsList);
        } else if (c == _viewItemCommand) {
            this._itemsList.saveSelection();
            viewItemCommand();
        } else if (c == _openOnDefaultBrowserCommand) {
            this._itemsList.saveSelection();
            openDefaultBrowserCommand();
        }
    }

    private void backCommand(IView view) {
        IView backDisplay = view.getBackDisplay();
        backDisplay.updateView();
        view.getMIDlet().getDisplay().setCurrent(backDisplay.asDisplayable());
        backDisplay.rollbackSelection();
    }

    private void viewItemCommand() {
        DisplayThreadSafe display = this._itemsList.getMIDlet().getDisplay();
        if (this._itemsList.size() == 0) {
            showEmptyListInfo(display);
            return;
        }

        int selectedItem = this._itemsList.getSelectedIndex();
        IRssItem item = this._feed.getRssItems().getRssItem(selectedItem);

        RssItemView view = new RssItemView(this._itemsList.getMIDlet(), this._itemsList);
        view.attachController(new RssItemController(view, item));
        display.setCurrent(view);
    }

    private void updateItemListView(IRssItem item) {
        if (item.getRssItemState().getState() == IRssItemState.kUnreadState) {
            item.getRssItemState().setState(IRssItemState.kReadState);
        }
        this._itemsList.updateView();
    }

    /**
     * open full description for IRssItem on default browser
     */
    private void openDefaultBrowserCommand() {
        DisplayThreadSafe display = this._itemsList.getMIDlet().getDisplay();
        if (this._itemsList.size() == 0) {
            showEmptyListInfo(display);
            return;
        }
        int selectedItem = this._itemsList.getSelectedIndex();
        IRssItem rssItem = this._feed.getRssItems().getRssItem(selectedItem);

        DefaultBrowser defaultBrowser = new DefaultBrowser();
        try {
            defaultBrowser.Open(rssItem.getLink(), this._itemsList.getMIDlet());
        } catch (NullPointerException ex) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(ex);
            //#endif
            showAboutErrorBrowserOpen(display, rssItem.getLink());
        } catch (ConnectionNotFoundException ex) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(ex);
            //#endif
            showAboutErrorBrowserOpen(display, rssItem.getLink());
        }
    }

    /**
     * @param display
     * @see DisplayThreadSafe
     */
    private void showEmptyListInfo(DisplayThreadSafe display) {
        ViewHelper.showMessageBox(
                display,
                Localization.getInstance().strings().get("kWarning"),
                Localization.getInstance().strings().get("kEmptyRssItemsList"),
                null,
                AlertType.INFO
        );
    }

    /**
     * @param display
     * @param link
     * @see DisplayThreadSafe
     */
    private void showAboutErrorBrowserOpen(DisplayThreadSafe display, String link) {
        ViewHelper.showMessageBox(
                display,
                Localization.getInstance().strings().get("kError"),
                Localization.getInstance().strings().get("kUnableToOpenOnBrowser") + " " + link,
                null,
                AlertType.ERROR
        );
    }
}
