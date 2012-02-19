package unittest.rss.parser;

import rider.core.rss.item.IRssItem;
import rider.core.rss.item.IRssItemCollection;
import rider.core.rss.item.RssItemCollection;
import rider.core.rss.parser.IRssParserListener;

/**
 * Created by IntelliJ IDEA.
 * User: Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * Date: 18.05.2010
 * Time: 23:39:57
 * To change this template use File | Settings | File Templates.
 */
public class TestParserListener implements IRssParserListener {

    public IRssItemCollection _rssItems = new RssItemCollection();
    public boolean _withError = false;

    public TestParserListener() {
    }

    public void onRssItemParsed(IRssItem item) {
        _rssItems.addRssItem(item);
    }

    public void onRssItemParseError(Exception ex) {
        _withError = true;
    }
}