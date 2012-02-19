package rider.view.standard;


import rider.Localization;
import rider.controller.BookmarksListController;
import rider.controller.IController;
import rider.utils.InternalLogger;
import rider.view.IView;
import rider.view.MIDletThreadSafe;
import rider.view.selection.SelectionState;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * provides bookmarks collection view
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see BookmarksListController
 */
public class BookmarksListView extends List implements IView {

    private MIDletThreadSafe _midlet = null;
    private IView _backDisplay = null;
    private IController _controller = null;
    private SelectionState _selectionState = new SelectionState();

    public BookmarksListView(MIDletThreadSafe midlet, IView backDisplay) throws NullPointerException {
        super(Localization.getInstance().strings().get("kBookmarks"), List.IMPLICIT);
        if (midlet == null) {
            throw new NullPointerException("BookmarksListView.BookmarksListView");
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
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("BookmarksListView.attachController"));
            //#endif
            throw new NullPointerException("BookmarksListView.attachController");
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
