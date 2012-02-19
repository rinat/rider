package rider.view.standard;


import rider.Localization;
import rider.controller.IController;
import rider.controller.MainController;
import rider.utils.InternalLogger;
import rider.view.IView;
import rider.view.MIDletThreadSafe;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

/**
 * main Rider view
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see MainController
 */
public class MainView extends Form implements IView {

    private IController _controller = null;
    private MIDletThreadSafe _midlet = null;

    private TextField _rssField =
            new TextField(Localization.getInstance().strings().get("kFeed"), Localization.getInstance().strings().get("kStartRssFeed"), 100, TextField.URL);

    public MainView(MIDletThreadSafe midlet) throws NullPointerException {
        super(Localization.getInstance().strings().get("kRider"));
        if (midlet == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("MIDletThreadSafe.MIDletThreadSafe"));
            //#endif
            throw new NullPointerException("MIDletThreadSafe.MIDletThreadSafe");
        }
        this._midlet = midlet;
        append(this._rssField);
    }

    /**
     * @see IView
     */
    public IView getBackDisplay() {
        return (this);
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
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("MainView.attachController"));
            //#endif
            throw new NullPointerException();
        }
        this._controller = controller;
    }

    public String getRssFieldContent() {
        return (this._rssField.getString());
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
