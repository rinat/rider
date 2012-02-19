package rider.view.standard;

import rider.Localization;
import rider.controller.FileManagerController;
import rider.controller.IController;
import rider.view.IView;
import rider.view.MIDletThreadSafe;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * file manager view
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see FileManagerController
 */
public class FileManagerView extends List implements IView {

    private IView _backDisplay = null;
    private IController _controller = null;
    private MIDletThreadSafe _midlet = null;

    public FileManagerView(MIDletThreadSafe midlet, IView backDisplay) throws NullPointerException {
        super(Localization.getInstance().strings().get("kFileManager"), List.IMPLICIT);
        if (midlet == null) {
            throw new NullPointerException("FileManagerView.FileManagerView");
        }
        this._midlet = midlet;
        this._backDisplay = backDisplay;
    }

    /**
     * @see IView
     */
    public IView getBackDisplay() {
        return (this._backDisplay);
    }

    /**
     * @see IView
     */
    public MIDletThreadSafe getMIDlet() {
        return (this._midlet);
    }

    /**
     * @param controller behavior for this view must be not null
     * @throws NullPointerException
     */
    public void attachController(IController controller) throws NullPointerException {
        if (controller == null) {
            throw new NullPointerException("FileManagerView.attachController");
        }
        this._controller = controller;
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
