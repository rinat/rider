package rider.core.rss.item;

import rider.utils.InternalLogger;

/**
 * Rss item tag representation
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class RssItem implements IRssItem {

    private String _description;
    private String _link;
    private String _title;
    private IRssItemState _rssItemState = new RssItemState();

    public RssItem(String title, String link, String description) throws NullPointerException {
        if (title == null || link == null || description == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("RssItem.RssItem"));
            //#endif
            throw new NullPointerException("RssItem.RssItem");
        }
        this._title = title;
        this._link = link;
        this._description = description;
    }

    public IRssItemState getRssItemState() {
        return (_rssItemState);
    }

    public String getDescription() {
        return (this._description);
    }

    public String getLink() {
        return (this._link);
    }

    public String getTitle() {
        return (this._title);
    }
}
