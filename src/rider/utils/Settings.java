package rider.utils;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Provide access to rms as key value
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class Settings {

    private String _recirdStoreName;
    private Hashtable _hashTable;

    public Settings(String recordStoreName) throws RecordStoreException, NullPointerException {
        if (recordStoreName == null) {
            throw new NullPointerException("Settings.Settings");
        }
        _recirdStoreName = recordStoreName;
        _hashTable = new Hashtable();
        load();
    }

    public String get(String key) {
        return (String) _hashTable.get(key);
    }

    public void put(String key, String value) {
        if (value == null) value = "";
        _hashTable.put(key, value);
    }

    private void load() throws RecordStoreException {
        RecordStore rs = null;
        RecordEnumeration re = null;
        try {
            rs = RecordStore.openRecordStore(_recirdStoreName, true);
            re = rs.enumerateRecords(null, null, false);
            while (re.hasNextElement()) {
                byte[] raw = re.nextRecord();
                String pref = new String(raw); // Parse out the name.
                int index = pref.indexOf('|');
                String name = pref.substring(0, index);
                String value = pref.substring(index + 1);
                put(name, value);
            }
        }
        finally {
            if (re != null) re.destroy();
            if (rs != null) rs.closeRecordStore();
        }
    }

    public void save() throws RecordStoreException {
        RecordStore rs = null;
        RecordEnumeration re = null;
        try {
            rs = RecordStore.openRecordStore(_recirdStoreName, true);
            re = rs.enumerateRecords(null, null, false); // First remove all records, a little clumsy.
            while (re.hasNextElement()) {
                int id = re.nextRecordId();
                rs.deleteRecord(id);
            }

            // Now save the preferences records.
            Enumeration keys = _hashTable.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = get(key);
                String pref = key + "|" + value;
                byte[] raw = pref.getBytes();
                rs.addRecord(raw, 0, raw.length);
            }
        }
        finally {
            if (re != null) re.destroy();
            if (rs != null) rs.closeRecordStore();
        }
    }
}
