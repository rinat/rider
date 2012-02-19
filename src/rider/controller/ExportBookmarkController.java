/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rider.controller;


import rider.Localization;
import rider.core.bookmarks.IBookmarkCollection;
import rider.core.bookmarks.serialization.xml.BookmarkCollectionXmlSerializer;
import rider.core.file.FileWriter;
import rider.core.file.FileWriterListener;
import rider.utils.InternalLogger;
import rider.utils.ViewHelper;
import rider.view.DisplayThreadSafe;
import rider.view.IView;
import rider.view.standard.ExportBookmarkView;
import rider.view.standard.FolderManagerView;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * Provide bookmarks exporting
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class ExportBookmarkController implements CommandListener, IController, IFolderManagerListerner {

    // commands
    private Command _backCommand = new Command(Localization.getInstance().strings().get("kBack"), Command.CANCEL, 1);
    private Command _cancelCommand = new Command(Localization.getInstance().strings().get("kCancel"), Command.CANCEL, 2);
    private Command _saveCommand = new Command(Localization.getInstance().strings().get("kSaveBookmarks"), Command.ITEM, 3);
    // view
    IBookmarkCollection _bookmarks = null;
    ExportBookmarkView _inputFileNameView = null;
    FolderManagerView _folderManagerView = null;

    /**
     * @param view      must be not null
     * @param bookmarks must be not null
     * @throws Exception
     */
    public ExportBookmarkController(ExportBookmarkView view, IBookmarkCollection bookmarks) throws Exception {
        if (view == null || bookmarks == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("ExportBookmarkController.ExportBookmarkController"));
            //#endif
            throw new NullPointerException("ExportBookmarkController.ExportBookmarkController");
        }
        this._inputFileNameView = view;
        this._bookmarks = bookmarks;

        addCommands(this._inputFileNameView);

        _folderManagerView = new FolderManagerView(this._inputFileNameView.getMIDlet(),
                this._inputFileNameView.getBackDisplay());

        FolderManagerController folderManagerController = new FolderManagerController(_folderManagerView);
        folderManagerController.attachFileManagerListener(this);
        _folderManagerView.attachController(folderManagerController);
        this._inputFileNameView.getMIDlet().getDisplay().setCurrent(_folderManagerView);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == _backCommand) {
            backCommand(this._inputFileNameView);
        } else if (c == _cancelCommand) {
            cancelCommand();
        } else if (c == _saveCommand) {
            saveCommand();
        }
    }

    private void backCommand(IView view) {
        IView backDisplay = view.getBackDisplay();
        backDisplay.updateView();
        view.getMIDlet().getDisplay().setCurrent(backDisplay.asDisplayable());
    }

    private void cancelCommand() {
        this._inputFileNameView.getMIDlet().getDisplay().setCurrent(
                _inputFileNameView.getBackDisplay().asDisplayable());
    }

    private void saveCommand() {
        class FileWrListener implements FileWriterListener {

            public void OnFileWritten(String content) {
                _inputFileNameView.getMIDlet().getDisplay().setCurrent(
                        _folderManagerView.getBackDisplay().asDisplayable());
            }

            public void onFileWriteError(Exception e) {
                showUnableToSave(_inputFileNameView.getPath(),
                        _inputFileNameView.getMIDlet().getDisplay());
            }
        }

        BookmarkCollectionXmlSerializer serialzer = new BookmarkCollectionXmlSerializer();
        String xmlBookmarks = serialzer.serialize(_bookmarks);
        FileWriter writer = new FileWriter(this._inputFileNameView.getPath(), xmlBookmarks, new FileWrListener());
        writer.start();
    }

    /**
     * @see IController
     */
    public void updateView() {
    }

    public void onFolderSelected(String folder) {
        this._inputFileNameView.getMIDlet().getDisplay().setCurrent(_inputFileNameView);
        _inputFileNameView.setPath(folder);
    }

    public void onFolderManagerError(Exception e) {
        showErrorMessage(this._folderManagerView.getMIDlet().getDisplay(), Localization.getInstance().strings().get("kUnableToExecuteFolderOperation"));
    }

    /**
     * @param view
     * @see ExportBookmarkView
     */
    private void addCommands(ExportBookmarkView view) {
        view.addCommand(_backCommand);
        view.addCommand(_cancelCommand);
        view.addCommand(_saveCommand);
        view.setCommandListener(this);
    }

    /**
     * @param filename
     * @param display
     * @see DisplayThreadSafe
     */
    private void showUnableToSave(String filename, DisplayThreadSafe display) {
        ViewHelper.showMessageBox(
                display,
                Localization.getInstance().strings().get("kError"),
                Localization.getInstance().strings().get("kUnableToSaveFile") + " " + filename,
                null,
                AlertType.ERROR
        );
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
