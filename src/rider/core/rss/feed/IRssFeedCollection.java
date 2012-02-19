package rider.core.rss.feed;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public interface IRssFeedCollection {

    int getCount();

    IRssFeed getRssFeed(int index);

    void addRssFeed(IRssFeed rssFeed) throws NullPointerException;

    void removeRssFeed(int index);

    void removeRssFeed(IRssFeed bookmark) throws NullPointerException;

    void removeAllFeeds();
}
