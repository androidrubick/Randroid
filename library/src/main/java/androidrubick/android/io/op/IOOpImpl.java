package androidrubick.android.io.op;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import androidrubick.android.async.ARSchedulers;
import androidrubick.android.io.BufferType;
import androidrubick.android.io.IOUtils;

import static androidrubick.android.async.MainLooperProxy.isProxy;
import static androidrubick.android.async.MainLooperProxy.wrap;

/**
 * <p></p>
 * Created by Yin Yong on 2017/11/24.
 *
 * @since 1.0.0
 */
/*package*/ class IOOpImpl {
    private boolean closeIn;
    private boolean closeOut;
    @IntRange(from = 1)
    private final int bufferSize;
    private final String charset;
    @Nullable
    private IOCallback cb;

    IOOpImpl(boolean closeIn, boolean closeOut,
             @IntRange(from = 1) int bufferSize,
             String charset,
             @Nullable IOCallback cb) {
        this.closeIn = closeIn;
        this.closeOut = closeOut;
        this.bufferSize = bufferSize;
        this.charset = charset;
        this.cb = cb;
    }

    boolean sync(int fromType, Object fromObj, int toType, Object toObj) {
        Throwable err = null;
        if (fromType == IOOpBase.FILE) {
            try {
                InputStream ins = new FileInputStream((File) fromObj);
                fromType = IOOpBase.B_STREAM;
                fromObj = ins;
                closeIn = true;
            } catch (Throwable e) {
                err = e;
            }
        }
        if (toType == IOOpBase.FILE) {
            try {
                OutputStream out = new FileOutputStream((File) toObj);
                toType = IOOpBase.B_STREAM;
                toObj = out;
                closeOut = true;
            } catch (Throwable e) {
                err = e;
            }
        }

        // 如果没有异常，进行IO传输
        if (null == err) {
            return trans(fromType, fromObj, toType, toObj);
        }

        // 如果有异常，直接回调并释放资源，并设置为不可再次调用
        release(fromType, fromObj, toType, toObj);
        performError(err, 0, BufferType.Byte);
        return false;
    }

    void async(final int fromType, final Object fromObj, final int toType, final Object toObj) {
        if (!isProxy(this.cb)) {
            this.cb = null != cb ? (IOCallback) wrap(cb) : cb;
        }
        ARSchedulers.io(new Runnable() {
            @Override
            public void run() {
                sync(fromType, fromObj, toType, toObj);
            }
        });
    }

    private void release(int fromType, Object fromObj, int toType, Object toObj) {
        if (toType == IOOpBase.B_STREAM || toType == IOOpBase.C_STREAM) {
            if (closeOut && toObj instanceof Closeable) {
                IOUtils.close((Closeable) toObj);
            }
        }
        if (fromType == IOOpBase.B_STREAM || fromType == IOOpBase.C_STREAM) {
            if (closeIn && fromObj instanceof Closeable) {
                IOUtils.close((Closeable) fromObj);
            }
        }
    }

    private boolean trans(int fromType, Object fromObj, int toType, Object toObj) {
        switch ((fromType * 10) + toType) {
            case IOOpBase.B_STREAM * 10 + IOOpBase.B_STREAM:
                return i2o((InputStream) fromObj, (OutputStream) toObj);
            case IOOpBase.B_STREAM * 10 + IOOpBase.C_STREAM:
                return i2w((InputStream) fromObj, (Writer) toObj);
            case IOOpBase.C_STREAM * 10 + IOOpBase.B_STREAM:
                return r2o((Reader) fromObj, (OutputStream) toObj);
            case IOOpBase.C_STREAM * 10 + IOOpBase.C_STREAM:
                return r2w((Reader) fromObj, (Writer) toObj);
            default: {
                release(fromType, fromObj, toType, toObj);
                performError(new IllegalArgumentException("Invalid fromType or toType"), 0, BufferType.Byte);
                return false;
            }
        }
    }

    private boolean r2w(Reader reader, Writer writer) {
        long readTotal = 0;
        try {
            char[] buf = new char[bufferSize];
            int len;
            while (-1 != (len = reader.read(buf))) {
                writer.write(buf, 0, len);
                readTotal += len;
                performProgress(len, readTotal, BufferType.Char);
            }
        } catch (Throwable e) {
            performError(e, readTotal, BufferType.Char);
            return false;
        } finally {
            if (closeOut) {
                IOUtils.close(writer);
            }
            if (closeIn) {
                IOUtils.close(reader);
            }
        }
        performComplete(readTotal, BufferType.Char);
        return true;
    }

    private boolean i2w(InputStream inputStream, Writer writer) {
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(inputStream, charset);
        } catch (Throwable e) {
            if (closeOut) {
                IOUtils.close(writer);
            }
            if (closeIn) {
                IOUtils.close(inputStream);
            }
            performError(e, 0, BufferType.Char);
            return false;
        }
        return r2w(reader, writer);
    }

    private boolean r2o(Reader reader, OutputStream outputStream) {
        OutputStreamWriter writer;
        try {
            writer = new OutputStreamWriter(outputStream, charset);
        } catch (Throwable e) {
            if (closeOut) {
                IOUtils.close(outputStream);
            }
            if (closeIn) {
                IOUtils.close(reader);
            }
            performError(e, 0, BufferType.Char);
            return false;
        }
        return r2w(reader, writer);
    }

    private boolean i2o(InputStream inputStream, OutputStream outputStream) {
        long readTotal = 0;
        try {
            byte[] buf = new byte[bufferSize];
            int len;
            while (-1 != (len = inputStream.read(buf))) {
                outputStream.write(buf, 0, len);
                readTotal += len;
                performProgress(len, readTotal, BufferType.Byte);
            }
        } catch (Throwable e) {
            performError(e, readTotal, BufferType.Byte);
            return false;
        } finally {
            if (closeOut) {
                IOUtils.close(outputStream);
            }
            if (closeIn) {
                IOUtils.close(inputStream);
            }
        }
        performComplete(readTotal, BufferType.Byte);
        return true;
    }

    private void performProgress(long readThisTime, long readTotal, BufferType type) {
        if (null != cb) {
            cb.onProgress(readThisTime, readTotal, type);
        }
    }

    private void performError(Throwable e, long readTotal, BufferType type) {
        if (null != cb) {
            cb.onFailed(e, readTotal, type);
        }
    }

    private void performComplete(long readTotal, BufferType type) {
        if (null != cb) {
            cb.onComplete(readTotal, type);
        }
    }
}
