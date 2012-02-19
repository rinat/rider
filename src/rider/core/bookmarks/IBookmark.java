/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rider.core.bookmarks;

import rider.core.rss.feed.IRssFeed;
import rider.core.rss.feed.IRssFeedCollection;

/**
 * IBookmark representaion
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public interface IBookmark {

    /**
     * @return bookmark title
     */
    String getTitle();

    /**
     * set bokmark title
     *
     * @param title
     * @throws NullPointerException
     */
    void setTitle(String title) throws NullPointerException;

    /**
     * adding IRssFeed to bookmark
     *
     * @param rssFeed
     * @throws NullPointerException
     * @see IRssFeed
     */
    void addRssFeed(IRssFeed rssFeed) throws NullPointerException;

    void removeRssFeed(int feedIndex);

    /**
     * @return count of IRssFeed
     */
    int getCount();

    /**
     * get IRssFeed by index
     *
     * @param index
     * @return
     * @see IRssFeed
     */
    IRssFeed getRssFeed(int index);

    /**
     * @return IRssFeed collection
     * @see rider.core.rss.feed.IRssFeedCollection
     */
    IRssFeedCollection getRssFeeds();
}
