/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rider.controller;


import rider.Localization;
import rider.core.bookmarks.IBookmark;
import rider.utils.InternalLogger;
import rider.utils.ViewHelper;
import rider.view.DisplayThreadSafe;
import rider.view.IView;
import rider.view.standard.RenameBookmarkView;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * Provide operations for RenameBookmarkView
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class RenameBookmarkController implements CommandListener, IController {

    // commands
    private Command _backCommand = new Command(Localization.getInstance().strings().get("kBack"), Command.CANCEL, 1);
    private Command _saveCommand = new Command(Localization.getInstance().strings().get("kSave"), Command.ITEM, 2);
    // view
    private RenameBookmarkView _view = null;
    private IBookmark _bookmark = null;

    public RenameBookmarkController(RenameBookmarkView view, IBookmark bookmark) throws NullPointerException {
        if (view == null || bookmark == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("RenameBookmarkController.RenameBookmarkController"));
            //#endif
            throw new NullPointerException("RenameBookmarkController.RenameBookmarkController");
        }
        this._view = view;
        this._bookmark = bookmark;
        updateView();
        addCommand(this._view);
    }

    /**
     * @see IController
     */
    public void updateView() {
        this._view.setBookmarkTitle(_bookmark.getTitle());
    }

    private void addCommand(RenameBookmarkView view) {
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

        String newBookmarkName = this._view.getBookmarkTitle();
        if (newBookmarkName.length() != 0) {
            this._bookmark.setTitle(newBookmarkName);
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
                Localization.getInstance().strings().get("kEmptyBookmarkName"),
                null,
                AlertType.WARNING
        );
    }
}
