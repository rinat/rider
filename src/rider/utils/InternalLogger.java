package rider.utils;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import net.sf.microlog.midp.file.FileAppender;

public class InternalLogger {

    private static boolean _initialized = false;
    private static final Object _sync = new Object();
    private static InternalLogger _instance = null;

    Logger _logger;

    public void debug(String message) {
        this._logger.debug(message);
    }

    public void error(String message) {
        this._logger.error(message);
    }

    public void fatal(String message) {
        //#ifdef rider.debugEnabled        
        this._logger.fatal(message);
        //#endif
    }

    public void fatal(Exception exception) {
        if (exception != null) {
            synchronized (_sync) {
                //#ifdef rider.debugEnabled
                this._logger.fatal(exception.toString() + ":" + exception.getMessage());
                //#endif
            }
        }
    }

    public static InternalLogger getInstance() {
        if (!_initialized) {
            synchronized (_sync) {
                if (_instance == null) {
                    _instance = new InternalLogger();
                    _initialized = true;
                }
            }
        }
        return (_instance);
    }

    private InternalLogger() {
        this._logger = LoggerFactory.getLogger();
        FileAppender fileAppender = new FileAppender();
        fileAppender.setFileName(FileAppender.DEFAULT_FILENAME);
        this._logger.addAppender(fileAppender);
    }
}
