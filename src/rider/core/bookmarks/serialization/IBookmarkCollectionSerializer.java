/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rider.core.bookmarks.serialization;

import rider.core.bookmarks.IBookmarkCollection;

/**
 * serialize IBookmarkCollection to string representation
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public interface IBookmarkCollectionSerializer {

    /**
     * @param bookmarks must be not null
     * @return IBookmarkCollection string presentation
     * @throws NullPointerException
     * @see IBookmarkCollection
     */
    String serialize(IBookmarkCollection bookmarks) throws NullPointerException;
}
