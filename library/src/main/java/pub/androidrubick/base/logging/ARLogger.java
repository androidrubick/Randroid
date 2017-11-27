package pub.androidrubick.base.logging;

/**
 * util for {@link ARLog}
 *
 * <p/>
 *
 * Created by Yin Yong on 16/12/12.
 *
 * @since 1.0.0
 */
public final class ARLogger {

    private static ARLog sInstance;

    /**
     * set global {@link ARLog} instance
     */
    public static void setVSLog(ARLog instance) {
        sInstance = instance;
    }

    public static void debug(String message) {
        if (null != sInstance) {
            sInstance.debug(message);
        }
    }

    public static void info(String message) {
        if (null != sInstance) {
            sInstance.info(message);
        }
    }

    public static void warning(String message) {
        if (null != sInstance) {
            sInstance.warning(message);
        }
    }

    public static void warning(String message, Throwable cause) {
        if (null != sInstance) {
            sInstance.warning(message, cause);
        }
    }

    public static void error(String message) {
        if (null != sInstance) {
            sInstance.error(message);
        }
    }

    public static void error(String message, Throwable cause) {
        if (null != sInstance) {
            sInstance.error(message, cause);
        }
    }

    public static void setLevel(int level) {
        if (null != sInstance) {
            sInstance.setLevel(level);
        }
    }

    public static int getLevel() {
        if (null != sInstance) {
            return sInstance.getLevel();
        }
        return 0;
    }

}
