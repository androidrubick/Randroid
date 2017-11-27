package pub.androidrubick.base.cache.mem;

import java.util.LinkedList;

import pub.androidrubick.base.collection.CollectionsCompat;
import pub.androidrubick.base.utils.Objects;
import pub.androidrubick.base.utils.Recycleable;

/**
 * <p/>
 *
 * Created by Yin Yong on 2015/9/1.
 *
 * @since 1.0
 */
public abstract class InstancePool<E> implements Recycleable {

    private int mPoolSize;
    private final LinkedList<E> mPoolList;
    protected InstancePool(int poolSize) {
        this.mPoolSize = poolSize;
        this.mPoolList = new LinkedList<E>();
    }

    /**
     * obtain an instance
     *
     * @since 1.0
     */
    public E obtain() {
        synchronized (mPoolList) {
            if (!mPoolList.isEmpty()) {
                return mPoolList.removeFirst();
            }
            return create();
        }
    }

    /**
     * max pool size
     *
     * @since 1.0
     */
    public final int poolSize() {
        return mPoolSize;
    }

    /**
     * current pool size
     *
     * @since 1.0
     */
    public int size() {
        return CollectionsCompat.size(mPoolList);
    }

    /**
     * 回收对象，如果对象池没有达到上限（{@link #poolSize()}），将加入到对象池中
     *
     * @since 1.0
     */
    public void returnInstance(E e) {
        if (Objects.isNull(e)) {
            return;
        }
        synchronized (mPoolList) {
            if (mPoolList.size() < mPoolSize) {
                if (e instanceof Recycleable) {
                    Objects.getAs(e, Recycleable.class).recycle();
                }
                mPoolList.add(e);
            }
        }
    }

    /**
     * create a new instance
     *
     * @since 1.0
     */
    protected abstract E create();

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public void recycle() {
        if (!mPoolList.isEmpty()) {
            mPoolList.clear();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        recycle();
        super.finalize();
    }
}
