/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rider.controller;


import rider.Localization;
import rider.core.rss.feed.IRssFeed;
import rider.utils.InternalLogger;
import rider.utils.ViewHelper;
import rider.view.DisplayThreadSafe;
import rider.view.IView;
import rider.view.standard.RenameFeedView;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * Provide bookmark renaming
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class RenameFeedController implements CommandListener, IController {

    // commands
    private Command _backCommand = new Command(Localization.getInstance().strings().get("kBack"), Command.CANCEL, 1);
    private Command _saveCommand = new Command(Localization.getInstance().strings().get("kSave"), Command.ITEM, 2);
    // view
    private RenameFeedView _view = null;
    private IRssFeed _feed = null;

    /**
     * @param view must be not null
     * @param feed must be not null
     * @throws NullPointerException
     */
    public RenameFeedController(RenameFeedView view, IRssFeed feed) throws NullPointerException {
        if (view == null || feed == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("RenameFeedController.RenameFeedController"));
            //#endif
            throw new NullPointerException("RenameFeedController.RenameFeedController");
        }
        this._view = view;
        this._feed = feed;
        updateView();
        addCommand(this._view);
    }

    /**
     * @see IController
     */
    public void updateView() {
        this._view.setBookmarkTitle(_feed.getLink());
    }

    /**
     * @param view
     * @see RenameFeedView
     */
    private void addCommand(RenameFeedView view) {
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

    private void saveCommand() {
        DisplayThreadSafe display = this._view.getMIDlet().getDisplay();

        String newLink = this._view.getBookmarkTitle();
        if (newLink.length() != 0) {
            this._feed.setLink(newLink);
            backCommand(this._view);
        } else {
            showEmptyTitleField(display);
        }
    }

    private void backCommand(IView view) {
        IView backDisplay = view.getBackDisplay();
        backDisplay.updateView();
        view.getMIDlet().getDisplay().setCurrent(backDisplay.asDisplayable());
        backDisplay.rollbackSelection();
    }

    /**
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
}
