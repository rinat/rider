package rider.core.browser;

import rider.utils.InternalLogger;
import rider.view.MIDletThreadSafe;

import javax.microedition.io.ConnectionNotFoundException;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see IBrowser
 */
public class DefaultBrowser implements IBrowser {

    public DefaultBrowser() {
    }

    /**
     * current platform browser invoker
     *
     * @param link   must be not null
     * @param midlet must be not null
     * @throws NullPointerException
     * @throws ConnectionNotFoundException
     */
    public void Open(String link, MIDletThreadSafe midlet)
            throws NullPointerException, ConnectionNotFoundException {
        if (link == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("DefaultBrowser.Open"));
            //#endif
            throw new NullPointerException("DefaultBrowser.Open");
        }
        midlet.platformRequest(link);
    }
}
