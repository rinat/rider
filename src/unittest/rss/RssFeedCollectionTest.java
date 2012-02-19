package unittest.rss;

import j2meunit.framework.TestCase;
import rider.core.rss.feed.IRssFeed;
import rider.core.rss.feed.RssFeed;
import rider.core.rss.feed.RssFeedCollection;

/**
 * Test for RssFeedCollection method checking
 */
public class RssFeedCollectionTest extends TestCase {
    public RssFeedCollectionTest() {
    }

    public void runTest() {
        construtorTest();
        addAndGetItemsTest();
    }

    private void construtorTest() {
        try {
            RssFeedCollection collection = new RssFeedCollection();
            this.assertNotNull(collection);
            return;
        }
        catch (Exception e) {
            this.assertTrue(false);
        }
        this.assertTrue(false);
    }

    private void addAndGetItemsTest() {
        RssFeedCollection collection = new RssFeedCollection();
        collection.addRssFeed(new RssFeed("feed.rss"));
        collection.addRssFeed(new RssFeed("feed1.rss"));
        collection.addRssFeed(new RssFeed("feed2.rss"));

        this.assertEquals(3, collection.getCount());

        checkEqualItemContent(collection.getRssFeed(0), "feed.rss");
        checkEqualItemContent(collection.getRssFeed(1), "feed1.rss");
        checkEqualItemContent(collection.getRssFeed(2), "feed2.rss");

        collection.removeAllFeeds();
        this.assertEquals(0, collection.getCount());
    }

    private void checkEqualItemContent(IRssFeed item, String link) {
        assertEquals(item.getLink(), link);
    }
}
