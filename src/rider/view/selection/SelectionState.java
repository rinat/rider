package rider.view.selection;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class SelectionState {

    private final Object _syncObject = new Object();
    private int _selectionState = 0;

    public SelectionState() {
        this._selectionState = 0;
    }

    public void setState(int selectionState) {
        synchronized (_syncObject) {
            this._selectionState = selectionState;
        }
    }

    public int getState() {
        synchronized (_syncObject) {
            return (this._selectionState);
        }
    }
}
