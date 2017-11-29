package androidrubick.android.io;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;

import static androidrubick.android.utils.AndroidUtils.isMainThread;

/**
 *
 * 多文件操作的回调，实现异步回调到主线程
 *
 * <p/>
 * <p/>
 * Created by Yin Yong on 15/8/24.
 *
 * @since 1.0
 */
public final class FileIOCallback_Async implements FileIOCallback, Handler.Callback {

    private final Handler mHandler = new Handler(Looper.getMainLooper(), this);
    private final FileIOCallback mCb;
    public FileIOCallback_Async(FileIOCallback cb) {
        mCb = cb;
    }

    @Override
    public final void onProgress(File file, boolean success) {
        if (null != mCb) {
            if (isMainThread()) {
                mCb.onProgress(file, success);
            } else {
                mHandler.obtainMessage(0, success ? 1 : 0, 0, file).sendToTarget();
            }
        }
    }

    @Override
    public final void onComplete() {
        if (null != mCb) {
            if (isMainThread()) {
                mCb.onComplete();
            } else {
                mHandler.obtainMessage(1).sendToTarget();
            }
        }
    }

    @Override
    public final boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                mCb.onProgress((File) msg.obj, msg.arg1 > 0);
                break;
            case 1:
                mCb.onComplete();
                break;
        }
        return true;
    }
}
