package pub.androidrubick.base.utils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p/>
 * <p/>
 * Created by Yin Yong on 15/5/25.
 *
 * @since 1.0
 */
public class IfSetter<T> {

    private AtomicBoolean ifSet = new AtomicBoolean(false);
    private T value;

    /**
     * 设值
     *
     * @since 1.0
     */
    public void set(T value) {
        ifSet.set(true);
        this.value = value;
    }

    /**
     * 清除设值状态
     *
     * @since 1.0
     */
    public void clear() {
        ifSet.set(false);
        this.value = null;
    }

    /**
     * 获取当前设值
     *
     * @since 1.0
     */
    public T get() {
        return this.value;
    }

    /**
     * 是否已经设值
     *
     * @since 1.0
     */
    public boolean isSet() {
        return this.ifSet.get();
    }

}
