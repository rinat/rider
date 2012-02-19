package rider;

import rider.utils.InternalLogger;

import javax.microedition.lcdui.Image;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Provide all icons associated with Rider MIDlet
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class Icons {

    private static final Object _sync = new Object();
    private static boolean _initialized = false;
    private static Icons _instance;

    private Hashtable _nameToImage = new Hashtable();
    private String _dirIconPath = "/icons/dir.png";
    private String _bookmarksFileIconPath = "/icons/bookmarks-file.png";
    private String _aboutIconPath = "/icons/rider-min.png";
    private String _readIconPath = "/icons/rss-item-read.png";
    private String _unreadIconPath = "/icons/rss-item-unread.png";
    private String _bookmarksIconPath = "/icons/rss-bookmark.png";
    private String _rssFeedLoadedIconPath = "/icons/rss-feed-loaded.png";
    private String _rssFeedNotLoadedPath = "/icons/rss-feed-notloaded.png";
    private String _rssFeedLoadingPath = "/icons/rss-feed-loading.png";
    private String _rssFeedLoadingErrorPath = "/icons/rss-feed-loading-error.png";

    public static Icons getInstance() {
        if (!_initialized) {
            synchronized (_sync) {
                if (_instance == null) {
                    _instance = new Icons();
                    _initialized = true;
                }
            }
        }
        return (_instance);
    }

    public Image getRssLoadingErrorIcon() {
        return (getOrCreateImage(_rssFeedLoadingErrorPath));
    }

    /**
     * @return Image associated with rss not loaded state
     */
    public Image getRssNotLoadedIcon() {
        return (getOrCreateImage(_rssFeedNotLoadedPath));
    }

    /**
     * @return Image associated with rss loading state
     */
    public Image getRssLoadingIcon() {
        return (getOrCreateImage(_rssFeedLoadingPath));
    }

    /**
     * @return Image associated with rss feed loaded state
     */
    public Image getRssFeedLoadedIcon() {
        return (getOrCreateImage(_rssFeedLoadedIconPath));
    }
    
    /**
     * @return Image associated with bookmarks
     */
    public Image getBookmarkIcon() {
        return (getOrCreateImage(_bookmarksIconPath));
    }

    /**
     * @return Image associated with read rss item
     */
    public Image getReadRssItemIcon() {
        return (getOrCreateImage(_readIconPath));
    }

    /**
     * @return Image associated with unread rss item
     */
    public Image getUnreadRssItemIcon() {
        return (getOrCreateImage(_unreadIconPath));
    }

    /**
     * @return Image associated with directory icon or null if not exist
     */
    public Image getDirIcon() {
        return (getOrCreateImage(_dirIconPath));
    }

    /**
     * @return Image associated with file icon or null if not exist
     */
    public Image getBookmarksFileIcon() {
        return (getOrCreateImage(_bookmarksFileIconPath));
    }

    /**
     * @return Image associated with about icon or null if not exist
     */
    public Image getAboutIcon() {
        return (getOrCreateImage(_aboutIconPath));
    }

    /**
     * image by path loader
     *
     * @param path path to image
     * @return null if not exist
     */
    private Image getOrCreateImage(String path) {
        synchronized (_sync) {
            if (_nameToImage.containsKey(path)) {
                return (Image) _nameToImage.get(path);
            }
            Image createdImage;
            try {
                createdImage = Image.createImage(path);
                _nameToImage.put(path, createdImage);
            } catch (IOException e) {
                //#ifdef rider.debugEnabled
                InternalLogger.getInstance().fatal(e);
                //#endif
                createdImage = null;
            }
            return (createdImage);
        }
    }

    private Icons() {
    }
}
