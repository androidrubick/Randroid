package pub.androidrubick.demo;

import androidrubick.android.app.ARContext;
import androidrubick.android.logging.AndroidLog;
import androidrubick.base.logging.ARLog;
import androidrubick.base.logging.ARLogger;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/12/5.
 */
public class App extends ARContext {

    @Override
    public void onCreate() {
        super.onCreate();
        ARLogger.setARLog(new AndroidLog("yytest", ARLog.DEBUG));
    }
}
