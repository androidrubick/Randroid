package pub.androidrubick.base.utils.retrypolocy;

import pub.androidrubick.base.utils.MathPreconditions;

/**
 *
 * 当执行某次操作失败时，调用{@link #retry(Throwable)}，
 * 如果没有达到指定的重试执行次数，后续的代码将执行，否则直接抛出参数中的异常。
 *
 * <p/>
 *
 * <pre>
 *     RetryPolicy rp = new SimpleRetryPolicy(n);
 *     ...
 *     void someMethod() throw XException {
 *         while (true) {
 *             try {
 *                 // do core operations
 *                 ...
 *             } catch(XException ex) {
 *                 rp.retry(ex); // throw ex if no more retry times
 *                 // do next loop
 *                 continue;
 *             }
 *             break;
 *         }
 *     }
 *
 * </pre>
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/8/31.
 *
 * @since 1.0
 */
public class SimpleRetryPolicy implements RetryPolicy {

    private final int mRetryCount;
    /** The current retry count. */
    private int mCurrentRetryCount;
    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    public SimpleRetryPolicy(int retryCount) {
        mRetryCount = MathPreconditions.checkNoLessThanMin("retryCount", retryCount, 0);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public boolean hasAttemptRemaining() {
        return mCurrentRetryCount <= mRetryCount;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public <Ex extends Throwable> void retry(Ex error) throws Ex {
        mCurrentRetryCount ++;
        if (!hasAttemptRemaining()) {
            throw error;
        }
    }
}
