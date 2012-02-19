package rider.view.standard;


import rider.Localization;
import rider.controller.ExportBookmarkController;
import rider.controller.IController;
import rider.utils.InternalLogger;
import rider.view.IView;
import rider.view.MIDletThreadSafe;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

/**
 * provides bookmarks export
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see ExportBookmarkController
 */
public class ExportBookmarkView extends Form implements IView {

    private MIDletThreadSafe _midlet = null;
    private IView _backdisplay = null;
    private IController _controller = null;
    private TextField _path = new TextField(Localization.getInstance().strings().get("kFileName") + ":", "", 100, TextField.PLAIN);

    public ExportBookmarkView(MIDletThreadSafe midlet, IView backDisplay) {
        super(Localization.getInstance().strings().get("kExportBookmark"));
        if (midlet == null || backDisplay == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("ExportBookmarkView.ExportBookmarkView"));
            //#endif
            throw new NullPointerException("ExportBookmarkView.ExportBookmarkView");
        }
        append(this._path);

        this._midlet = midlet;
        this._backdisplay = backDisplay;
    }

    /**
     * @param controller behavior for this view must be not null
     * @throws NullPointerException
     */
    public void attachController(IController controller) throws NullPointerException {
        if (controller == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException());
            //#endif
            throw new NullPointerException("ExportBookmarkView.attachController");
        }
        this._controller = controller;
    }

    public void setPath(String title) {
        this._path.setString(title);
    }

    public String getPath() {
        return (_path.getString());
    }

    /**
     * @see IView
     */
    public IView getBackDisplay() {
        return (_backdisplay);
    }

    /**
     * @see IView
     */
    public MIDletThreadSafe getMIDlet() {
        return (_midlet);
    }

    /**
     * @see IView
     */
    public void updateView() {
        _controller.updateView();
    }

    /**
     * @see IView
     */
    public Displayable asDisplayable() {
        return (this);
    }

    public void rollbackSelection() {
    }

    public void saveSelection() {
    }
}
