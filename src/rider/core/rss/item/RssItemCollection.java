package rider.core.rss.item;

import rider.utils.InternalLogger;

import java.util.Vector;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see IRssItemCollection
 */
public class RssItemCollection implements IRssItemCollection {

    private Vector _rssItemsCollection = new Vector();

    public RssItemCollection() {
    }

    public void addRssItem(IRssItem rssItem) throws NullPointerException {
        if (rssItem == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException());
            //#endif
            throw new NullPointerException();
        }
        this._rssItemsCollection.addElement(rssItem);
    }

    public void removeAllRssItems() {
        this._rssItemsCollection.removeAllElements();
    }

    public int getCount() {
        return (this._rssItemsCollection.size());
    }

    public IRssItem getRssItem(int index) {
        return ((IRssItem) this._rssItemsCollection.elementAt(index));
    }

    public void removeRssItem(int index) {
        this._rssItemsCollection.removeElementAt(index);
    }

    public void removeRssItem(IRssItem rssItem) throws NullPointerException {
        if (rssItem == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException());
            //#endif
            throw new NullPointerException();
        }
        this._rssItemsCollection.removeElement(rssItem);
    }

}
