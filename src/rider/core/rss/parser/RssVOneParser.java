package rider.core.rss.parser;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import rider.core.rss.item.IRssItem;
import rider.core.rss.item.RssItem;
import rider.core.rss.loader.IRssInputStream;
import rider.utils.InternalLogger;
import rider.utils.StringUtil;

import java.io.IOException;

public class RssVOneParser implements IRssParser {

    public RssVOneParser() {
    }

    public void parse(IRssInputStream inputStream, IRssParserListener listener) throws NullPointerException {
        if (listener == null || inputStream == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("RssVOneParser.RssVOneParser"));
            //#endif
            throw new NullPointerException("RssVOneParser.parse");
        }
        try {
            KXmlParser parser = new KXmlParser();
            parser.setInput(inputStream.getStreamReader());
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "rdf:RDF");
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "channel");
            while (parser.getEventType() != XmlPullParser.END_TAG) {
                String nodeName = parser.getName();
                if (nodeName.equals("item")) {
                    listener.onRssItemParsed(parseRssItem(parser));
                } else {
                    parser.skipSubTree();
                }
                parser.nextTag();
            }
        } catch (XmlPullParserException ex) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(ex);
            //#endif
            listener.onRssItemParseError(ex);
        } catch (IOException ex) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(ex);
            //#endif
            listener.onRssItemParseError(ex);
        }
    }

    private IRssItem parseRssItem(KXmlParser parser) throws XmlPullParserException, IOException {
        String title = "";
        String link = "";
        String description = "";

        parser.nextTag();
        while (parser.getEventType() != XmlPullParser.END_TAG) {
            String nodeName = parser.getName();

            if (nodeName.equals("title")) {
                title = parser.nextText();
            } else if (nodeName.equals("description")) {
                description = StringUtil.removeHtml(parser.nextText());
            } else if (nodeName.equals("link")) {
                link = parser.nextText();
            } else {
                parser.skipSubTree();
            }
            parser.nextTag();
        }
        return (new RssItem(title, link, description));
    }
}
