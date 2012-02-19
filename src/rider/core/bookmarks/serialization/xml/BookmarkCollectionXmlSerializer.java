package rider.core.bookmarks.serialization.xml;

import rider.core.bookmarks.IBookmark;
import rider.core.bookmarks.IBookmarkCollection;
import rider.core.bookmarks.serialization.IBookmarkCollectionSerializer;
import rider.core.rss.feed.IRssFeed;

/**
 * serialize IBookmarkCollection to xml
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see IBookmarkCollectionSerializer
 */
public class BookmarkCollectionXmlSerializer implements IBookmarkCollectionSerializer {

    public static final String kBookmark = "Bookmark";
    public static final String kBookmarks = "Bookmarks";
    public static final String kFeeds = "Feeds";
    public static final String kFeed = "Feed";
    public static final String kTitle = "Title";

    /**
     * @param bookmarks must be not null
     * @return IBookmarkCollection string presentation
     * @throws NullPointerException
     */
    public String serialize(IBookmarkCollection bookmarks) throws NullPointerException {
        if (bookmarks == null) {
            throw new NullPointerException("BookmarkCollectionXmlSerializer.serialize");
        }
        StringBuffer result = new StringBuffer();
        appendContentsTo(bookmarks, result);
        return (result.toString());
    }

    /**
     * serialize each bookmark from IBookmarkCollection to xml
     *
     * @param bookmarks
     * @param result
     * @see IBookmarkCollection
     */
    private void appendContentsTo(IBookmarkCollection bookmarks, StringBuffer result) {
        writeOpenTagTo(kBookmarks, result);
        for (int boolmarkIndex = 0; boolmarkIndex < bookmarks.getCount(); ++boolmarkIndex) {
            IBookmark bookmark = bookmarks.getBookmark(boolmarkIndex);
            appendContentsTo(bookmark, result);
        }
        writeEndTagTo(kBookmarks, result);
    }

    /**
     * serialize IBookmark to xml
     *
     * @param bookmark
     * @param result
     * @see IBookmark
     */
    private void appendContentsTo(IBookmark bookmark, StringBuffer result) {
        writeOpenTagTo(kBookmark, result);
        writeOpenTagTo(kTitle, result);
        writeTextTo(bookmark.getTitle(), result);
        writeEndTagTo(kTitle, result);
        writeChildrentTo(bookmark, result);
        writeEndTagTo(kBookmark, result);
    }

    /**
     * serialize each IRssFeed that contains on IBookmark to xml
     *
     * @param bookmark must be not null
     * @param result   must be not null
     * @see IRssFeed
     * @see IBookmark
     */
    private void writeChildrentTo(IBookmark bookmark, StringBuffer result) {
        writeOpenTagTo(kFeeds, result);
        for (int itemIndex = 0; itemIndex < bookmark.getCount(); ++itemIndex) {
            IRssFeed rssFeed = bookmark.getRssFeed(itemIndex);
            appendContentsTo(rssFeed, result);
        }
        writeEndTagTo(kFeeds, result);
    }

    /**
     * simple append text to StringBuffer
     *
     * @param text   must be not null
     * @param result must be not null
     */
    private void writeTextTo(String text, StringBuffer result) {
        result.append(text);
    }

    /**
     * append rssFeed to as xml to StringBuffer
     *
     * @param rssFeed must be not null
     * @param result  must be not null
     * @see IRssFeed
     */
    private void appendContentsTo(IRssFeed rssFeed, StringBuffer result) {
        writeOpenTagTo("Feed", result);
        result.append(rssFeed.getLink());
        writeEndTagTo("Feed", result);
    }

    /**
     * @param name   of open xml tag
     * @param result
     */
    private void writeOpenTagTo(String name, StringBuffer result) {
        result.append("<");
        result.append(name);
        result.append(">");
    }

    /**
     * @param name   of close xml tag
     * @param result
     */
    private void writeEndTagTo(String name, StringBuffer result) {
        result.append("</");
        result.append(name);
        result.append(">");
    }
}
