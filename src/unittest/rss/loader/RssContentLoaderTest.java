package unittest.rss.loader;

import j2meunit.framework.TestCase;
import rider.core.rss.feed.IRssFeed;
import rider.core.rss.feed.RssFeed;
import rider.core.rss.loader.IRssContentListener;
import rider.core.rss.loader.RssContentLoader;

public class RssContentLoaderTest extends TestCase {

    class ContentListener implements IRssContentListener {

        private boolean _isErrorExpected = false;
        private String _expectedContent = "";

        public ContentListener(boolean isErrorExpected, String expectedContent) {
            this._isErrorExpected = isErrorExpected;
            this._expectedContent = expectedContent;
        }

        public void onContentLoaded(IRssFeed rssFeed, String content) {
            assertTrue(_isErrorExpected);
            assertEquals(this._expectedContent, content);
        }

        public void onContentLoadError(IRssFeed rssFeed, Exception exception) {
            assertTrue(_isErrorExpected);
        }
    }

    public RssContentLoaderTest() {
    }

    public void runTest() {
        constructorExceptionTest();
        loadLinkTest("http://test1.ru/RssContentLoaderTest_simpleContent.txt", "RssContentLoaderTest", false);
        loadLinkTest("http://test1.ru/RssContentLoaderTest_emptyContent.txt", "", false);
        loadLinkTest("http://test1.ru/RssContentLoaderTest_emptyContent_notExists.txt", "", true);
    }

    private void constructorExceptionTest() {
        try {
            RssFeed feed = new RssFeed("http://test1.ru/RssContentLoaderTest_simpleContent.txt");
            RssContentLoader loader = new RssContentLoader(null, null);
        }
        catch (NullPointerException e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
    }

    private void loadLinkTest(String link, String expectedContent, boolean expectedError) {
        try {
            RssFeed feed = new RssFeed(link);
            RssContentLoader loader = new RssContentLoader(feed,
                    new ContentListener(expectedError, expectedContent));
            loader.start();
        }
        catch (NullPointerException e) {
            assertTrue(false);
            return;
        }
        assertTrue(true);
    }
}
