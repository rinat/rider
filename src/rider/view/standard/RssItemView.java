package rider.view.standard;

import rider.controller.IController;
import rider.core.rss.item.IRssItem;
import rider.view.IView;
import rider.view.MIDletThreadSafe;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 * View for IRssItem
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see IRssItem
 */
public class RssItemView extends Form implements IView {

    private IController _controller = null;
    private MIDletThreadSafe _midlet = null;
    private IView _backdisplay = null;

    public RssItemView(MIDletThreadSafe midlet, IView backdisplay) {
        super("");
        if (midlet == null) {
            throw new NullPointerException("RssItemView.RssItemView");
        }
        this._midlet = midlet;
        this._backdisplay = backdisplay;
    }

    public void attachController(IController controller) {
        if (controller == null) {
            throw new NullPointerException("RssItemView.attachController");
        }
        _controller = controller;
    }

    public IView getBackDisplay() {
        return (this._backdisplay);
    }

    public void updateView() {
    }

    public MIDletThreadSafe getMIDlet() {
        return (this._midlet);
    }

    public Displayable asDisplayable() {
        return (this);
    }

    public void rollbackSelection() {
    }

    public void saveSelection() {
    }
}
