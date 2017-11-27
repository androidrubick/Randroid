package pub.androidrubick.android.app.bc;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import pub.androidrubick.android.app.ARContext;
import pub.androidrubick.base.utils.Objects;

/**
 *
 * 广播注册、注销的工具类
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/3/23.
 *
 * @since 1.0.0
 */
public class Broadcasts {

    private Broadcasts() {}

    /**
     * 注册广播，参数<code> actions </code> 为注册的行为
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
        if (Objects.isNull(receiver) || Objects.isEmpty(actions)) {
            return ;
        }
        IntentFilter filter = new IntentFilter();
        for (int i = 0; i < actions.length; i++) {
            filter.addAction(actions[i]);
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
        ARContext.app().registerReceiver(receiver, filter);
    }

    /**
     * 注销指定广播
     *
     * @since 1.0.0
     */
    public static void unregisterReceiver(BroadcastReceiver receiver) {
        try {
            ARContext.app().unregisterReceiver(receiver);
        } catch (Exception e) { }
    }

    /**
     * 注销指定广播
     */
    public static void unregisterReceiver(BroadcastReceiver...receivers) {
        if (Objects.isEmpty(receivers)) {
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
        ARContext.app().sendBroadcast(intent);
    }

}
