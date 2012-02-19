package unittest.bookmarks;

import j2meunit.framework.TestCase;
import rider.core.bookmarks.IBookmark;
import rider.core.bookmarks.IBookmarkCollection;
import rider.core.bookmarks.serialization.xml.BookmarkCollectionXmlDeserializer;
import rider.core.bookmarks.serialization.xml.BookmarkCollectionXmlSerializer;
import rider.core.rss.feed.IRssFeed;

import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * Date: 19.05.2010
 * Time: 0:39:49
 * To change this template use File | Settings | File Templates.
 */
public class BookmarkImportExportTest extends TestCase {

    public BookmarkImportExportTest() {
    }

    public void runTest() {
        importBookmarksTest();
        exportBookmarksTest();
    }

    private void exportBookmarksTest() {
        BookmarkCollectionXmlDeserializer deserializer = new BookmarkCollectionXmlDeserializer();
        try {
            IBookmarkCollection bookmarks = deserializer.deserialize(createBookmarks());
            this.assertNotNull(bookmarks);
            this.assertEquals(bookmarks.getCount(), 4);

            BookmarkCollectionXmlSerializer serializer = new BookmarkCollectionXmlSerializer();
            String bookmarksXml = serializer.serialize(bookmarks);

            String needXml = removeAllBlanksFrom(createBookmarks());
            this.assertEquals(bookmarksXml, needXml);

            this.assertTrue(true);
        } catch (UnsupportedEncodingException e) {
            this.assertTrue(false);
        }
    }

    private void importBookmarksTest() {
        BookmarkCollectionXmlDeserializer deserializer = new BookmarkCollectionXmlDeserializer();
        try {
            IBookmarkCollection bookmarks = deserializer.deserialize(createBookmarks());
            this.assertNotNull(bookmarks);
            this.assertEquals(bookmarks.getCount(), 4);

            checkBookmarkTitle(bookmarks.getBookmark(0), "BookmarkFirst");
            checkFeed(bookmarks.getBookmark(0).getRssFeed(0), "http://images.apple.com/main/rss/hotnews/hotnews.rss");
            checkFeed(bookmarks.getBookmark(0).getRssFeed(1), "http://images.apple.com/main/rss/hotnews/hotnews.rss");

            checkBookmarkTitle(bookmarks.getBookmark(1), "BookmarkSecond");
            checkFeed(bookmarks.getBookmark(1).getRssFeed(0), "http://images.apple.com/main/rss/hotnews/hotnews.rss");
            checkFeed(bookmarks.getBookmark(1).getRssFeed(1), "http://images.apple.com/main/rss/hotnews/hotnews.rss");

            checkBookmarkTitle(bookmarks.getBookmark(2), "BookmarkThird");
            checkFeed(bookmarks.getBookmark(2).getRssFeed(0), "http://images.apple.com/main/rss/hotnews/hotnews.rss");
            checkFeed(bookmarks.getBookmark(2).getRssFeed(1), "http://images.apple.com/main/rss/hotnews/hotnews.rss");

            checkBookmarkTitle(bookmarks.getBookmark(3), "BookmarkFourth");

            this.assertTrue(true);
        } catch (UnsupportedEncodingException e) {
            this.assertTrue(false);
        }
    }

    private void checkFeed(IRssFeed feed, String link) {
        this.assertEquals(feed.getLink(), link);
    }

    private void checkBookmarkTitle(IBookmark bookmark, String title) {
        this.assertEquals(bookmark.getTitle(), title);
    }

    private String removeAllBlanksFrom(String xml) {
        String result = "";
        for (int charIndex = 0; charIndex < xml.length(); ++charIndex) {
            if (xml.charAt(charIndex) != ' ') {
                result += xml.charAt(charIndex);
            }
        }
        return (result);
    }

    private String createBookmarks() {
        StringBuffer bookmarks = new StringBuffer();

        bookmarks.append("<Bookmarks>");
        bookmarks.append("    <Bookmark>");
        bookmarks.append("        <Title>BookmarkFirst</Title>");
        bookmarks.append("        <Feeds>");
        bookmarks.append("            <Feed>http://images.apple.com/main/rss/hotnews/hotnews.rss</Feed>");
        bookmarks.append("            <Feed>http://images.apple.com/main/rss/hotnews/hotnews.rss</Feed>");
        bookmarks.append("        </Feeds>");
        bookmarks.append("    </Bookmark>");
        bookmarks.append("    <Bookmark>");
        bookmarks.append("        <Title>BookmarkSecond</Title>");
        bookmarks.append("        <Feeds>");
        bookmarks.append("            <Feed>http://images.apple.com/main/rss/hotnews/hotnews.rss</Feed>");
        bookmarks.append("            <Feed>http://images.apple.com/main/rss/hotnews/hotnews.rss</Feed>");
        bookmarks.append("        </Feeds>");
        bookmarks.append("    </Bookmark>");
        bookmarks.append("    <Bookmark>");
        bookmarks.append("        <Title>BookmarkThird</Title>");
        bookmarks.append("        <Feeds>");
        bookmarks.append("            <Feed>http://images.apple.com/main/rss/hotnews/hotnews.rss</Feed>");
        bookmarks.append("            <Feed>http://images.apple.com/main/rss/hotnews/hotnews.rss</Feed>");
        bookmarks.append("        </Feeds>");
        bookmarks.append("    </Bookmark>");
        bookmarks.append("    <Bookmark>");
        bookmarks.append("        <Title>BookmarkFourth</Title>");
        bookmarks.append("        <Feeds>");
        bookmarks.append("            <Feed>http://images.apple.com/main/rss/hotnews/hotnews.rss</Feed>");
        bookmarks.append("            <Feed>http://images.apple.com/main/rss/hotnews/hotnews.rss</Feed>");
        bookmarks.append("        </Feeds>");
        bookmarks.append("    </Bookmark>");
        bookmarks.append("</Bookmarks>");

        return (bookmarks.toString());
    }
}
