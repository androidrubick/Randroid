package pub.androidrubick.base.logging;

/**
 * interface for logging in this library.
 *
 * <p/>
 *
 * Created by Yin Yong on 16/12/12.
 */
public interface ARLog {
    int DEBUG = 5;
    int VERBOSE = 4;
    int INFO = 3;
    int WARNING = 2;
    int ERROR = 1;

    void debug(String message);

    void info(String message);

    void warning(String message);

    void warning(String message, Throwable cause);

    void error(String message);

    void error(String message, Throwable cause);

    void setLevel(int level);

    int getLevel();
}
