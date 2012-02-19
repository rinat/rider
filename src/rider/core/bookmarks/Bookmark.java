package rider.core.bookmarks;

import rider.core.rss.feed.IRssFeed;
import rider.core.rss.feed.IRssFeedCollection;
import rider.core.rss.feed.RssFeedCollection;
import rider.utils.InternalLogger;

/**
 * Bookmark contains collection of IRssFeed
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see IRssFeed
 */
public class Bookmark implements IBookmark {

    private String _title;
    private IRssFeedCollection _feedCollection = new RssFeedCollection();

    /**
     * @param title must be not null
     * @throws NullPointerException
     */
    public Bookmark(String title) throws NullPointerException {
        if (title == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("Bookmark.Bookmark"));
            //#endif
            throw new NullPointerException();
        }
        this.setTitle(title);
    }

    /**
     * @see IRssFeedCollection
     */
    public IRssFeedCollection getRssFeeds() {
        return (_feedCollection);
    }

    /**
     * set bookmark title
     *
     * @param title must be not null
     * @throws NullPointerException
     */
    public void setTitle(String title) throws NullPointerException {
        if (title == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("Bookmark.setTitle"));
            //#endif
            throw new NullPointerException();
        }
        this._title = title;
    }

    public String getTitle() {
        return (this._title);
    }

    /**
     * adding IRssFeed to bookmark
     *
     * @param rssFeed
     * @throws NullPointerException
     * @see IRssFeed
     */
    public void addRssFeed(IRssFeed rssFeed) throws NullPointerException {
        if (rssFeed == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("Bookmark.addRssFeed"));
            //#endif
            throw new NullPointerException();
        }
        this._feedCollection.addRssFeed(rssFeed);
    }

    /**
     * @return count of IRssFeed contains on bookmark
     */
    public int getCount() {
        return (this._feedCollection.getCount());
    }

    public IRssFeed getRssFeed(int index) {
        return (this._feedCollection.getRssFeed(index));
    }

    public void removeRssFeed(int feedIndex) {
        this._feedCollection.removeRssFeed(feedIndex);
    }
}
