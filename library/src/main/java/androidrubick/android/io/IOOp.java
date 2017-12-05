package androidrubick.android.io;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import androidrubick.android.async.ARSchedulers;
import androidrubick.android.async.AsyncHelper;
import androidrubick.base.io.IOConstants;
import androidrubick.base.io.PredefinedBAOS;
import androidrubick.base.utils.DummyRuntimeException;

import static androidrubick.android.io.BaseIOTrans.performError;

/**
 *
 * subThread 是指，创建新线程执行任务后，在主线程中回调；
 *
 * sync 是指，在调用的当前线程执行任务，并在主线程中回调
 *
 * <p>
 * Created by Yin Yong on 2017/11/24.
 *
 * @since 1.0.0
 */
public class IOOp {
    /**
     * @since 1.0
     */
    @NonNull
    public static IOOp from(File file) {
        return new IOOp(FILE, file);
    }

    /**
     * @since 1.0
     */
    @NonNull
    public static IOOp fromContent(String content) {
        return from(null == content ? null : new StringReader(content));
    }

    /**
     * @since 1.0
     */
    @NonNull
    public static IOOp from(byte[] data) {
        return from(null == data ? null : new ByteArrayInputStream(data));
    }

    /**
     * @since 1.0
     */
    @NonNull
    public static IOOp from(InputStream ins) {
        return new IOOp(B_STREAM, ins);
    }

    /**
     * @since 1.0
     */
    @NonNull
    public static IOOp from(Reader reader) {
        return new IOOp(C_STREAM, reader);
    }

    /**
     * @param val whether close input stream / reader
     * @since 1.0
     */
    public IOOp closeIn(boolean val) {
        mCloseIn = val;
        return this;
    }

    /**
     * @param val whether close output stream / writer
     * @since 1.0
     */
    public IOOp closeOut(boolean val) {
        mCloseOut = val;
        return this;
    }

    /**
     * @param cb progress and result callback
     * @since 1.0
     */
    public IOOp callback(IOCallback cb) {
        mCb = cb;
        return this;
    }

    /**
     * do IO subThread in io thread (however, callback in main UI thread)
     *
     * @since 1.0
     */
    public IOOp subThread() {
        mSubThread = true;
        return this;
    }

    /**
     * buffer size used when transforming
     *
     * @since 1.0
     */
    public IOOp bufferSize(@IntRange(from = 1) int bufferSize) {
        mBufferSize = bufferSize;
        return this;
    }

    /**
     * char set of char-related operations
     *
     * @since 1.0
     */
    public IOOp charset(String charset) {
        try {
            Charset.forName(charset);
            mCharset = charset;
        } catch (Exception ignore) {
            throw new DummyRuntimeException(ignore);
        }
        return this;
    }

    /**
     * transform src to {@link File}
     *
     * @since 1.0
     */
    public void to(final File file) {
        if (mRecycleable) {
            return;
        }
        mRecycleable = true;
        mToType = FILE;
        mToObj = file;
        checkTrans();
    }

    /**
     * transform src to {@link OutputStream}
     *
     * @since 1.0
     */
    public void to(OutputStream out) {
        if (mRecycleable) {
            return;
        }
        mRecycleable = true;
        mToType = B_STREAM;
        mToObj = out;
        checkTrans();
    }

    /**
     * transform src to {@link Writer}
     *
     * @since 1.0
     */
    public void to(Writer writer) {
        if (mRecycleable) {
            return;
        }
        mRecycleable = true;
        mToType = C_STREAM;
        mToObj = writer;
        checkTrans();
    }

    /**
     * only support sync
     *
     * @return transform src to raw byte array
     * @since 1.0.0
     */
    public byte[] asRaw() {
        PredefinedBAOS baos = new PredefinedBAOS();
        boolean subThread = mSubThread;
        try {
            mSubThread = false;
            to(baos);
            return baos.toByteArray();
        } finally {
            if (subThread) {
                subThread();
            }
        }
    }

    /**
     * only support sync
     *
     * @return transform src to string
     * @since 1.0.0
     */
    public String asString() {
        StringWriter writer = new StringWriter();
        boolean subThread = mSubThread;
        try {
            mSubThread = false;
            to(writer);
            return writer.toString();
        } finally {
            if (subThread) {
                subThread();
            }
        }
    }

