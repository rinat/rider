package unittest.bookmarks;

import j2meunit.framework.TestCase;
import rider.core.bookmarks.Bookmark;
import rider.core.rss.feed.IRssFeed;
import rider.core.rss.feed.RssFeed;

/**
 * Created by IntelliJ IDEA.
 * User: Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * Date: 18.05.2010
 * Time: 20:58:05
 * To change this template use File | Settings | File Templates.
 */
public class BookmarkTest extends TestCase {
    public BookmarkTest() {
    }

    public void runTest() {
        exceptionConstructorTest();
        constructorTest();
        setTitleException();
        setTitleOk();
        addFeedException();
        addFeedOk();
    }

    private void exceptionConstructorTest() {
        try {
            Bookmark bookmark = new Bookmark(null);
        }
        catch (NullPointerException e) {
            this.assertTrue(true);
            return;
        }
        this.assertTrue(false);
    }

    private void constructorTest() {
        Bookmark bookmark = new Bookmark("Bookmark");
        this.assertNotNull(bookmark);
        this.assertEquals("Bookmark", bookmark.getTitle());
        this.assertEquals(bookmark.getCount(), 0);
    }

    private void setTitleException() {
        Bookmark bookmark = new Bookmark("bookmark");
        try {
            bookmark.setTitle(null);
        }
        catch (NullPointerException e) {
            this.assertTrue(true);
            return;
        }
        this.assertTrue(false);
    }

    private void setTitleOk() {
        Bookmark bookmark = new Bookmark("bookmark");
        this.assertEquals("bookmark", bookmark.getTitle());
        bookmark.setTitle("newBookmark");
        this.assertEquals("newBookmark", bookmark.getTitle());
        this.assertEquals(bookmark.getCount(), 0);
    }

    private void addFeedException() {
        Bookmark bookmark = new Bookmark("bookmark");
        try {
            bookmark.addRssFeed(null);
        }
        catch (NullPointerException e) {
            this.assertTrue(true);
            return;
        }
        this.assertTrue(false);
    }

    private void addFeedOk() {
        Bookmark bookmark = new Bookmark("bookmark");
        bookmark.addRssFeed(new RssFeed("feed.xml"));
        this.assertEquals(bookmark.getCount(), 1);

        IRssFeed feed = bookmark.getRssFeed(0);
        this.assertNotNull(feed);
        this.assertEquals(feed.getLink(), "feed.xml");
    }
}
