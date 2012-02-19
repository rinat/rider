/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rider.controller;

/**
 * Listener of FolderManagerController actions
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see FolderManagerController
 */
public interface IFolderManagerListerner {

    void onFolderSelected(String folder);

    void onFolderManagerError(Exception e);
}
