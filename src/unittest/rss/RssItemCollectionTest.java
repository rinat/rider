package unittest.rss;

import j2meunit.framework.TestCase;
import rider.core.rss.item.IRssItem;
import rider.core.rss.item.RssItem;
import rider.core.rss.item.RssItemCollection;

/**
 * Test for RssFeedCollection method checking
 */
public class RssItemCollectionTest extends TestCase {
    public RssItemCollectionTest() {
    }

    public void runTest() {
        construtorTest();
        addAndGetItemsTest();
    }

    private void construtorTest() {
        try {
            RssItemCollection collection = new RssItemCollection();
            this.assertNotNull(collection);
            return;
        }
        catch (Exception e) {
            this.assertTrue(false);
        }
        this.assertTrue(false);
    }

    private void addAndGetItemsTest() {
        RssItemCollection collection = new RssItemCollection();
        collection.addRssItem(new RssItem("title", "link", "description"));
        collection.addRssItem(new RssItem("title1", "link1", "description1"));
        collection.addRssItem(new RssItem("title2", "link2", "description2"));

        this.assertEquals(3, collection.getCount());

        checkEqualItemContent(collection.getRssItem(0), "title", "link", "description");
        checkEqualItemContent(collection.getRssItem(1), "title1", "link1", "description1");
        checkEqualItemContent(collection.getRssItem(2), "title2", "link2", "description2");

        this.assertEquals(3, collection.getCount());
        collection.removeAllRssItems();
        this.assertEquals(0, collection.getCount());
    }

    private void checkEqualItemContent(IRssItem item, String title, String link, String description) {
        this.assertEquals(item.getTitle(), title);
        this.assertEquals(item.getLink(), link);
        this.assertEquals(item.getDescription(), description);
    }
}
