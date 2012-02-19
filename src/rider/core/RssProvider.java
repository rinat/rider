package rider.core;

import rider.core.bookmarks.BookmarkCollection;
import rider.core.bookmarks.IBookmark;
import rider.core.bookmarks.IBookmarkCollection;
import rider.utils.InternalLogger;

import javax.microedition.rms.RecordStoreException;

/**
 * Rss Core main class for provides rss core settings loading and access to bookmark collection
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class RssProvider {

    private IBookmarkCollection _bookmarkCollection = new BookmarkCollection();
    private RssCoreSettings _rssCoreSettings = null;

    public RssProvider() {
        try {
            _rssCoreSettings = new RssCoreSettings();
        } catch (RecordStoreException e) {
            _rssCoreSettings = null;
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(e);
            //#endif
        }
    }

    public RssCoreSettings getSettings() {
        return (this._rssCoreSettings);
    }

    public void importBookmarks(IBookmarkCollection bookmarks) {
        this._bookmarkCollection.removeAllBookmarks();
        for (int bookmarkIndex = 0; bookmarkIndex < bookmarks.getCount(); ++bookmarkIndex) {
            _bookmarkCollection.addBookmark(bookmarks.getBookmark(bookmarkIndex));
        }
    }

    public int getBookmarksCount() {
        return (this._bookmarkCollection.getCount());
    }

    public IBookmark getBookmark(int index) {
        return (this._bookmarkCollection.getBookmark(index));
    }

    public IBookmarkCollection getBookmarks() {
        return (this._bookmarkCollection);
    }

    private boolean existsBookmarkByName(String bookmarkName, IBookmarkCollection bookmarks) {
        for (int elementIndex = 0; elementIndex < bookmarks.getCount(); ++elementIndex) {
            IBookmark bookmark = bookmarks.getBookmark(elementIndex);
            if (bookmark.getTitle().equals(bookmarkName)) {
                return (true);
            }
        }
        return (false);
    }
}