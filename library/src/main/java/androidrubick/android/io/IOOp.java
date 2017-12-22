package androidrubick.android.io;

import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import androidrubick.base.io.PredefinedBAOS;

/**
 * <p>
 * 该辅助类封装常用的IO操作，封装包含字符串、文件、原始的字节数组、流等之间的相互传递；
 * 该辅助类支持对IO操作获取进度，获取完成状态等；
 * 该辅助类支持异步——子线程进行IO操作，在主线程触发回调；
 * </p>
 * <p>
 * async 是指，创建新线程执行任务后，在主线程中回调；
 * </p>
 * <p>
 * sync 是指，在调用的当前线程执行任务，并在当前线程中回调
 * </p>
 *
 * <p></p>
 * Created by Yin Yong on 2017/11/24.
 *
 * @since 1.0.0
 */
public class IOOp extends IOOpBase<IOOp> {
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
     * do IO op in a new sub threads, callback in ui thread
     *
     * @since 1.0
     */
    public IOOpAsync async() {
        return new IOOpAsync(this, mFromType, mFromObj);
    }

    /**
     * transform src to {@link File}
     *
     * @since 1.0
     */
    public boolean to(final File file) {
        return checkDoOp(FILE, file);
    }

    /**
     * transform src to {@link OutputStream}
     *
     * @since 1.0
     */
    public boolean to(OutputStream out) {
        return checkDoOp(B_STREAM, out);
    }

    /**
     * transform src to {@link Writer}
     *
     * @since 1.0
     */
    public boolean to(Writer writer) {
        return checkDoOp(C_STREAM, writer);
    }

    /**
     * only support sync
     *
     * @return transform src to raw byte array
     * @since 1.0.0
     */
    public byte[] asRaw() {
        PredefinedBAOS baos = new PredefinedBAOS();
        to(baos);
        return baos.toByteArray();
    }

    /**
     * only support sync
     *
     * @return transform src to string
     * @since 1.0.0
     */
    public String asString() {
        StringWriter writer = new StringWriter();
        to(writer);
        return writer.toString();
    }

    private boolean checkDoOp(int toType, Object toObj) {
        if (mRecycleable) {
            return false;
        }
        mRecycleable = true;

        return new IOOpImpl(closeIn, closeOut, bufferSize, charset, cb)
                .sync(mFromType, mFromObj, toType, toObj);
    }

    // 该字段，必须内部赋值
    private final int mFromType;
    private final Object mFromObj;

    private volatile boolean mRecycleable;

    private IOOp(int fromType, Object fromObj) {
        this.mFromType = fromType;
        this.mFromObj = fromObj;
    }
}
