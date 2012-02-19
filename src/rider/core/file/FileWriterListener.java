package rider.core.file;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public interface FileWriterListener {

    void OnFileWritten(String content);

    void onFileWriteError(Exception e);
}
