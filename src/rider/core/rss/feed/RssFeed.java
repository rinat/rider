package rider.core.rss.feed;

import rider.core.rss.item.IRssItem;
import rider.core.rss.item.IRssItemCollection;
import rider.core.rss.item.RssItemCollection;
import rider.utils.InternalLogger;

/**
 * Provide feed and IRssItemCollection
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see rider.core.rss.item.IRssItemCollection
 */
public class RssFeed extends IRssFeed {

    private String _link;
    private IRssItemCollection _rssItemCollection = new RssItemCollection();
    private IRssFeedState _rssFeedState = new RssFeedState();

    public RssFeed(String link) throws NullPointerException {
        if (link == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("RssFeed.RssFeed"));
            //#endif
            throw new NullPointerException("RssFeed.RssFeed");
        }
        this._link = link;
    }

    public void setLink(String link) {
        this._link = link;
        _rssItemCollection.removeAllRssItems();
    }

    public String getLink() {
        return (this._link);
    }

    public IRssItem getRssItem(int index) {
        return (this._rssItemCollection.getRssItem(index));
    }

    public int getCount() {
        return (this._rssItemCollection.getCount());
    }

    public void removeAllRssItems() {
        this._rssItemCollection.removeAllRssItems();
    }

    public void onRssItemParsed(IRssItem item) throws NullPointerException {
        this._rssItemCollection.addRssItem(item);
    }

    public void onRssItemParseError(Exception ex) {
    }

    public IRssItemCollection getRssItems() {
        return (this._rssItemCollection);
    }

    public IRssFeedState getRssFeedState() {
        return (_rssFeedState);
    }
}
