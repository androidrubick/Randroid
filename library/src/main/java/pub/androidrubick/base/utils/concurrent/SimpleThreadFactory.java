package pub.androidrubick.base.utils.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 简单的{@link java.util.concurrent.ThreadFactory}
 *
 * <p/>
 * <p/>
 * Created by Yin Yong on 15/6/4.
 *
 * @since 1.0
 */
public class SimpleThreadFactory implements ThreadFactory {
    private final AtomicInteger mCount = new AtomicInteger(1);
    private String mName;
    public SimpleThreadFactory(String name) {
        mName = name;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, mName + " #" + mCount.getAndIncrement());
    }
}
