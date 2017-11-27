package pub.androidrubick.base.logging;

import android.os.SystemClock;

/**
 * 统计时间日志工具
 *
 * <p>
 * Created by Yin Yong on 2017/11/17.
 *
 * @since 1.0.0
 */
public class ARTimeSlotLogger {

    public static long time;

    public static void begin() {
        time = SystemClock.elapsedRealtime();
    }

    public static void end(String tag) {
        long delta = SystemClock.elapsedRealtime() - time;
        ARLogger.debug("[time slot] " + tag + " : " + delta);
        time = SystemClock.elapsedRealtime();
    }

}
