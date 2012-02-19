package rider.core.rss.feed;

import rider.core.rss.item.IRssItem;
import rider.core.rss.item.IRssItemCollection;
import rider.core.rss.parser.IRssParserListener;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see IRssParserListener
 */
public abstract class IRssFeed implements IRssParserListener {

    public abstract String getLink();

    public abstract void setLink(String link);

    /**
     * @param index
     * @return rss item by index
     * @see rider.core.rss.item.IRssItem
     */
    public abstract IRssItem getRssItem(int index);

    /**
     * @return rss items count associated with current IRssFeed
     */
    public abstract int getCount();

    public abstract void removeAllRssItems();

    /**
     * @return all rss items associated with current IRssFeed
     * @see rider.core.rss.item.IRssItemCollection
     */
    public abstract IRssItemCollection getRssItems();

    /**
     * @return IRssFeed state
     * @see IRssFeedState
     */
    public abstract IRssFeedState getRssFeedState();
}
