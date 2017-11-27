package pub.androidrubick.base.utils.concurrent;

import java.util.LinkedList;
import java.util.concurrent.Executor;

/**
 *
 * 有序地、线性地执行任务，真正执行的{@link java.util.concurrent.Executor}为
 *
 * 构造中传入的参数。
 *
 * <p/>
 * <p/>
 * Created by Yin Yong on 15/6/4.
 *
 * @since 1.0
 */
public class SerialExecutor extends AbsExecutorWrapper {

    final LinkedList<Runnable> mTasks = new LinkedList<Runnable>();
    Runnable mActive;

    public SerialExecutor(Executor base) {
        super(base);
    }

    /**
     * 执行{@link Runnable}
     *
     * @since 1.0
     */
    public synchronized void execute(final Runnable r) {
        mTasks.offer(new Runnable() {
            public void run() {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            }
        });
        if (mActive == null) {
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if ((mActive = mTasks.poll()) != null) {
            getExecutor().execute(mActive);
        }
    }
}
