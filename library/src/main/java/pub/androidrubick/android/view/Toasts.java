package pub.androidrubick.android.view;

import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import pub.androidrubick.android.app.ARContext;

/**
 *
 * helper for show toast
 *
 * <p>
 * Created by Yin Yong on 2017/11/24.
 *
 * @since 1.0.0
 */
public class Toasts {

    private static WeakReference<Toast> mToastRef;

    private static Toast ensureToastInstance(){
        Toast temp;
        if (null == mToastRef || null == (temp = mToastRef.get())) {
            Context context = ARContext.app();
            mToastRef = new WeakReference<>(Toast.makeText(context, "", Toast.LENGTH_SHORT));
            temp = mToastRef.get();
        }
        return temp;
    }

    public static void show(CharSequence msg) {
        show0(msg, Toast.LENGTH_SHORT);
    }

    public static void show(int msgRes) {
        Context context = ARContext.app();
        show(context.getString(msgRes));
    }

    public static void showLong(String msg) {
        show0(msg, Toast.LENGTH_LONG);
    }

    public static void showLong(int msgRes) {
        Context context = ARContext.app();
        showLong(context.getString(msgRes));
    }

    private static void show0(CharSequence message, int length){
        Toast temp = ensureToastInstance();
        temp.setDuration(length);
        temp.setText(message);
        temp.show();
    }

}
