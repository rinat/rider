package rider.view.standard;

import rider.controller.IController;
import rider.controller.RssFeedsListController;
import rider.utils.InternalLogger;
import rider.view.IView;
import rider.view.MIDletThreadSafe;
import rider.view.selection.SelectionState;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * provides rss list view
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see RssFeedsListController
 */
public class RssFeedsListView extends List implements IView {

    MIDletThreadSafe _midlet = null;
    IView _backDisplay = null;
    IController _controller = null;
    // members
    private SelectionState _selectionState = new SelectionState();

    public RssFeedsListView(String bookmarkTitle, MIDletThreadSafe midlet, IView backDisplay)
            throws NullPointerException {
        super(bookmarkTitle, List.IMPLICIT);
        if (bookmarkTitle == null || midlet == null || backDisplay == null) {
            InternalLogger.getInstance().fatal(new NullPointerException("RssFeedsListView.RssFeedsListView"));
            throw new NullPointerException();
        }
        this._midlet = midlet;
        this._backDisplay = backDisplay;
    }

    /**
     * @param controller behavior for this view must be not null
     * @throws NullPointerException
     */
    public void attachController(IController controller) throws NullPointerException {
        if (controller == null) {
            InternalLogger.getInstance().fatal(new NullPointerException("RssFeedsListView.attachController"));
            throw new NullPointerException();
        }
        this._controller = controller;
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
        int previousSelection = this._selectionState.getState();
        if (previousSelection < this.size() && previousSelection != -1) {
            this.setSelectedIndex(this._selectionState.getState(), true);
        }
    }

    public void saveSelection() {
        _selectionState.setState(this.getSelectedIndex());
    }
}
