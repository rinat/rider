/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rider.core.browser;

import rider.view.MIDletThreadSafe;

import javax.microedition.io.ConnectionNotFoundException;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public interface IBrowser {

    /**
     * provider internet browser interface
     *
     * @param link   must be not null
     * @param midlet must be not null
     * @throws NullPointerException
     * @throws ConnectionNotFoundException
     */
    void Open(String link, MIDletThreadSafe midlet)
            throws NullPointerException, ConnectionNotFoundException;
}
