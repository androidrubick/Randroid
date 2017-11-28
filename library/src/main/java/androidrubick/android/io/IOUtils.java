package androidrubick.android.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import static androidrubick.base.utils.Objects.getAs;

/**
 * 工具类，封装简单的I/O转换操作
 *
 * @author Yin Yong
 *
 * @since 1.0
 *
 */
public class IOUtils {
    private IOUtils() { }

    /**
     * @since 1.0
     */
    public static IOOp from(File file) {
        return new IOOp(file);
    }

    /**
     * @since 1.0
     */
    public static IOOp fromContent(String content) {
        return new IOOp(content);
    }

    /**
     * @since 1.0
     */
    public static IOOp from(byte[] data) {
        return new IOOp(data);
    }

    /**
     * @since 1.0
     */
    public static IOOp from(InputStream ins) {
        return new IOOp(ins);
    }

    /**
     * @since 1.0
     */
    public static IOOp from(Reader reader) {
        return new IOOp(reader);
    }

    // >>>>>>>>>>>>>>>>>>>>>>
    // TODO close
    /**
     * 关闭指定的可关闭的I/O
     * @param close 一个实现了Closeable的I/O对象
     *
     * @since 1.0
     */
    public static void close(Closeable close) {
        if (null == close) {
            return ;
        }
        try {
            if (close instanceof Flushable) {
                getAs(close, Flushable.class).flush();
            }
        } catch (IOException ignore) { }
        try {
            if (close instanceof FileOutputStream) {
                getAs(close, FileOutputStream.class).getFD().sync();
            }
        } catch (IOException ignore) { }
        try {
            close.close();
        } catch (IOException ignore) { }
    }
}
