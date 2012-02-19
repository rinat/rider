package rider.view.standard;

import rider.controller.IController;
import rider.controller.RssItemsListController;
import rider.view.IView;
import rider.view.MIDletThreadSafe;
import rider.view.selection.SelectionState;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * provides RssItemsList view
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see RssItemsListController
 */
public class RssItemsListView extends List implements IView {

    // members
    private MIDletThreadSafe _midlet = null;
    private IView _backDisplay = null;
    private IController _controller = null;
    // members
    private SelectionState _selectionState = new SelectionState();

    public RssItemsListView(String title, MIDletThreadSafe midlet, IView backDisplay) throws NullPointerException {
        super(title, List.IMPLICIT);
        if (title == null || midlet == null) {
            throw new NullPointerException("RssItemsListView.RssItemsListView");
        }
        this._midlet = midlet;
        this._backDisplay = backDisplay;
    }

    /**
     * attach controller that provider logic for this view
     *
     * @param controller associated with current view
     */
    public void attachController(IController controller) {
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
