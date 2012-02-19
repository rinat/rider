/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rider.core.bookmarks.serialization;

import rider.core.bookmarks.IBookmarkCollection;

import java.io.UnsupportedEncodingException;

/**
 * deserialize IBookmarkCollection from string presentation
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public interface IBookmarkCollectionDeserializer {

    /**
     * deserialize IBookmarkCollection from string presentation
     *
     * @param content
     * @return IBookmarkCollection not null
     * @throws NullPointerException
     * @throws UnsupportedEncodingException
     * @see IBookmarkCollection
     */
    IBookmarkCollection deserialize(String content) throws NullPointerException, UnsupportedEncodingException;
}
