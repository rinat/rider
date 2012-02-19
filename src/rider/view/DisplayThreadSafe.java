package rider.view;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;

/**
 * Thread safe wrapper of Display
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class DisplayThreadSafe {

    Display _display = null;
    private final Object _syncronizationObject = new Object();

    /**
     * @param display must be not null
     * @throws NullPointerException
     */
    public DisplayThreadSafe(Display display) throws NullPointerException {
        if (display == null) {
            throw new NullPointerException("DisplayThreadSafe.DisplayThreadSafe");
        }
        this._display = display;
    }

    /**
     * thread safe wrapper over Display.setCurrent function
     *
     * @param displayable
     * @throws NullPointerException
     */
    public void setCurrent(Displayable displayable) throws NullPointerException {
        synchronized (_syncronizationObject) {
            this._display.setCurrent(displayable);
        }
    }

    /**
     * thread safe wrapper over Display.setCurrentItem function
     *
     * @param item
     */
    public void setCurrentItem(Item item) {
        synchronized (_syncronizationObject) {
            this._display.setCurrentItem(item);
        }
    }
}
