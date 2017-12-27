package androidrubick.android.io.op;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.nio.charset.Charset;

import androidrubick.base.io.IOConstants;
import androidrubick.base.utils.Exceptions;

/**
 * base settings
 *
 * <p></p>
 * Created by Yin Yong on 2017/12/12.
 *
 * @since 1.0.0
 */
/*package*/ abstract class IOOpBase<Self extends IOOpBase> {

    private Self self() {
        return (Self) this;
    }

    /**
     * default value is {@code true}
     *
     * @param val whether close input stream / reader
     * @since 1.0
     */
    public final Self closeIn(boolean val) {
        closeIn = val;
        return self();
    }

    /**
     * default value is {@code true}
     *
     * @param val whether close output stream / writer
     * @since 1.0
     */
    public final Self closeOut(boolean val) {
        closeOut = val;
        return self();
    }

    /**
     * @param cb progress and result callback
     * @since 1.0
     */
    public final Self callback(IOCallback cb) {
        this.cb = cb;
        return self();
    }

    /**
     * buffer size used when transforming;
     *
     * default size is {@link IOConstants#DEF_BUFFER_SIZE}
     *
     * @since 1.0
     */
    public final Self bufferSize(@IntRange(from = 1) int bufferSize) {
        this.bufferSize = bufferSize;
        return self();
    }

    /**
     * char set of char-related operations;
     *
     * default charset is {@link IOConstants#DEF_CHARSET_NAME}
     *
     * @since 1.0
     */
    public final Self charset(String charset) {
        try {
            Charset.forName(charset);
            this.charset = charset;
        } catch (Exception ignore) {
            throw Exceptions.asRuntime(ignore);
        }
        return self();
    }

    static final int FILE     = 1;
    // byte stream
    static final int B_STREAM = 2;
    // char stream
    static final int C_STREAM = 3;

    int bufferSize = IOConstants.DEF_BUFFER_SIZE;
    String charset = IOConstants.DEF_CHARSET_NAME;
    boolean closeIn = true;
    boolean closeOut = true;
    IOCallback cb;

    IOOpBase() {

    }

    IOOpBase(@NonNull IOOpBase<?> raw) {
        bufferSize = raw.bufferSize;
        charset = raw.charset;
        closeIn = raw.closeIn;
        closeOut = raw.closeOut;
        cb = raw.cb;
    }
}
