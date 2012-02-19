package rider.core.file;

public interface FileReaderListener {

    void onFileRead(String content);

    void onFileReadError(Exception e);
}
