package rider.core.rss.loader;

import rider.utils.InternalLogger;

import java.io.InputStreamReader;

/**
 * Wrapper over InputStreamReader
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class RssInputStream implements IRssInputStream {

    private InputStreamReader _reader;

    public RssInputStream(InputStreamReader reader) throws NullPointerException {
        if (reader == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("RssInputStream.RssInputStream"));
            //#endif
            throw new NullPointerException("RssInputStream.RssInputStream");
        }
        this._reader = reader;
    }

    public InputStreamReader getStreamReader() {
        return (this._reader);
    }
}
