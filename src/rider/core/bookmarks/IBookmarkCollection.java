/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rider.core.bookmarks;

/**
 * simple collection of bookmarks
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public interface IBookmarkCollection {

    int getCount();

    IBookmark getBookmark(int index);

    void addBookmark(IBookmark bookmark) throws NullPointerException;

    void removeBookmark(int index);

    void removeBookmark(IBookmark bookmark) throws NullPointerException;

    void removeAllBookmarks();
}
