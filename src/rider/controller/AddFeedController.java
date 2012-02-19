/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rider.controller;


import rider.Localization;
import rider.core.rss.feed.IRssFeedCollection;
import rider.core.rss.feed.RssFeed;
import rider.utils.InternalLogger;
import rider.utils.ViewHelper;
import rider.view.DisplayThreadSafe;
import rider.view.IView;
import rider.view.standard.AddFeedView;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * Provide rss feed creation
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class AddFeedController implements IController, CommandListener {

    // commands
    private Command _backCommand = new Command(Localization.getInstance().strings().get("kBack"), Command.CANCEL, 1);
    private Command _saveCommand = new Command(Localization.getInstance().strings().get("kSave"), Command.ITEM, 2);
    // view
    AddFeedView _view = null;
    IRssFeedCollection _rssFeedCollection = null;

    public AddFeedController(AddFeedView view, IRssFeedCollection feeds) {
        if (view == null || feeds == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("AddFeedController.AddFeedController"));
            //#endif
            throw new NullPointerException("AddFeedController.AddFeedController");
        }
        this._view = view;
        this._rssFeedCollection = feeds;
        updateView();
        addCommands(_view);
    }

    /**
     * @see IController
     */
    public void updateView() {
        updateFrom(this._rssFeedCollection);
    }

    /**
     * @param view
     * @see AddFeedView
     */
    private void addCommands(AddFeedView view) {
        view.addCommand(_backCommand);
        view.addCommand(_saveCommand);
        view.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == _backCommand) {
            backCommand(this._view);
        } else if (c == _saveCommand) {
            saveCommand();
        }
    }

    /**
     * save feed with entered name to feed collection
     */
    private void saveCommand() {
        DisplayThreadSafe display = this._view.getMIDlet().getDisplay();

        String newBookmarkName = this._view.getFeedTitle();
        if (newBookmarkName.length() != 0) {
            this._rssFeedCollection.addRssFeed(new RssFeed(newBookmarkName));
            backCommand(this._view);
        } else {
            showEmptyTitleField(display);
        }
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
     * show warning about empty title field
     *
     * @param display
     * @see DisplayThreadSafe
     */
    private void showEmptyTitleField(DisplayThreadSafe display) {
        ViewHelper.showMessageBox(
                display,
                Localization.getInstance().strings().get("kWarning"),
                Localization.getInstance().strings().get("kEmptyFeedName"),
                null,
                AlertType.WARNING
        );
    }

    /**
     * @param bookmarks
     * @see IRssFeedCollection
     */
    private void updateFrom(IRssFeedCollection bookmarks) {
    }
}
