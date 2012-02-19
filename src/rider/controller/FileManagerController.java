/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rider.controller;


import rider.Icons;
import rider.Localization;
import rider.utils.InternalLogger;
import rider.view.standard.FileManagerView;

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
 * Provide file system browsing and file selecting
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class FileManagerController implements CommandListener, IController, Runnable {

    // commands    
    private Command _openCommand = new Command(Localization.getInstance().strings().get("kOpen"), Command.ITEM, 1);
    private Command _upCommand = new Command(Localization.getInstance().strings().get("kUp"), Command.BACK, 2);
    private Command _cancelCommand = new Command(Localization.getInstance().strings().get("kCancel"), Command.CANCEL, 3);
    // views
    FileManagerView _fileSelectorView = null;
    // members
    private String[] _roots = null;
    private String _currentDirectory = "";
    private String _selectedFileName = "";
    private Vector _supportedExtensions = new Vector();
    // listener
    IFileManagerListener _filemanagerListener = null;
    // static
    private final static String SEP_STR = "/";
    private final static char SEP_CHAR = SEP_STR.charAt(0);
    private final static String ROOT_PREFIX = "file://localhost/";

    public FileManagerController(FileManagerView view, Vector fileTypesFilter) throws Exception {
        this._fileSelectorView = view;
        this._supportedExtensions = fileTypesFilter;
        addCommands();

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
     * @see IFileManagerListener
     */
    public void attachFileManagerListener(IFileManagerListener listener) {
        if (listener == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("FileManagerController.attachFileManagerListener"));
            //#endif
            throw new NullPointerException("FileManagerController.attachFileManagerListener");
        }
        this._filemanagerListener = listener;
    }

    /**
     * @see IController
     */
    public void updateView() {
    }

    public String getSelectedFileName() {
        return _selectedFileName;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == this._openCommand || c.getCommandType() == Command.OK) {
            openCommand();
        } else if (c == this._upCommand || c.getCommandType() == Command.BACK) {
            upCommand();
        } else if (c == _cancelCommand) {
            this._fileSelectorView.getMIDlet().getDisplay().
                    setCurrent(this._fileSelectorView.getBackDisplay().asDisplayable());
        }
    }

    public static boolean isFileManagerSupported() {
        return (System.getProperty("microedition.io.file.FileConnection.version") != null);
    }

    private void addCommands() {
        _fileSelectorView.addCommand(this._upCommand);
        _fileSelectorView.addCommand(this._cancelCommand);
        _fileSelectorView.setSelectCommand(this._openCommand);
        _fileSelectorView.setCommandListener(this);
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
                    _fileSelectorView.removeCommand(_upCommand);

                } else {
                    FileConnection fileConnection = (FileConnection) Connector.open(ROOT_PREFIX + directory, Connector.READ);
                    if (!fileConnection.exists()) {
                        throw new Exception(Localization.getInstance().strings().get("kCannotOpenFile") + ": " + ROOT_PREFIX + directory);
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
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(e);
            //#endif
            throw e;
        }
    }

    private void show() throws Exception {
        try {
            _fileSelectorView.deleteAll();
            addCommands();
            Vector dir = getDirectoryListing(_currentDirectory);

            for (int i = 0; i < dir.size(); i++) {
                String fname = (String) dir.elementAt(i);
                if (isDirectory(fname)) {
                    Image fileIcon = Icons.getInstance().getDirIcon();
                    _fileSelectorView.append(fname, fileIcon);
                }
                if (isFileTypeSupported(fname, this._supportedExtensions)) {
                    Image dirIcon = Icons.getInstance().getBookmarksFileIcon();
                    _fileSelectorView.append(fname, dirIcon);
                }
            }
            this._fileSelectorView.getMIDlet().getDisplay().setCurrent(_fileSelectorView);
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

    private String getFileType(String fileName) {
        char ch = '.';
        int index = fileName.lastIndexOf((int) ch);
        return fileName.substring(index + 1);
    }

    private boolean isFileTypeSupported(String fileName, Vector supprotedFileTypes) {
        if (supprotedFileTypes.size() == 0) {
            return (true);
        }
        String extension = getFileType(fileName);
        for (Enumeration elements = supprotedFileTypes.elements(); elements.hasMoreElements();) {
            String supportedFileType = (String) elements.nextElement();
            if (supportedFileType.equals(extension)) {
                return (true);
            }
        }
        return (false);
    }

    private void onFileSelected(String filename) {
        if (this._filemanagerListener != null) {
            _filemanagerListener.onFileSelected(filename);
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
        String selected = _fileSelectorView.getString(_fileSelectorView.getSelectedIndex());
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
        } else {
            if (isFileTypeSupported(selected, this._supportedExtensions)) {
                _selectedFileName = selected;
                onFileSelected(ROOT_PREFIX + _currentDirectory);
            }
        }
    }
}