    private void checkTrans() {
        final boolean subThread = mSubThread;
//        final IOCallback callback = subThread && null != mCb ? new IOCallback_Async(mCb) : mCb;
        final IOCallback callback = subThread && null != mCb ? (IOCallback) AsyncHelper.async(mCb) : mCb;

        Throwable err = transFileType();
        // 如果有异常，直接回调并释放资源，并设置为不可再次调用
        if (null != err) {
            release();
            performError(callback, err, 0, BufferType.Byte);
            return;
        }

        // 如果没有异常，则进行传输
        final Object from = mFromObj;
        final Object to = mToObj;
        final Ops ops = new Ops(mForceCloseIn || mCloseIn, mForceCloseOut || mCloseOut,
                mBufferSize, mCharset, callback);
        final IOTrans ioTrans = getIOTrans(mFromType, mToType);
        if (subThread) {
            ARSchedulers.io(new Runnable() {
                @Override
                public void run() {
                    ioTrans.trans(from, to, ops);
                }
            });
        } else {
            ioTrans.trans(from, to, ops);
            release();
        }
    }

    /**
     * 如果返回不为null，即有异常，直接回调异常方法；
     * <p>
     * 如果返回null，则继续进行后续的IO操作
     */
    private Throwable transFileType() {
        if (mFromType == FILE) {
            try {
                InputStream ins = new FileInputStream((File) mFromObj);
                mFromType = B_STREAM;
                mFromObj = ins;
                mForceCloseIn = true;
            } catch (Throwable e) {
                return e;
            }
        }
        if (mToType == FILE) {
            try {
                OutputStream out = new FileOutputStream((File) mToObj);
                mToType = B_STREAM;
                mToObj = out;
                mForceCloseOut = true;
            } catch (Throwable e) {
                return e;
            }
        }
        return null;
    }

    private void release() {
        if (mToType == B_STREAM || mToType == C_STREAM) {
            if (mForceCloseOut || mCloseOut) {
                IOUtils.close((Closeable) mToObj);
            }
        }
        if (mFromType == B_STREAM || mFromType == C_STREAM) {
            if (mForceCloseIn || mCloseIn) {
                IOUtils.close((Closeable) mFromObj);
            }
        }
    }

    private static IOTrans getIOTrans(int fromType, int toType) {
        switch ((fromType * 10) + toType) {
            case B_STREAM * 10 + B_STREAM:
                return new I2O();
            case B_STREAM * 10 + C_STREAM:
                return new I2W();
            case C_STREAM * 10 + B_STREAM:
                return new R2O();
            case C_STREAM * 10 + C_STREAM:
                return new R2W();
        }
        return new Dummy();
    }

    /*package*/ static class Ops {
        final boolean closeIn;
        final boolean closeOut;
        @IntRange(from = 1)
        final int bufferSize;
        final String charset;
        @Nullable
        final IOCallback cb;

        Ops(boolean closeIn, boolean closeOut,
            @IntRange(from = 1) int bufferSize,
            String charset,
            @Nullable IOCallback cb) {
            this.closeIn = closeIn;
            this.closeOut = closeOut;
            this.bufferSize = bufferSize;
            this.charset = charset;
            this.cb = cb;
        }
    }

    private static final int FILE     = 1;
    // byte stream
    private static final int B_STREAM = 2;
    // char stream
    private static final int C_STREAM = 3;

    private int mBufferSize = IOConstants.DEF_BUFFER_SIZE;
    private String mCharset = IOConstants.DEF_CHARSET_NAME;
    private boolean mCloseIn = true;
    private boolean mCloseOut = true;
    private boolean mForceCloseIn = false;
    private boolean mForceCloseOut = false;
    private IOCallback mCb;

    private boolean mSubThread = false;

    // 该字段，必须内部赋值
    private int mFromType;
    private Object mFromObj;
    private int mToType;
    private Object mToObj;

    private volatile boolean mRecycleable;

    private IOOp(int fromType, Object fromObj) {
        this.mFromType = fromType;
        this.mFromObj = fromObj;
    }
}
