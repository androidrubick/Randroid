package androidrubick.android.async;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import androidrubick.base.utils.Exceptions;
import androidrubick.base.utils.Primitives;

import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.isProxyClass;
import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * wrap interfaces invocations, so that we make sure that
 *
 * they are invoked in main looper
 *
 * <p>
 * Created by Yin Yong on 2018/1/3.
 *
 * @since 1.0.0
 */
public class MainLooperProxy {

    /**
     * @param raw  raw object, holding interface(s)
     * @param <TS> super interface(s) to async proxy
     * @param <T>  class type of raw object
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <TS, T extends TS> TS wrap(T raw) {
        if (null == raw) {
            return null;
        }
        Class[] superInterfaces = raw.getClass().getInterfaces();
        return (TS) newProxyInstance(raw.getClass().getClassLoader(), superInterfaces, new MainLooperWrapper(raw));
    }

    /**
     * check target object whether is an main-looper proxy or not
     *
     * @param o target object
     * @return if target object is an async proxy, return true
     * @since 1.0.0
     */
    public static boolean isProxy(Object o) {
        return null != o && isProxyClass(o.getClass()) && getInvocationHandler(o) instanceof MainLooperWrapper;
    }

    private static class MainLooperWrapper implements Handler.Callback, InvocationHandler {
        private final Handler mHandler = new Handler(Looper.getMainLooper(), this);
        private final Object mRaw;

        MainLooperWrapper(Object raw) {
            mRaw = raw;
        }

        @Override
        public boolean handleMessage(Message msg) {
            try {
                Method method;
                Object args[] = null;
                if (msg.obj instanceof Method) {
                    method = (Method) msg.obj;
                } else {
                    Map<String, Object> data = (Map<String, Object>) msg.obj;
                    method = (Method) data.get("method");
                    args = (Object[]) data.get("args");
                }
                method.invoke(mRaw, args);
            } catch (Throwable e) {
                throw Exceptions.asRuntime(e);
            }
            return true;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                return method.invoke(mRaw, args);
            } else {
                Message msg = mHandler.obtainMessage(0);
                if (null != args && args.length > 0) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("method", method);
                    data.put("args", args);
                    msg.obj = data;
                } else {
                    msg.obj = method;
                }
                msg.sendToTarget();
                return Primitives.defValueOf(method.getReturnType());
            }
        }
    }
}
