package rider.core.rss.loader;

import rider.core.rss.feed.IRssFeed;
import rider.utils.InternalLogger;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import java.io.IOException;
import java.io.InputStream;

/**
 * Byte by byte feed loading as raw string data loader
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class IncrementalRssContentLoader {

    private final Object _syncObject = new Object();
    private IRssFeed _rssFeed = null;
    private StringBuffer _rssContent = new StringBuffer();
    private HttpConnection _connection = null;
    private InputStream _inStream = null;

    /**
     * @param rssFeed must be not null
     * @throws NullPointerException
     * @see IRssFeed
     */
    public IncrementalRssContentLoader(IRssFeed rssFeed) throws NullPointerException {
        if (rssFeed == null) {
            throw new NullPointerException("IncrementalRssContentLoader.IncrementalRssContentLoader");
        }
        this._rssFeed = rssFeed;
    }

    public String getContent() {
        synchronized (_syncObject) {
            return (this._rssContent.toString());
        }
    }

    /**
     * Establish connection with rss feed link
     *
     * @throws IOException
     */
    public void tryConnectTo() throws IOException {
        this._inStream = null;
        if (this._rssContent.length() < 0) {
            this._rssContent.delete(0, this._rssContent.length() - 1);
        }

        this._connection = (HttpConnection) Connector.open(_rssFeed.getLink());
        this._connection.setRequestMethod(HttpConnection.GET);
        int code = _connection.getResponseCode();
        if (code == HttpConnection.HTTP_OK) {
            _inStream = _connection.openDataInputStream();
        } else {
            this._connection = null;
        }
        if (code != HttpConnection.HTTP_OK) {
            IOException exception = new IOException("tryConnectTo(IRssFeed rssFeed)");
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(exception);
            //#endif
            throw exception;
        }
    }

    /**
     * Break connection with rss feed link
     *
     * @throws IOException
     */
    public void tryDisconnect() throws IOException {
        if (this._connection != null) {
            this._connection.close();
        }
        if (this._connection != null) {
            this._connection.close();
        }
        if (this._inStream != null) {
            this._inStream.close();
        }
    }

    /**
     * Try load next byte
     *
     * @return -1 if not enough bytes for loading
     * @throws IOException
     */
    public int tryLoadNextByte() throws IOException {
        if (this._connection != null
                && this._inStream != null) {
            int ch = this._inStream.read();
            if (ch != -1) {
                this._rssContent.append((char) ch);
            }
            return (ch);
        }
        return (-1);
    }
}
