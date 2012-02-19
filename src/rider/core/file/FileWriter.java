package rider.core.file;

import rider.utils.InternalLogger;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class FileWriter implements Runnable {

    private String _filename = null;
    private String _content = null;
    private FileWriterListener _listener = null;

    public FileWriter(String filename, String content, FileWriterListener listener) throws NullPointerException {
        if (listener == null || filename == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("FileWriter.FileWriter"));
            //#endif
            throw new NullPointerException("FileWriter.FileWriter");
        }
        this._filename = filename;
        this._content = content;
        this._listener = listener;
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
            onFileWriteError(ex);
        }
    }

    /**
     * you must use start() function
     * don't use this function it's Runnable implementation
     */
    public void run() {
        try {
            String url = _filename;
            String string = _content;
            byte data[] = string.getBytes();
            FileConnection fconn = (FileConnection) Connector.open(url, Connector.READ_WRITE);
            if (!fconn.exists()) {
                fconn.create();
            }
            OutputStream ops = fconn.openOutputStream();
            ops.write(data);
            ops.close();
            fconn.close();
            OnFileWritten(_content);
        }
        catch (IOException ioe) {
            onFileWriteError(ioe);
        }
        catch (SecurityException se) {
            onFileWriteError(se);
        }
    }

    private void onFileWriteError(Exception e) {
        if (this._listener != null) {
            this._listener.onFileWriteError(e);
        }
    }

    private void OnFileWritten(String content) {
        if (this._listener != null) {
            this._listener.OnFileWritten(content);
        }
    }
}
