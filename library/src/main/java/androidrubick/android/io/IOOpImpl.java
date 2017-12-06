package androidrubick.android.io;

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
import androidrubick.android.async.AsyncHelper;

import static androidrubick.android.io.IOOp.B_STREAM;
import static androidrubick.android.io.IOOp.C_STREAM;
import static androidrubick.android.io.IOOp.FILE;

/**
 * <p></p>
 * Created by Yin Yong on 2017/11/24.
 *
 * @since 1.0.0
 */
/*package*/ class IOOpImpl {
    final boolean async;
    boolean closeIn;
    boolean closeOut;
    @IntRange(from = 1)
    final int bufferSize;
    final String charset;
    @Nullable
    IOCallback cb;

    IOOpImpl(boolean async,
             boolean closeIn, boolean closeOut,
             @IntRange(from = 1) int bufferSize,
             String charset,
             @Nullable IOCallback cb) {
        this.async = async;
        this.closeIn = closeIn;
        this.closeOut = closeOut;
        this.bufferSize = bufferSize;
        this.charset = charset;
        this.cb = cb;
    }

    public void doOp(int fromType, Object fromObj, int toType, Object toObj) {
        cb = async && null != cb ? (IOCallback) AsyncHelper.async(cb) : cb;

        Throwable err = null;
        if (fromType == FILE) {
            try {
                InputStream ins = new FileInputStream((File) fromObj);
                fromType = B_STREAM;
                fromObj = ins;
                closeIn = true;
            } catch (Throwable e) {
                err = e;
            }
        }
        if (toType == FILE) {
            try {
                OutputStream out = new FileOutputStream((File) toObj);
                toType = B_STREAM;
                toObj = out;
                closeOut = true;
            } catch (Throwable e) {
                err = e;
            }
        }

        // 如果有异常，直接回调并释放资源，并设置为不可再次调用
        if (null != err) {
            release(fromType, fromObj, closeIn, toType, toObj, closeOut);
            performError(err, 0, BufferType.Byte);
            return;
        }

        // 如果没有异常，则进行传输
        final int ft = fromType;
        final int tt = toType;
        final Object from = fromObj;
        final Object to = toObj;
        if (async) {
            ARSchedulers.io(new Runnable() {
                @Override
                public void run() {
                    trans(ft, from, tt, to);
                }
            });
        } else {
            trans(fromType, from, toType, to);
            release(fromType, fromObj, closeIn, toType, toObj, closeOut);
        }
    }

    private void release(int fromType, Object fromObj, boolean closeIn,
                         int toType, Object toObj, boolean closeOut) {
        if (toType == B_STREAM || toType == C_STREAM) {
            if (closeOut) {
                IOUtils.close((Closeable) toObj);
            }
        }
        if (fromType == B_STREAM || fromType == C_STREAM) {
            if (closeIn) {
                IOUtils.close((Closeable) fromObj);
            }
        }
    }

    private void trans(int fromType, Object fromObj, int toType, Object toObj) {
        try {
            switch ((fromType * 10) + toType) {
                case B_STREAM * 10 + B_STREAM:
                    i2o((InputStream) fromObj, (OutputStream) toObj);
                    break;
                case B_STREAM * 10 + C_STREAM:
                    i2w((InputStream) fromObj, (Writer) toObj);
                    break;
                case C_STREAM * 10 + B_STREAM:
                    r2o((Reader) fromObj, (OutputStream) toObj);
                    break;
                case C_STREAM * 10 + C_STREAM:
                    r2w((Reader) fromObj, (Writer) toObj);
                    break;
                default:
                    throw new IllegalArgumentException("invalid fromType or toType");
            }
        } catch (Throwable e) {
            performError(e, 0, BufferType.Byte);
        }
    }

    private void r2w(Reader reader, Writer writer) {
        long readTotal = 0;
        try {
            char[] buf = new char[bufferSize];
            int len;
            while (-1 != (len = reader.read(buf))) {
                writer.write(buf, 0, len);
                readTotal += len;
                performProgress(len, readTotal, BufferType.Char);
            }
            performComplete(readTotal, BufferType.Char);
        } catch (Throwable e) {
            performError(e, readTotal, BufferType.Char);
        } finally {
            if (closeOut) {
                IOUtils.close(writer);
            }
            if (closeIn) {
                IOUtils.close(reader);
            }
        }
    }

    private void i2w(InputStream inputStream, Writer writer) {
        try {
            r2w(new InputStreamReader(inputStream, charset), writer);
        } catch (Throwable e) {
            performError(e, 0, BufferType.Char);
        } finally {
            if (closeOut) {
                IOUtils.close(writer);
            }
            if (closeIn) {
                IOUtils.close(inputStream);
            }
        }
    }

    private void r2o(Reader reader, OutputStream outputStream) {
        try {
            r2w(reader, new OutputStreamWriter(outputStream, charset));
        } catch (Throwable e) {
            performError(e, 0, BufferType.Char);
        } finally {
            if (closeOut) {
                IOUtils.close(outputStream);
            }
            if (closeIn) {
                IOUtils.close(reader);
            }
        }
    }

    private void i2o(InputStream inputStream, OutputStream outputStream) {
        long readTotal = 0;
        try {
            byte[] buf = new byte[bufferSize];
            int len;
            while (-1 != (len = inputStream.read(buf))) {
                outputStream.write(buf, 0, len);
                readTotal += len;
                performProgress(len, readTotal, BufferType.Byte);
            }
            performComplete(readTotal, BufferType.Byte);
        } catch (Throwable e) {
            performError(e, readTotal, BufferType.Byte);
        } finally {
            if (closeOut) {
                IOUtils.close(outputStream);
            }
            if (closeIn) {
                IOUtils.close(inputStream);
            }
        }
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
