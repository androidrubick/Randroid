package androidrubick.android.io.op;

import android.support.annotation.NonNull;

import androidrubick.android.io.BufferType;

/**
 * IO进度的回调
 * <p>
 * <p/>
 * Created by Yin Yong on 15/7/21.
 *
 * @since 1.0
 */
public interface IOCallback {

    /**
     * 中间I/O操作的进度回调方法
     *
     * @param readThisTime 上次回调结束到本地回调读取/写入的字节/字符数
     * @param readTotal    从开始到本次回调一共读取/写入的字节/字符数
     * @param bt           buffer type, byte or char
     * @since 1.0
     */
    void onProgress(long readThisTime, long readTotal, @NonNull BufferType bt);

    /**
     * I/O操作结束时的回调
     *
     * @param readTotal 一共读取/写入的字节/字符数
     * @param bt        buffer type, byte or char
     * @since 1.0
     */
    void onComplete(long readTotal, @NonNull BufferType bt);

    /**
     * I/O操作发生错误的回调
     *
     * @param e         错误信息
     * @param readTotal 一共读取/写入的字节/字符数
     * @param bt        buffer type, byte or char
     * @since 1.0
     */
    void onFailed(Throwable e, long readTotal, @NonNull BufferType bt);
}
