package pub.androidrubick.base.utils.concurrent;

import java.util.concurrent.Executor;

import pub.androidrubick.base.utils.Preconditions;

/**
 *
 * 包装另一个{@link java.util.concurrent.Executor}
 *
 * <p/>
 * <p/>
 * Created by Yin Yong on 15/6/4.
 *
 * @since 1.0
 */
public abstract class AbsExecutorWrapper implements Executor {

    private Executor mBase;
    protected AbsExecutorWrapper(Executor base) {
        this.mBase = Preconditions.checkNotNull(base, "base Executor can not be null");
    }

    /**
     * @return base {@link Executor}
     *
     * @since 1.0
     */
    public Executor getExecutor() {
        return mBase;
    }

}
