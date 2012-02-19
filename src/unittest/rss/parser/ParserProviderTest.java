package unittest.rss.parser;

import j2meunit.framework.TestCase;
import rider.core.rss.parser.IRssParser;
import rider.core.rss.parser.RssParserProvider;
import rider.core.rss.parser.RssVOneParser;
import rider.core.rss.parser.RssVTwoParser;

/**
 * Created by IntelliJ IDEA.
 * User: Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * Date: 19.05.2010
 * Time: 0:16:56
 * To change this template use File | Settings | File Templates.
 */
public class ParserProviderTest extends TestCase {
    public ParserProviderTest() {
    }

    public void runTest() {
        selectVersionOneTest();
        selectVersionTwoTest();
        createParserWithVersion(1, false);
        createParserWithVersion(2, false);
        createParserWithVersion(3, true);
    }

    private void createParserWithVersion(int version, boolean expectedError) {
        try {
            IRssParser parser = RssParserProvider.createParser(version);
            if (version == 1) {
                RssVOneParser vOneParser = (RssVOneParser) parser;
                this.assertNotNull(vOneParser);
            } else if (version == 2) {
                RssVTwoParser vOneParser = (RssVTwoParser) parser;
                this.assertNotNull(vOneParser);
            }
            this.assertTrue(!expectedError);
        } catch (Exception e) {
            this.assertTrue(expectedError);
        }
    }

    private void selectVersionOneTest() {
        try {
            int version = RssParserProvider.getRssVersion(createRssOne());
            this.assertEquals(version, 1);
            this.assertTrue(true);
        } catch (Exception e) {
            this.assertTrue(false);
        }
    }

    private void selectVersionTwoTest() {
        try {
            int version = RssParserProvider.getRssVersion(createRssTwo());
            this.assertEquals(version, 2);
            this.assertTrue(true);
        } catch (Exception e) {
            this.assertTrue(false);
        }
    }

    private String createRssTwo() {
        StringBuffer xmlString = new StringBuffer();
        xmlString.append("<?xml version=\"1.0\"?>");
        xmlString.append("<rss version=\"2.0\">");
        xmlString.append("  <channel>");
        xmlString.append("  </channel>");
        xmlString.append("</rss>");
        return (xmlString.toString());
    }

    private String createRssOne() {
        StringBuffer xmlString = new StringBuffer();
        xmlString.append("<?xml version=\"1.0\"?>");
        xmlString.append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"");
        xmlString.append("         xmlns=\"http://purl.org/rss/1.0/\">");
        xmlString.append("<channel rdf:about=\"http://www.xml.com/xml/news.rss\">");
        xmlString.append("</channel>");
        xmlString.append("</rdf:RDF>");
        return xmlString.toString();
    }
}
