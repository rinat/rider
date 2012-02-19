package rider.view;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

/**
 * This class is thread safe wrapper over Midlet
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class MIDletThreadSafe {

    private MIDlet _midlet = null;
    private DisplayThreadSafe _displayWrapper = null;
    private final Object _syncronizationObject = new Object();

    /**
     * MIDletThreadSafe constructor
     *
     * @param midlet must be not null
     * @throws NullPointerException
     */
    public MIDletThreadSafe(MIDlet midlet) throws NullPointerException {
        if (midlet == null) {
            throw new NullPointerException("MIDletThreadSafe.MIDletThreadSafe");
        }
        this._midlet = midlet;
        this._displayWrapper = new DisplayThreadSafe(Display.getDisplay(midlet));
    }

    /**
     * getter for DisplayThreadSafe
     *
     * @return thread safe wrapper over Display
     * @see DisplayThreadSafe
     */
    public DisplayThreadSafe getDisplay() {
        return (this._displayWrapper);
    }

    /**
     * notify MIDlet about Destroyed
     * wrapper over MIDlet.notifyDestroyed function
     */
    public void notifyDestroyed() {
        synchronized (_syncronizationObject) {
            this._midlet.notifyDestroyed();
        }
    }

    /**
     * Invoking Platform Services
     * wrapper over MIDlet.platformRequest function
     *
     * @param request
     * @throws javax.microedition.io.ConnectionNotFoundException
     *
     */
    public void platformRequest(String request) throws ConnectionNotFoundException {
        synchronized (_syncronizationObject) {
            this._midlet.platformRequest(request);
        }
    }
}
