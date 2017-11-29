package androidrubick.android.io;

import java.io.File;

/**
 *
 * 多文件操作的回调
 *
 * <p/>
 * <p/>
 * Created by Yin Yong on 15/8/24.
 *
 * @since 1.0
 */
public interface FileIOCallback {

    /**
     * 操作进度的回调
     *
     * @param file 当前回调操作的文件
     * @param success 是否操作成功
     *
     * @since 1.0
     */
    void onProgress(File file, boolean success);

    /**
     * 文件操作结束时的回调
     *
     * @since 1.0
     */
    void onComplete();

}
