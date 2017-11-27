package pub.androidrubick.base.utils;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 *
 * 包装另一个非运行时异常，将之转为运行时异常；
 *
 * 此外，该类也给已抛出的异常增加更多的操作，而不改变该异常的栈信息。
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/9/22.
 *
 * @since 1.0
 */
public class DummyRuntimeException extends RuntimeException {

    private Throwable mCause;
    public DummyRuntimeException(Throwable throwable) {
        super();
        mCause = Preconditions.checkNotNull(throwable, "throwable");
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return mCause.getStackTrace();
    }

    @Override
    public void setStackTrace(StackTraceElement[] trace) {
        mCause.setStackTrace(trace);
    }

    @Override
    public void printStackTrace() {
        mCause.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream err) {
        mCause.printStackTrace(err);
    }

    @Override
    public void printStackTrace(PrintWriter err) {
        mCause.printStackTrace(err);
    }

    @Override
    public String getMessage() {
        return mCause.getMessage();
    }

    @Override
    public Throwable getCause() {
        return mCause.getCause();
    }

    /**
     * no fill
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
