package rider.view.standard;

import rider.Localization;
import rider.controller.AddFeedController;
import rider.controller.IController;
import rider.utils.InternalLogger;
import rider.view.IView;
import rider.view.MIDletThreadSafe;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

/**
 * provides feed adding
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see AddFeedController
 */
public class AddFeedView extends Form implements IView {

    private MIDletThreadSafe _midlet = null;
    private IView _backdisplay = null;
    private IController _controller = null;
    private TextField _feedTitle = new TextField(Localization.getInstance().strings().get("kFeed") + ":", "", 100, TextField.HYPERLINK);

    public AddFeedView(MIDletThreadSafe midlet, IView backDisplay) throws NullPointerException {
        super(Localization.getInstance().strings().get("kAddFeed"));
        if (midlet == null || backDisplay == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("AddFeedView.AddFeedView"));
            //#endif
            throw new NullPointerException("AddFeedView.AddFeedView");
        }
        append(this._feedTitle);

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
            InternalLogger.getInstance().fatal(new NullPointerException("AddFeedView.attachController"));
            //#endif
            throw new NullPointerException("AddFeedView.attachController");
        }
        this._controller = controller;
    }

    public void setFeedTitle(String title) {
        this._feedTitle.setString(title);
    }

    public String getFeedTitle() {
        return (_feedTitle.getString());
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
    public Displayable asDisplayable() {
        return (this);
    }

    /**
     * @see IView
     */
    public void updateView() {
        _controller.updateView();
    }

    public void rollbackSelection() {
    }

    public void saveSelection() {
    }
}
