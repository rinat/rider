package rider.core.file;

import rider.utils.InternalLogger;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class FileReader implements Runnable {

    private String _filename = null;
    private FileReaderListener _listener = null;

    public FileReader(String filename, FileReaderListener listener) throws NullPointerException {
        if (listener == null || filename == null) {
            //#ifdef rider.debugEnabled
            InternalLogger.getInstance().fatal(new NullPointerException("FileReader.FileReader"));
            //#endif
            throw new NullPointerException("FileReader.FileReader");
        }
        this._filename = filename;
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
            onFileReadError(ex);
        }
    }

    /**
     * you must use start() function
     * don't use this function it's Runnable implementation
     */
    public void run() {
        try {
            readFile(this._filename);
        } catch (IOException ex) {
            onFileReadError(ex);
        }
    }

    private void readFile(String filename) throws IOException {
        StringBuffer buffer = new StringBuffer();

        FileConnection fconn = (FileConnection) Connector.open(filename, Connector.READ);
        InputStreamReader reader = new InputStreamReader(fconn.openInputStream());
        int readChar = reader.read();
        while (readChar != -1) {
            buffer.append((char) readChar);
            readChar = reader.read();
        }
        onFileReaded(buffer.toString());
        reader.close();
    }

    private void onFileReadError(Exception e) {
        if (this._listener != null) {
            this._listener.onFileReadError(e);
        }
    }

    private void onFileReaded(String content) {
        if (this._listener != null) {
            this._listener.onFileRead(content);
        }
    }
}
