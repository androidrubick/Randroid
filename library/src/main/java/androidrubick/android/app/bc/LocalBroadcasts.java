package androidrubick.android.app.bc;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import androidrubick.android.app.ARContext;
import androidrubick.base.utils.ArraysCompat;
import androidrubick.base.utils.Objects;

/**
 * 本地广播的封装
 *
 * @author Yin Yong
 *
 * @since 1.0.0
 *
 */
public class LocalBroadcasts {

    private LocalBroadcasts() {}

    private static LocalBroadcastManager sLocalBroadcastManager;
    private static void checkLocalBroadcastManagerInstance() {
        if (null == sLocalBroadcastManager) {
            synchronized (LocalBroadcasts.class) {
                if (null == sLocalBroadcastManager) {
                    sLocalBroadcastManager = LocalBroadcastManager.getInstance(ARContext.app());
                }
            }
        }
    }

    /**
     * 注册广播，参数<code> action </code> 为注册的行为
     *
     * @since 1.0.0
     */
    public static void registerReceiver(BroadcastReceiver receiver, String action) {
        if (Objects.isNull(receiver)) {
            return ;
        }
        registerReceiver(receiver, new IntentFilter(action));
    }

    /**
     * 注册广播，参数<code> actions </code> 为注册的行为
     *
     * @since 1.0.0
     */
    public static void registerReceiver(BroadcastReceiver receiver, String...actions) {
        if (Objects.isNull(receiver) || ArraysCompat.isEmpty(actions)) {
            return ;
        }
        IntentFilter filter = new IntentFilter();
        for (String action : actions) {
            filter.addAction(action);
        }
        registerReceiver(receiver, filter);
    }

    /**
     * 注册广播，参数<code> filters </code> 为注册的行为
     *
     * @since 1.0.0
     */
    public static void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (Objects.isNull(receiver) || Objects.isNull(filter)) {
            return ;
        }
        checkLocalBroadcastManagerInstance();
        sLocalBroadcastManager.registerReceiver(receiver, filter);
    }

    /**
     * 注销指定广播
     *
     * @since 1.0.0
     */
    public static void unregisterReceiver(BroadcastReceiver receiver) {
        checkLocalBroadcastManagerInstance();
        try {
            sLocalBroadcastManager.unregisterReceiver(receiver);
        } catch (Exception ignored) { }
    }

    /**
     * 注销指定广播
     *
     * @since 1.0.0
     */
    public static void unregisterReceiver(BroadcastReceiver...receivers) {
        if (ArraysCompat.isEmpty(receivers)) {
            return ;
        }

        for (BroadcastReceiver br : receivers) {
            unregisterReceiver(br);
        }
    }

    /**
     * 发送指定广播
     *
     * @since 1.0.0
     */
    public static void sendBroadcast(String action) {
        sendBroadcast(new Intent(action));
    }

    /**
     * 发送指定广播
     *
     * @since 1.0.0
     */
    public static void sendBroadcast(Intent intent) {
        if (Objects.isNull(intent)) {
            return ;
        }
        checkLocalBroadcastManagerInstance();
        sLocalBroadcastManager.sendBroadcast(intent);
    }

}
