package unittest.rss;

import j2meunit.framework.TestCase;
import rider.core.rss.item.RssItem;

/**
 * Test for RssItem method checking
 */
public class RssItemTest extends TestCase {
    public RssItemTest() {
    }

    public void runTest() {
        testRssItemConstructor();
        testRssItemConstructorWithParams();
        testRssItemFieldInitializer();
    }

    private void testRssItemConstructor() {
        RssItem rssItem = new RssItem("", "", "");
        this.assertEquals(rssItem.getTitle(), "");
        this.assertEquals(rssItem.getDescription(), "");
        this.assertEquals(rssItem.getLink(), "");
    }

    private void testRssItemConstructorWithParams() {
        RssItem rssItem = new RssItem("Nvidia", "http://www.nvidia.ru", "Chip");

        this.assertEquals(rssItem.getTitle(), "Nvidia");
        this.assertEquals(rssItem.getDescription(), "Chip");
        this.assertEquals(rssItem.getLink(), "http://www.nvidia.ru");
    }

    private void testRssItemFieldInitializer() {
        RssItem rssItem = new RssItem("Ferra", "http://www.ferra.ru", "New site");
        this.assertEquals(rssItem.getTitle(), "Ferra");
        this.assertEquals(rssItem.getDescription(), "New site");
        this.assertEquals(rssItem.getLink(), "http://www.ferra.ru");
    }

}
