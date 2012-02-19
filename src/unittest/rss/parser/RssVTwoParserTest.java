package unittest.rss.parser;

import j2meunit.framework.TestCase;
import rider.core.rss.item.IRssItem;
import rider.core.rss.item.IRssItemCollection;
import rider.core.rss.loader.RssInputStream;
import rider.core.rss.parser.IRssParser;
import rider.core.rss.parser.RssParserProvider;
import rider.core.rss.parser.RssVTwoParser;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * Date: 19.05.2010
 * Time: 0:05:26
 * To change this template use File | Settings | File Templates.
 */
public class RssVTwoParserTest extends TestCase {
    public RssVTwoParserTest() {
    }

    public void runTest() {
        exceptionConstructorTest();
        emptyContentTest();
        validRssTest();
        inValidRssTest();
    }

    private void exceptionConstructorTest() {
        try {
            RssVTwoParser parser = new RssVTwoParser();
            parser.parse(null, null);
        }
        catch (NullPointerException e) {
            this.assertTrue(true);
            return;
        }
        this.assertTrue(false);
    }

    private void emptyContentTest() {
        String rssContent = "";
        TestParserListener listener = new TestParserListener();

        IRssParser parser = null;
        try {
            parser = RssParserProvider.createParser(
                    RssParserProvider.getRssVersion(rssContent));
        } catch (Exception e) {
            this.assertTrue(true);
            return;
        }
        this.assertTrue(false);
    }

    private void inValidRssTest() {
        String rssContent = createInValidXml();
        TestParserListener listener = new TestParserListener();

        IRssParser parser = null;
        try {
            parser = RssParserProvider.createParser(
                    RssParserProvider.getRssVersion(rssContent));
        } catch (Exception e) {
            this.assertTrue(false);
            return;
        }
        parser.parse(new RssInputStream(createStreamReaderFrom(rssContent)), listener);
        this.assertTrue(listener._withError);
    }

    private void validRssTest() {
        String rssContent = createValidXml();
        TestParserListener listener = new TestParserListener();

        IRssParser parser = null;
        try {
            parser = RssParserProvider.createParser(
                    RssParserProvider.getRssVersion(rssContent));
        } catch (Exception e) {
            this.assertTrue(false);
            return;
        }
        parser.parse(new RssInputStream(createStreamReaderFrom(rssContent)), listener);

        this.assertTrue(!listener._withError);

        IRssItemCollection rssItems = listener._rssItems;
        assertNotNull(rssItems);
        assertEquals(1, rssItems.getCount());

        IRssItem rssItem = rssItems.getRssItem(0);
        assertNotNull(rssItem);
        assertEquals(rssItem.getTitle(), "Star City");
        assertEquals(rssItem.getLink(), "http://liftoff.msfc.nasa.gov/news/2003/news-starcity.asp");
        assertEquals(rssItem.getDescription(), "How do Americans get ready to work with Russians aboard");
    }

    private String createValidXml() {
        StringBuffer xmlString = new StringBuffer();
        xmlString.append("<?xml version=\"1.0\"?>");
        xmlString.append("<rss version=\"2.0\">");
        xmlString.append("  <channel>");
        xmlString.append("    <item>");
        xmlString.append("      <title>Star City</title>");
        xmlString.append("      <link>http://liftoff.msfc.nasa.gov/news/2003/news-starcity.asp</link>");
        xmlString.append("      <description>How do Americans get ready to work with Russians aboard</description>");
        xmlString.append("      <pubDate>Tue, 03 Jun 2003 09:39:21 GMT</pubDate>");
        xmlString.append("      <guid>http://liftoff.msfc.nasa.gov/2003/06/03.html#item573</guid>");
        xmlString.append("    </item>");
        xmlString.append("  </channel>");
        xmlString.append("</rss>");
        return (xmlString.toString());
    }

    private String createInValidXml() {
        StringBuffer xmlString = new StringBuffer();
        xmlString.append("<?xml version=\"1.0\"?>");
        xmlString.append("<rss version=\"2.0\">");
        xmlString.append("  <channel>");
        xmlString.append("    <item"); // invalid here
        xmlString.append("      <title>Star City</title>");
        xmlString.append("      <link>http://liftoff.msfc.nasa.gov/news/2003/news-starcity.asp</link>");
        xmlString.append("      <description>How do Americans get ready to work with Russians aboard</description>");
        xmlString.append("      <pubDate>Tue, 03 Jun 2003 09:39:21 GMT</pubDate>");
        xmlString.append("      <guid>http://liftoff.msfc.nasa.gov/2003/06/03.html#item573</guid>");
        xmlString.append("    </item>");
        xmlString.append("  </channel>");
        xmlString.append("</rss>");
        return (xmlString.toString());
    }

    private InputStreamReader createStreamReaderFrom(String content) {
        byte[] xmlByteArray = content.getBytes();
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xmlByteArray);
        return (new InputStreamReader(xmlStream));
    }
}
