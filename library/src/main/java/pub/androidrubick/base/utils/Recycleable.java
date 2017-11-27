package pub.androidrubick.base.utils;

/**
 * 如果对象是可以回收的，提供{@link #recycle()}的实现。
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/9/1.
 *
 * @since 1.0
 */
public interface Recycleable {

    /**
     * 方法中回收该对象中的一些引用。
     *
     * @since 1.0
     */
    void recycle();

}
