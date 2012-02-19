/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rider.controller;

/**
 * Listener of FileManagerController actions
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see FileManagerController
 */
public interface IFileManagerListener {

    public void onFileSelected(String filename);

    public void onFileManagerError(Exception e);
}
