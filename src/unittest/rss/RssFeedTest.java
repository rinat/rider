package unittest.rss;

import j2meunit.framework.TestCase;
import rider.core.rss.feed.RssFeed;
import rider.core.rss.item.IRssItem;
import rider.core.rss.item.RssItem;


/**
 * Test for RssFeed method checking
 */
public class RssFeedTest extends TestCase {
    public RssFeedTest() {
    }

    public void runTest() {
        contructorTest();
        collectionTest();
    }

    /**
     * check RssFeed constructor, ling getter and setter
     */
    private void contructorTest() {
        RssFeed feed = new RssFeed("link.rss");
        this.assertEquals(feed.getLink(), "link.rss");
        feed.setLink("newlink.rss");
        this.assertEquals(feed.getLink(), "newlink.rss");
    }

    /**
     * check access to IRssItem collection inside RssFeed
     */
    private void collectionTest() {
        RssFeed feed = new RssFeed("link.rss");
        feed.getRssItems().addRssItem(new RssItem("title", "link", "description"));
        feed.getRssItems().addRssItem(new RssItem("title1", "link1", "description1"));
        feed.getRssItems().addRssItem(new RssItem("title2", "link2", "description2"));
        assertEquals(3, feed.getRssItems().getCount());

        checkEqualItemContent(feed.getRssItems().getRssItem(0),
                "title", "link", "description");
        checkEqualItemContent(feed.getRssItems().getRssItem(1),
                "title1", "link1", "description1");
        checkEqualItemContent(feed.getRssItems().getRssItem(2),
                "title2", "link2", "description2");
    }

    private void checkEqualItemContent(IRssItem item, String title, String link, String descr) {
        assertEquals(item.getTitle(), title);
        assertEquals(item.getLink(), link);
        assertEquals(item.getDescription(), descr);
    }
}
