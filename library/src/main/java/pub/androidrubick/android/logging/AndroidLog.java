package pub.androidrubick.android.logging;

import android.util.Log;

import pub.androidrubick.base.logging.ARLog;

/**
 *
 * <p/>
 *
 * Created by Yin Yong on 16/12/12.
 *
 * @since 1.0.0
 */
public class AndroidLog implements ARLog {
    private int mLevel = INFO;
    private String TAG = "[AR]";

    public AndroidLog() {
    }

    public AndroidLog(String tag) {
        this.TAG = tag;
    }

    public AndroidLog(int level) {
        this.mLevel = level;
    }

    public AndroidLog(String tag, int level) {
        this.TAG = tag;
        this.mLevel = level;
    }

    public void debug(String message) {
        if(this.mLevel == DEBUG) {
            Log.d(TAG, message);
        }
    }

    public void info(String message) {
        if(this.mLevel >= INFO) {
            Log.i(TAG, message);
        }
    }

    public void warning(String message) {
        if(this.mLevel >= WARNING) {
            Log.w(TAG, message);
        }
    }

    @Override
    public void warning(String message, Throwable cause) {
        if(this.mLevel >= WARNING) {
            Log.w(TAG, message, cause);
        }
    }

    public void error(String message) {
        if(this.mLevel >= ERROR) {
            Log.e(TAG, message);
        }

    }

    public void error(String message, Throwable cause) {
        if(this.mLevel >= ERROR) {
            Log.e(TAG, message, cause);
        }

    }

    public int getLevel() {
        return this.mLevel;
    }

    public void setLevel(int level) {
        if(level <= DEBUG && level >= ERROR) {
            this.mLevel = level;
        } else {
            throw new IllegalArgumentException("Log level is not between ERROR and DEBUG");
        }
    }
}
