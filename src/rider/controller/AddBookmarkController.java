/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rider.controller;


import rider.Localization;
import rider.core.bookmarks.Bookmark;
import rider.core.bookmarks.IBookmark;
import rider.core.bookmarks.IBookmarkCollection;
import rider.utils.InternalLogger;
import rider.utils.ViewHelper;
import rider.view.DisplayThreadSafe;
import rider.view.IView;
import rider.view.standard.AddBookmarkView;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * Provide bookmark creation
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see AddBookmarkView
 */
public class AddBookmarkController implements IController, CommandListener {

    // commands
    private Command _backCommand = new Command(Localization.getInstance().strings().get("kBack"), Command.CANCEL, 1);
    private Command _saveBookmark = new Command(Localization.getInstance().strings().get("kSave"), Command.SCREEN, 2);
    // view
    private AddBookmarkView _view = null;
    private IBookmarkCollection _bookmarks = null;

    /**
     * @param view      must be not null
     * @param bookmarks must be not null
     * @see IBookmarkCollection
     */
    public AddBookmarkController(AddBookmarkView view, IBookmarkCollection bookmarks) {
        if (view == null || bookmarks == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("AddBookmarkController.AddBookmarkController"));
            //#endif
            throw new NullPointerException("AddBookmarkController.AddBookmarkController");
        }
        this._view = view;
        this._bookmarks = bookmarks;
        updateView();
        addCommands(this._view);
    }

    /**
     * @see IController
     */
    public void updateView() {
    }

    /**
     * @param view
     * @see AddBookmarkView
     */
    private void addCommands(AddBookmarkView view) {
        view.addCommand(_backCommand);
        view.addCommand(_saveBookmark);
        view.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == _backCommand) {
            backCommand(this._view);
        } else if (c == _saveBookmark) {
            saveCommand();
        }
    }

    private void backCommand(IView view) {
        IView backDisplay = view.getBackDisplay();
        backDisplay.updateView();
        view.getMIDlet().getDisplay().setCurrent(backDisplay.asDisplayable());
    }

    /**
     * save current bookmarks to xml file
     */
    private void saveCommand() {
        DisplayThreadSafe display = this._view.getMIDlet().getDisplay();

        String newBookmarkName = this._view.getBookmarkTitle();
        if (newBookmarkName.length() == 0) {
            showWarning(display, Localization.getInstance().strings().get("kEmptyBookmarkName"));
        } else if (existsBookmarkByName(newBookmarkName, _bookmarks)) {
            showWarning(display, Localization.getInstance().strings().get("kThisBookmarkAlreadyExists"));
        } else {
            this._bookmarks.addBookmark(new Bookmark(newBookmarkName));
            backCommand(this._view);
        }
    }

    /**
     * checking by name is bookmarks already exist
     *
     * @param bookmarkName
     * @param bookmarks
     * @return exist or not bookmark on collection already
     * @see IBookmarkCollection
     */
    private boolean existsBookmarkByName(String bookmarkName, IBookmarkCollection bookmarks) {
        for (int elementIndex = 0; elementIndex < bookmarks.getCount(); ++elementIndex) {
            IBookmark bookmark = bookmarks.getBookmark(elementIndex);
            if (bookmark.getTitle().equals(bookmarkName)) {
                return (true);
            }
        }
        return (false);
    }

    /**
     * @param display
     * @param message
     * @see DisplayThreadSafe
     */
    private void showWarning(DisplayThreadSafe display, String message) {
        ViewHelper.showMessageBox(
                display,
                Localization.getInstance().strings().get("kWarning"),
                message,
                null,
                AlertType.WARNING
        );
    }
}
