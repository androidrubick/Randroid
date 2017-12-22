package androidrubick.android.io;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/12/13.
 *
 * @since 1.0.0
 */
public class IOOpAsync extends IOOpBase<IOOpAsync> {

    // 该字段，必须内部赋值
    private final int mFromType;
    private final Object mFromObj;

    private volatile boolean mRecycleable;
    /*package*/ IOOpAsync(@NonNull IOOpBase<?> raw, int fromType, Object fromObj) {
        super(raw);
        mFromType = fromType;
        mFromObj = fromObj;
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

    private void checkDoOp(int toType, Object toObj) {
        if (mRecycleable) {
            return ;
        }
        mRecycleable = true;

        new IOOpImpl(closeIn, closeOut, bufferSize, charset, cb)
                .async(mFromType, mFromObj, toType, toObj);
    }
}
