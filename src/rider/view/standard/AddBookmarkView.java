package rider.view.standard;

import rider.Localization;
import rider.controller.AddBookmarkController;
import rider.controller.IController;
import rider.utils.InternalLogger;
import rider.view.IView;
import rider.view.MIDletThreadSafe;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

/**
 * provides bookmark adding
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see AddBookmarkController
 */
public class AddBookmarkView extends Form implements IView {

    private MIDletThreadSafe _midlet = null;
    private IView _backdisplay = null;
    private IController _controller = null;
    private TextField _bookmarkTitle = new TextField(Localization.getInstance().strings().get("kTitle") + ":", "", 100, TextField.PLAIN);

    public AddBookmarkView(MIDletThreadSafe midlet, IView backDisplay) throws NullPointerException {
        super(Localization.getInstance().strings().get("kAddBookmark"));
        if (midlet == null || backDisplay == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("AddBookmarkView.AddBookmarkView"));
            //#endif
            throw new NullPointerException("AddBookmarkView.AddBookmarkView");
        }
        append(this._bookmarkTitle);

        this._midlet = midlet;
        this._backdisplay = backDisplay;
    }

    /**
     * @param controller behavior for this view must be not null
     * @throws NullPointerException
     */
    public void attachController(IController controller) throws NullPointerException {
        if (controller == null) {
            throw new NullPointerException("AddBookmarkView.attachController");
        }
        this._controller = controller;
    }

    public void setBookmarkTitle(String title) {
        this._bookmarkTitle.setString(title);
    }

    public String getBookmarkTitle() {
        return (_bookmarkTitle.getString());
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
