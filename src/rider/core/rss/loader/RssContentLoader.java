package rider.core.rss.loader;

import rider.core.rss.feed.IRssFeed;
import rider.utils.InternalLogger;

import java.io.IOException;

/**
 * Provider rss content loading from IRssFeed
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see rider.core.rss.feed.IRssFeed
 * @see IRssContentListener
 */
public class RssContentLoader implements Runnable {

    private boolean _quit = false;
    private IRssFeed _rssFeed = null;
    private IRssContentListener _contentTarget = null;

    /**
     * @param rssFeed       must be not null
     * @param contentTarget must be not null
     * @throws NullPointerException
     * @see IRssContentListener
     */
    public RssContentLoader(IRssFeed rssFeed, IRssContentListener contentTarget) throws NullPointerException {
        if (rssFeed == null || contentTarget == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("RssContentLoader.RssContentLoader"));
            //#endif
            throw new NullPointerException("RssContentLoader.RssContentLoader");
        }
        this._rssFeed = rssFeed;
        this._contentTarget = contentTarget;
    }

    /**
     * run operation on new thread
     */
    public void start() {
        Thread thread = new Thread(this);
        try {
            thread.start();
        }
        catch (Exception ex) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(ex);
            //#endif
            this._contentTarget.onContentLoadError(_rssFeed, ex);
        }
    }

    /**
     * you must use start() function
     * don't use this function it's Runnable implementation
     */
    public void run() {
        IncrementalRssContentLoader rssIncLoader = new IncrementalRssContentLoader(_rssFeed);
        try {
            rssIncLoader.tryConnectTo();

            while (rssIncLoader.tryLoadNextByte() != -1) {
            }
            _contentTarget.onContentLoaded(_rssFeed, rssIncLoader.getContent());
            rssIncLoader.tryDisconnect();

        } catch (IOException ex) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(ex);
            //#endif
            this._contentTarget.onContentLoadError(_rssFeed, ex);
        } catch (IllegalArgumentException ex) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(ex);
            //#endif
            this._contentTarget.onContentLoadError(_rssFeed, ex);
        }
    }
}
