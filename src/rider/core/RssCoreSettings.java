package rider.core;

import rider.utils.InternalLogger;
import rider.utils.Settings;

import javax.microedition.rms.RecordStoreException;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class RssCoreSettings {

    private final String kRecordStoreName = "rider.settings.info";
    private final String kBookmarksFieldName = "rider.bookmarks";

    private Settings _settings;

    public RssCoreSettings() throws RecordStoreException {
        this._settings = new Settings(kRecordStoreName);
    }

    public String loadBookmarks() {
        return (_settings.get(kBookmarksFieldName));
    }

    public void saveBookmarks(String bookmarks) {
        this._settings.put(kBookmarksFieldName, bookmarks);
    }

    public void save() {
        try {
            this._settings.save();
        } catch (RecordStoreException e) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(e);
            //#endif
        }
    }
}
