package rider.core.rss.feed;

import rider.utils.InternalLogger;

import java.util.Vector;

/**
 * Simple IRssFeed collection
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see IRssFeedCollection
 */
public class RssFeedCollection implements IRssFeedCollection {

    Vector _feedCollection = new Vector();

    public RssFeedCollection() {
    }

    public void addRssFeed(IRssFeed rssFeed) throws NullPointerException {
        if (rssFeed == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("addRssFeed.addRssFeed"));
            //#endif
            throw new NullPointerException();
        }
        _feedCollection.addElement(rssFeed);
    }

    public void removeAllFeeds() {
        this._feedCollection.removeAllElements();
    }

    public int getCount() {
        return (this._feedCollection.size());
    }

    public IRssFeed getRssFeed(int index) {
        return ((IRssFeed) this._feedCollection.elementAt(index));
    }

    public void removeRssFeed(int index) {
        this._feedCollection.removeElementAt(index);
    }

    public void removeRssFeed(IRssFeed rssFeed) throws NullPointerException {
        if (rssFeed == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("RssFeedCollection.removeRssFeed"));
            //#endif
            throw new NullPointerException("RssFeedCollection.removeRssFeed");
        }
        this._feedCollection.removeElement(rssFeed);
    }

}
