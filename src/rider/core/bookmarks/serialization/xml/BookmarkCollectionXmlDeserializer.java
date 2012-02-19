package rider.core.bookmarks.serialization.xml;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import rider.core.bookmarks.Bookmark;
import rider.core.bookmarks.BookmarkCollection;
import rider.core.bookmarks.IBookmark;
import rider.core.bookmarks.IBookmarkCollection;
import rider.core.bookmarks.serialization.IBookmarkCollectionDeserializer;
import rider.core.rss.feed.RssFeed;
import rider.utils.InternalLogger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * serialize IBookmarkCollection from xml
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see IBookmarkCollectionDeserializer
 */
public class BookmarkCollectionXmlDeserializer implements IBookmarkCollectionDeserializer {

    public BookmarkCollectionXmlDeserializer() {
    }

    /**
     * deserialize IBookmarkCollection from xml
     *
     * @param content
     * @return IBookmarkCollection not null
     * @throws NullPointerException
     * @throws UnsupportedEncodingException
     */
    public IBookmarkCollection deserialize(String content) throws NullPointerException, UnsupportedEncodingException {
        try {
            if (content == null) {
                //#ifdef rider.debugEnabled
                InternalLogger.getInstance().fatal(new NullPointerException("BookmarkCollectionXmlDeserializer.deserialize"));
                //#endif
                throw new NullPointerException("BookmarkCollectionXmlDeserializer.deserialize");
            }
            KXmlParser parser = new KXmlParser();
            parser.setInput(convertStringTo(content));
            return (parseBookmarkCollection(parser));

        } catch (XmlPullParserException ex) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(ex);
            //#endif
            throw new UnsupportedEncodingException();
        }
    }

    /**
     * convert String to InputStreamReader
     *
     * @param content must be not null
     * @return InputStreamReader crated from String
     */
    private InputStreamReader convertStringTo(String content) {
        byte[] xmlByteArray = content.getBytes();
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xmlByteArray);
        return (new InputStreamReader(xmlStream));
    }

    /**
     * parse string and create BookmarkCollection
     *
     * @param parser must be not null
     * @return IBookmarkCollection parsed by KXmlParser parser
     * @throws UnsupportedEncodingException
     * @see IBookmarkCollection
     */
    private IBookmarkCollection parseBookmarkCollection(KXmlParser parser) throws UnsupportedEncodingException {
        BookmarkCollection bookmarks = new BookmarkCollection();

        try {
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, BookmarkCollectionXmlSerializer.kBookmarks);
            parser.nextTag();

            while (parser.getEventType() != XmlPullParser.END_TAG) {
                String nodeName = parser.getName();
                if (nodeName.equals(BookmarkCollectionXmlSerializer.kBookmark)) {
                    bookmarks.addBookmark(parseBookmark(parser));
                } else {
                    parser.skipSubTree();
                }
                parser.nextTag();
            }

        } catch (XmlPullParserException ex) {
            throw new UnsupportedEncodingException();
        } catch (IOException ex) {
            throw new UnsupportedEncodingException();
        }
        return bookmarks;
    }

    /**
     * parse string and create IBookmark
     *
     * @param parser
     * @return IBookmark by KXmlParser parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    private IBookmark parseBookmark(KXmlParser parser) throws IOException, XmlPullParserException {
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, BookmarkCollectionXmlSerializer.kTitle);

        Bookmark bookmark = new Bookmark(parser.nextText());
        bookmark = (Bookmark) parseBookmarkContent(parser, bookmark);
        parser.nextTag();

        return (bookmark);
    }

    /**
     * parse rss feeds and append it to Bookmark
     *
     * @param parser
     * @param bookmark
     * @return IBookmark parsed from string
     * @throws XmlPullParserException
     * @throws IOException
     * @see IBookmark
     */
    private IBookmark parseBookmarkContent(KXmlParser parser, IBookmark bookmark) throws XmlPullParserException, IOException {
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, BookmarkCollectionXmlSerializer.kFeeds);
        parser.nextTag();

        while (parser.getEventType() != XmlPullParser.END_TAG) {
            String nodeName = parser.getName();
            if (nodeName.equals(BookmarkCollectionXmlSerializer.kFeed)) {
                bookmark.addRssFeed(new RssFeed(parser.nextText()));
            } else {
                parser.skipSubTree();
            }
            parser.nextTag();
        }
        return (bookmark);
    }
}
