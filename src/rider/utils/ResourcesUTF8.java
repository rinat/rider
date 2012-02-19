package rider.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UTFDataFormatException;
import java.util.Hashtable;


public class ResourcesUTF8 {

    public static final char DELIMITER = '=';

    private Hashtable _hashtable;
    private StringBuffer _buffer = new StringBuffer();
    private boolean _endFile = false;

    private String _fileName = null;
    private String _directory;
    private String _extenssion = "";
    private String _name;
    private boolean _load = false;
    private boolean _error = false;

    public ResourcesUTF8(String dir, String name, String extension) {
        this._extenssion = extension;
        this._load = false;
        this._name = name;
        this._directory = '/' + (dir == null || dir.trim().length() == 0 ? "" : dir + '/');
    }

    public String get(String name) {
        if (!_load) {
            if (!load())
                return "";
        }
        String str = (String) _hashtable.get(name);
        if (str == null)
            return "";
        else
            return str;
    }

    public void unload() {
        _hashtable = null;
        if (_fileName != null) {
            _name = null;
            _extenssion = null;
            _directory = null;
        }
        _load = false;
        _endFile = true;
        _buffer = null;
    }

    public boolean load() {
        if (_error)
            return false;
        DataInputStream in = null;
        try {
            InputStream inputStream = getInputStream();
            if (inputStream == null)
                return false;

            in = new DataInputStream(inputStream);
            _hashtable = new Hashtable();
            _endFile = false;
            _buffer = new StringBuffer();

            String str = readKey(in);
            while (str != null) {
                String key = str.trim();
                if (key.length() > 0) {
                    String value = readValue(in);
                    if (value != null) {
                        _hashtable.put(key, value.trim());
                    }
                }
                str = readKey(in);
            }
            in.close();
            _load = true;
            return true;
        }
        catch (Exception io) {
            _error = true;
            unload();
            return false;
        }
        finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException ex) {
                }
        }
    }

    private InputStream getInputStream() {
        if (_error)
            return null;
        if (_fileName == null) {
            InputStream in = null;
            String locale = System.getProperty("microedition.locale");
            if (locale != null) {
                locale = locale.replace('-', '_');
                _fileName = _directory + _name + "_" + locale + "." + _extenssion;
                in = this.getClass().getResourceAsStream(_fileName);
                if (in != null) {
                    return in;
                } else {
                    int i = locale.indexOf('_');
                    if (i != -1) {
                        locale = locale.substring(0, i);
                        _fileName = _directory + _name + "_" + locale + "." + _extenssion;
                        in = this.getClass().getResourceAsStream(_fileName);
                        if (in != null) {
                            return in;
                        }
                    }
                }
            }
            _fileName = _directory + _name + "." + _extenssion;
            in = this.getClass().getResourceAsStream(_fileName);
            if (in == null) {
                _error = true;
                unload();
                return null;
            } else {
                return in;
            }
        } else {
            return this.getClass().getResourceAsStream(_fileName);
        }
    }

    private String readKey(DataInputStream in)
            throws IOException {
        if (_endFile)
            return null;
        _buffer.setLength(0);
        int r = -1;
        while ((r = in.read()) > 0 && r != 0x0A && r != DELIMITER) {
            char c = (char) r;
            _buffer.append(c);
        }
        if (r == -1) {
            _endFile = true;
            return null;
        }
        return _buffer.toString();
    }

    private String readValue(InputStream in)
            throws IOException {
        if (_endFile)
            return null;

        _buffer.setLength(0);
        int c, char2, char3;
        int r = 0;
        while ((r = in.read()) > 0 && r != 0x0A) {
            c = r & 0xff;
            switch (c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    _buffer.append((char) c);
                    break;

                case 12:
                case 13:
                    char2 = getByte(in);
                    if ((char2 & 0xC0) != 0x80)
                        throw new UTFDataFormatException();
                    _buffer.append((char) (((c & 0x1F) << 6) | (char2 & 0x3F)));
                    break;

                case 14:
                    char2 = getByte(in);
                    char3 = getByte(in);
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new UTFDataFormatException();

                    _buffer.append((char) (((c & 0x0F) << 12) |
                            ((char2 & 0x3F) << 6) |
                            ((char3 & 0x3F) << 0)));
                    break;

                default:
                    throw new UTFDataFormatException();
            }
        }
        return _buffer.toString();
    }

    private int getByte(InputStream in)
            throws IOException, UTFDataFormatException {
        int r = in.read();
        if (r < 0 || r == 0x0A) {
            _endFile = true;
            throw new UTFDataFormatException();
        }
        return r;
    }
}


