package rider.core.rss.loader;

import rider.core.rss.feed.IRssFeed;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see RssContentLoader
 */
public interface IRssContentListener {

    void onContentLoaded(IRssFeed rssFeed, String content);

    void onContentLoadError(IRssFeed rssFeed, Exception exception);
}
