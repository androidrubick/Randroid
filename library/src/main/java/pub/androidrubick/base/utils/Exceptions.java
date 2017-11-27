package pub.androidrubick.base.utils;

/**
 * <p/>
 * <p/>
 * Created by Yin Yong on 15/7/2.
 *
 * @since 1.0
 */
public class Exceptions {
    private Exceptions() { }

    public static RuntimeException asRuntime(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new DummyRuntimeException(e);
    }

    public static RuntimeException asRuntime(String msg) {
        return new RuntimeException(msg);
    }

}
