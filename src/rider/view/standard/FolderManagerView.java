package rider.view.standard;

import rider.Localization;
import rider.controller.FolderManagerController;
import rider.controller.IController;
import rider.view.IView;
import rider.view.MIDletThreadSafe;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * folder manager view
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see FolderManagerController
 */
public class FolderManagerView extends List implements IView {

    private IView _backDisplay = null;
    private IController _controller = null;
    private MIDletThreadSafe _midlet = null;

    public FolderManagerView(MIDletThreadSafe midlet, IView backDisplay) throws NullPointerException {
        super(Localization.getInstance().strings().get("kFolderManager"), List.IMPLICIT);
        if (midlet == null) {
            throw new NullPointerException("FolderManagerView.FolderManagerView");
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
            throw new NullPointerException("FolderManagerView.attachController");
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
