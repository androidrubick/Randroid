package androidrubick.android.io;

import android.support.annotation.NonNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/11/24.
 *
 * @since 1.0.0
 */
/*package*/ interface IOTrans<From, To> {

    void trans(From from, To to, @NonNull IOOp.Ops ops) ;

}

abstract class BaseIOTrans<From, To> implements IOTrans<From, To> {

    static void performProgress(IOCallback cb, long readThisTime, long readTotal, BufferType type) {
        if (null != cb) {
            cb.onProgress(readThisTime, readTotal, type);
        }
    }

    static void performError(IOCallback cb, Throwable e, long readTotal, BufferType type) {
        if (null != cb) {
            cb.onFailed(e, readTotal, type);
        }
    }

    static void performComplete(IOCallback cb, long readTotal, BufferType type) {
        if (null != cb) {
            cb.onComplete(readTotal, type);
        }
    }
}

class Dummy<From, To> extends BaseIOTrans<From, To> {
    @Override
    public void trans(From from, To to, @NonNull IOOp.Ops ops) {
        performComplete(ops.cb, 0, BufferType.Byte);
    }
}

class R2W extends BaseIOTrans<Reader, Writer> {

    @Override
    public void trans(Reader reader, Writer writer, @NonNull IOOp.Ops ops) {
        long readTotal = 0;
        try {
            char[] buf = new char[ops.bufferSize];
            int len;
            while (-1 != (len = reader.read(buf))) {
                writer.write(buf, 0, len);
                readTotal += len;
                performProgress(ops.cb, len, readTotal, BufferType.Char);
            }
            performComplete(ops.cb, readTotal, BufferType.Char);
        } catch (Throwable e) {
            performError(ops.cb, e, readTotal, BufferType.Char);
        } finally {
            if (ops.closeOut) {
                IOUtils.close(writer);
            }
            if (ops.closeIn) {
                IOUtils.close(reader);
            }
        }
    }
}

class I2W extends BaseIOTrans<InputStream, Writer> {
    @Override
    public void trans(InputStream inputStream, Writer writer, @NonNull IOOp.Ops ops) {
        try {
            new R2W().trans(new InputStreamReader(inputStream, ops.charset), writer, ops);
        } catch (Throwable e) {
            performError(ops.cb, e, 0, BufferType.Char);
        } finally {
            if (ops.closeOut) {
                IOUtils.close(writer);
            }
            if (ops.closeIn) {
                IOUtils.close(inputStream);
            }
        }
    }
}

class R2O extends BaseIOTrans<Reader, OutputStream> {
    @Override
    public void trans(Reader reader, OutputStream outputStream, @NonNull IOOp.Ops ops) {
        try {
            new R2W().trans(reader, new OutputStreamWriter(outputStream, ops.charset), ops);
        } catch (Throwable e) {
            performError(ops.cb, e, 0, BufferType.Char);
        } finally {
            if (ops.closeOut) {
                IOUtils.close(outputStream);
            }
            if (ops.closeIn) {
                IOUtils.close(reader);
            }
        }
    }
}

class I2O extends BaseIOTrans<InputStream, OutputStream> {
    @Override
    public void trans(InputStream inputStream, OutputStream outputStream, @NonNull IOOp.Ops ops) {
        long readTotal = 0;
        try {
            byte[] buf = new byte[ops.bufferSize];
            int len;
            while (-1 != (len = inputStream.read(buf))) {
                outputStream.write(buf, 0, len);
                readTotal += len;
                performProgress(ops.cb, len, readTotal, BufferType.Byte);
            }
            performComplete(ops.cb, readTotal, BufferType.Byte);
        } catch (Throwable e) {
            performError(ops.cb, e, readTotal, BufferType.Byte);
        } finally {
            if (ops.closeOut) {
                IOUtils.close(outputStream);
            }
            if (ops.closeIn) {
                IOUtils.close(inputStream);
            }
        }
    }
}
