package rider.core.bookmarks;

import rider.utils.InternalLogger;

import java.util.Enumeration;
import java.util.Vector;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class BookmarkCollection implements IBookmarkCollection, Enumeration {

    private Vector _bookmarksCollection = new Vector();

    public BookmarkCollection() {
    }

    public Enumeration elements() {
        return (this._bookmarksCollection.elements());
    }

    public boolean hasMoreElements() {
        return (this._bookmarksCollection.elements().hasMoreElements());
    }

    public Object nextElement() {
        return (this._bookmarksCollection.elements().nextElement());
    }

    public void addBookmark(IBookmark bookmark) throws NullPointerException {
        if (bookmark == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("BookmarkCollection.addBookmark"));
            //#endif
            throw new NullPointerException();
        }
        _bookmarksCollection.addElement(bookmark);
    }

    public void removeAllBookmarks() {
        this._bookmarksCollection.removeAllElements();
    }

    public IBookmark getBookmark(int index) {
        return ((IBookmark) this._bookmarksCollection.elementAt(index));
    }

    public int getCount() {
        return (this._bookmarksCollection.size());
    }

    public void removeBookmark(int index) {
        this._bookmarksCollection.removeElementAt(index);
    }

    public void removeBookmark(IBookmark bookmark) throws NullPointerException {
        if (bookmark == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("BookmarkCollection.removeBookmark"));
            //#endif
            throw new NullPointerException();
        }
        this._bookmarksCollection.removeElement(bookmark);
    }

}
