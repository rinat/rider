package rider.core.rss.parser;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class RssParserProvider {

    public static int getRssVersion(String content) throws Exception {
        int rssOne = content.indexOf("<rdf:RDF xmlns:rdf=");
        if (rssOne != -1) {
            return (1);
        } else {
            rssOne = content.indexOf("<rss version=\"2.0\">");
            if (rssOne != -1) {
                return (2);
            }
        }
        throw new Exception("Undefined rss version");
    }

    public static IRssParser createParser(int version) throws Exception {
        if (version == 1) {
            return (new RssVOneParser());
        } else if (version == 2) {
            return (new RssVTwoParser());
        }
        throw new Exception("Undefined rss parser version");
    }
}
