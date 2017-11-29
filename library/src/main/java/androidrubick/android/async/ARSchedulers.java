package androidrubick.android.async;

import android.support.annotation.NonNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/11/28.
 *
 * @since 1.0.0
 */
public class ARSchedulers {

    public static void io(@NonNull Runnable run) {
        Schedulers.io().scheduleDirect(run);
    }

    public static void newThread(@NonNull Runnable run) {
        Schedulers.newThread().scheduleDirect(run);
    }

    public static void mainThread(@NonNull Runnable run) {
        AndroidSchedulers.mainThread().scheduleDirect(run);
    }

}
