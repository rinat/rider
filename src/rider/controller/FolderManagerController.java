/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rider.controller;


import rider.Icons;
import rider.Localization;
import rider.utils.InternalLogger;
import rider.view.standard.FolderManagerView;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Provide file system browsing and folder selecting
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class FolderManagerController implements CommandListener, IController, Runnable {

    // commands
    private Command _openCommand = new Command(Localization.getInstance().strings().get("kOpen"), Command.ITEM, 1);
    private Command _upCommand = new Command(Localization.getInstance().strings().get("kUp"), Command.BACK, 2);
    private Command _selectCommand = new Command(Localization.getInstance().strings().get("kSelect"), Command.ITEM, 3);
    private Command _cancelCommand = new Command(Localization.getInstance().strings().get("kCancel"), Command.CANCEL, 3);
    // views
    FolderManagerView _view = null;
    // members
    private String[] _roots = null;
    private String _currentDirectory = "";
    // listener
    IFolderManagerListerner _listener = null;
    // static
    private final static String SEP_STR = "/";
    private final static char SEP_CHAR = SEP_STR.charAt(0);
    private final static String ROOT_PREFIX = "file:///";

    /**
     * @param view
     * @throws Exception
     * @see FolderManagerView
     */
    public FolderManagerController(FolderManagerView view) throws Exception {
        this._view = view;

        addCommands(_view);
        new Thread(this).start();
    }

    public void run() {
        try {
            show();
        } catch (Exception e) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(e);
            //#endif
        }
    }

    /**
     * @param listener
     * @see IFolderManagerListerner
     */
    public void attachFileManagerListener(IFolderManagerListerner listener) {
        if (listener == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("FolderManagerController.attachFileManagerListener"));
            //#endif
            throw new NullPointerException("FolderManagerController.attachFileManagerListener");
        }
        this._listener = listener;
    }

    /**
     * @see IController
     */
    public void updateView() {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == this._openCommand || c.getCommandType() == Command.OK) {
            openCommand();
        } else if (c == this._upCommand || c.getCommandType() == Command.BACK) {
            upCommand();
        } else if (c == _cancelCommand) {
            this._view.getMIDlet().getDisplay().setCurrent(this._view.getBackDisplay().asDisplayable());
        } else if (c == _selectCommand) {
            useCommand();
        }
    }

    public static boolean isFileManagerSupported() {
        return (System.getProperty("microedition.io.file.FileConnection.version") != null);
    }

    private void useCommand() {
        int selectedIndex = this._view.getSelectedIndex();
        String selectedFolder = this._view.getString(selectedIndex);
        onFolderSelected(ROOT_PREFIX + _currentDirectory + selectedFolder);
    }

    private void addCommands(FolderManagerView view) {
        view.addCommand(this._upCommand);
        view.addCommand(this._cancelCommand);
        view.addCommand(this._selectCommand);
        view.setSelectCommand(this._openCommand);
        view.setCommandListener(this);
    }

    private String[] getRoots() {
        try {
            if (!isFileManagerSupported()) {
                throw new Exception(Localization.getInstance().strings().get("kFileBrowsingIsNotPermittedInYourHandSet"));
            }
            Enumeration enumeration = FileSystemRegistry.listRoots();
            Vector temp = new Vector();
            String root = "";
            while (enumeration.hasMoreElements()) {
                root = (String) enumeration.nextElement();
                temp.addElement(root);
            }
            String[] ret = new String[temp.size()];
            temp.copyInto(ret);
            return ret;

        } catch (Exception exception) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(exception);
            //#endif
            return null;
        }

    }

    private Vector getDirectoryListing(String directory) throws Exception {
        try {
            Vector tempVector;
            String tempString;

            try {
                if (directory.equals("")) {
                    if (_roots == null) {
                        _roots = getRoots();
                        if (_roots == null) {
                            return null;
                        }
                    }

                    tempVector = new Vector();
                    for (int i = 0; i < _roots.length; i++) {
                        tempVector.addElement(_roots[i]);
                    }
                    _view.removeCommand(_upCommand);

                } else {
                    FileConnection fileConnection = (FileConnection) Connector.open(ROOT_PREFIX + directory, Connector.READ);
                    if (!fileConnection.exists()) {
                        throw new Exception(Localization.getInstance().strings().get("kCannotOpenDirectory") + ": " + ROOT_PREFIX + directory);
                    }
                    tempVector = new Vector();
                    for (Enumeration enumer = fileConnection.list(); enumer.hasMoreElements();) {
                        tempString = (String) enumer.nextElement();
                        tempVector.addElement(tempString);
                    }
                    fileConnection.close();
                }
            } catch (Exception exception) {
                //#ifdef rider.debugEnabled
                InternalLogger.getInstance().fatal(exception);
                //#endif
                throw new Exception(exception.toString());
            }
            return tempVector;
        } catch (Exception e) {
            throw e;
        }
    }

    private void show() throws Exception {
        try {
            _view.deleteAll();
            Vector dir = getDirectoryListing(_currentDirectory);

            for (int i = 0; i < dir.size(); i++) {
                String fname = (String) dir.elementAt(i);
                if (isDirectory(fname)) {
                    Image dirIcon = Icons.getInstance().getDirIcon();
                    _view.append(fname, dirIcon);
                }
            }
            this._view.getMIDlet().getDisplay().setCurrent(_view);
        } catch (Exception ex) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(ex);
            //#endif
            throw ex;
        }
    }

    private boolean isDirectory(String directory) {
        if (directory.charAt(directory.length() - 1) == SEP_CHAR) {
            return true;
        } else {
            return false;
        }
    }

    private void onFolderSelected(String filename) {
        if (this._listener != null) {
            _listener.onFolderSelected(filename);
        }
    }

    private void upCommand() {
        Thread t = new Thread(new Runnable() {

            public void run() {
                int index = _currentDirectory.lastIndexOf(SEP_CHAR);
                _currentDirectory = _currentDirectory.substring(0, index);
                index = _currentDirectory.lastIndexOf(SEP_CHAR);
                _currentDirectory = _currentDirectory.substring(0, index + 1);
                try {
                    show();
                } catch (Exception ex) {
                    //#ifdef rider.debugEnabled
                    InternalLogger.getInstance().fatal(ex);
                    //#endif
                }
            }
        });
        t.start();
    }

    private void openCommand() {
        String selected = _view.getString(_view.getSelectedIndex());
        _currentDirectory += selected;
        if (isDirectory(selected)) {
            Thread t = new Thread(new Runnable() {

                public void run() {
                    try {
                        show();
                    } catch (Exception ex) {
                        //#ifdef rider.debugEnabled
                        InternalLogger.getInstance().fatal(ex);
                        //#endif
                    }
                }
            });
            t.start();
        }
    }
}
