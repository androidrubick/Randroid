package androidrubick.android.io;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import androidrubick.base.io.IOConstants;
import androidrubick.base.io.PredefinedBAOS;
import androidrubick.base.utils.DummyRuntimeException;

/**
 *
 * <p>
 *     该辅助类封装常用的IO操作，封装包含字符串、文件、原始的字节数组、流等之间的相互传递；
 *     该辅助类支持对IO操作获取进度，获取完成状态等；
 *     该辅助类支持异步，子线程进行IO操作，在主线程触发回调；
 * </p>
 *
 * <p>
 * async 是指，创建新线程执行任务后，在主线程中回调；
 *
 * sync 是指，在调用的当前线程执行任务，并在当前线程中回调
 * </p>
 *
 * <p></p>
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
     * do IO op in sub threads, callback in ui thread
     *
     * @since 1.0
     */
    public IOOp async() {
        mAsync = true;
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
        checkDoOp(FILE, file);
    }

    /**
     * transform src to {@link OutputStream}
     *
     * @since 1.0
     */
    public void to(OutputStream out) {
        checkDoOp(B_STREAM, out);
    }

    /**
     * transform src to {@link Writer}
     *
     * @since 1.0
     */
    public void to(Writer writer) {
        checkDoOp(C_STREAM, writer);
    }

    /**
     * only support sync
     *
     * @return transform src to raw byte array
     * @since 1.0.0
     */
    public byte[] asRaw() {
        PredefinedBAOS baos = new PredefinedBAOS();
        boolean async = mAsync;
        try {
            mAsync = false;
            to(baos);
            return baos.toByteArray();
        } finally {
            if (async) {
                async();
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
        boolean async = mAsync;
        try {
            mAsync = false;
            to(writer);
            return writer.toString();
        } finally {
            if (async) {
                async();
            }
        }
    }

    private void checkDoOp(int toType, Object toObj) {
        if (mRecycleable) {
            return;
        }
        mRecycleable = true;

        new IOOpImpl(mAsync, mCloseIn, mCloseOut, mBufferSize, mCharset, mCb)
                .doOp(mFromType, mFromObj, toType, toObj);
    }

    static final int FILE     = 1;
    // byte stream
    static final int B_STREAM = 2;
    // char stream
    static final int C_STREAM = 3;

    private int mBufferSize = IOConstants.DEF_BUFFER_SIZE;
    private String mCharset = IOConstants.DEF_CHARSET_NAME;
    private boolean mCloseIn = true;
    private boolean mCloseOut = true;
    private IOCallback mCb;

    private boolean mAsync = false;

    // 该字段，必须内部赋值
    private int mFromType;
    private Object mFromObj;

    private volatile boolean mRecycleable;

    private IOOp(int fromType, Object fromObj) {
        this.mFromType = fromType;
        this.mFromObj = fromObj;
    }
}
