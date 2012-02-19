package rider.core.rss.item;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public interface IRssItemCollection {

    int getCount();

    IRssItem getRssItem(int index);

    /**
     * @param rssItem must be not null
     * @throws NullPointerException
     * @see IRssItem
     */
    void addRssItem(IRssItem rssItem) throws NullPointerException;

    void removeRssItem(int index);

    /**
     * @param rssItem must be not null
     * @throws NullPointerException
     * @see IRssItem
     */
    void removeRssItem(IRssItem rssItem) throws NullPointerException;

    void removeAllRssItems();
}
