package androidrubick.android.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.CallSuper;

import static androidrubick.base.utils.Preconditions.checkNotNull;

/**
 * {@doc}
 * <p></p>
 * Created by Yin Yong on 2017/11/24.
 *
 * @since 1.0.0
 */
public class ARContext extends Application {

    private static ARContext sApp;

    public static Context app() {
        checkNotNull(sApp, "Method app() should be invoked after Application onCreate()");
        return sApp;
    }

    /**
     * 获取该包名下的类的加载器
     */
    public static ClassLoader appClassLoader() {
        try {
            return app().getClassLoader();
        } catch (Throwable e) {
            return Thread.currentThread().getContextClassLoader();
        }
    }

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }

}
