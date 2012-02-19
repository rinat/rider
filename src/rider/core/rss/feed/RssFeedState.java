package rider.core.rss.feed;

public class RssFeedState implements IRssFeedState {

    private final Object _sync = new Object();
    private int _state = IRssFeedState.kNotLoadedState;

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
