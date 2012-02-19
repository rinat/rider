package unittest.rss.parser;

import j2meunit.framework.TestCase;
import rider.core.rss.item.IRssItem;
import rider.core.rss.item.IRssItemCollection;
import rider.core.rss.loader.RssInputStream;
import rider.core.rss.parser.IRssParser;
import rider.core.rss.parser.RssParserProvider;
import rider.core.rss.parser.RssVOneParser;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * Date: 18.05.2010
 * Time: 23:33:18
 * To change this template use File | Settings | File Templates.
 */
public class RssVOneParserTest extends TestCase {
    public RssVOneParserTest() {
    }

    public void runTest() {
        exceptionConstructorTest();
        emptyContentTest();
        validRssTest();
        inValidRssTest();
    }

    private void exceptionConstructorTest() {
        try {
            RssVOneParser parser = new RssVOneParser();
            parser.parse(null, null);
        }
        catch (NullPointerException e) {
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
        assertEquals(rssItem.getTitle(), "Processing Inclusions with XSLT");
        assertEquals(rssItem.getLink(), "http://xml.com/pub/2000/08/09/xslt/xslt.html");
        assertEquals(rssItem.getDescription(), "Processing document inclusions with general XML tools");
    }

    private String createValidXml() {
        StringBuffer xmlString = new StringBuffer();
        xmlString.append("<?xml version=\"1.0\"?>");
        xmlString.append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"");
        xmlString.append("         xmlns=\"http://purl.org/rss/1.0/\">");
        xmlString.append("<channel rdf:about=\"http://www.xml.com/xml/news.rss\">");
        xmlString.append("</channel>");
        xmlString.append(" <item rdf:about=\"http://xml.com/pub/2000/08/09/xslt/xslt.html\">");
        xmlString.append("   <title>Processing Inclusions with XSLT</title>");
        xmlString.append("   <link>http://xml.com/pub/2000/08/09/xslt/xslt.html</link>");
        xmlString.append("   <description>");
        xmlString.append("Processing document inclusions with general XML tools");
        xmlString.append("</description>");
        xmlString.append(" </item>");
        xmlString.append("</rdf:RDF>");
        return xmlString.toString();
    }

    private String createInValidXml() {
        StringBuffer xmlString = new StringBuffer();
        xmlString.append("<?xml version=\"1.0\"?>");
        xmlString.append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"");
        xmlString.append("         xmlns=\"http://purl.org/rss/1.0/\">");
        xmlString.append("<channel rdf:about=\"http://www.xml.com/xml/news.rss\""); // invalid here
        xmlString.append("</channel>");
        xmlString.append(" <item rdf:about=\"http://xml.com/pub/2000/08/09/xslt/xslt.html\">");
        xmlString.append("   <title>Processing Inclusions with XSLT</title>");
        xmlString.append("   <link>http://xml.com/pub/2000/08/09/xslt/xslt.html</link>");
        xmlString.append("   <description>");
        xmlString.append("Processing document inclusions with general XML tools");
        xmlString.append("</description>");
        xmlString.append(" </item>");
        xmlString.append("</rdf:RDF>");
        return xmlString.toString();
    }

    private InputStreamReader createStreamReaderFrom(String content) {
        byte[] xmlByteArray = content.getBytes();
        ByteArrayInputStream xmlStream = new ByteArrayInputStream(xmlByteArray);
        return (new InputStreamReader(xmlStream));
    }
}
