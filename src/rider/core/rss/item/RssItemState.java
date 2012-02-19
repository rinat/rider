package rider.core.rss.item;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class RssItemState implements IRssItemState {

    private final Object _sync = new Object();
    private int _state = IRssItemState.kUnreadState;

    public void setState(int state) {
        synchronized (this._sync) {
            this._state = state;
        }
    }

    public int getState() {
        synchronized (this._sync) {
            return (this._state);
        }
    }
}
