package androidrubick.android.io;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;

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
