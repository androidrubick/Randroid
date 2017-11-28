package androidrubick.android.bitmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

import androidrubick.android.bitmap.loader.BitmapLoader;
import androidrubick.android.device.DeviceInfos;
import androidrubick.android.io.Files;
import androidrubick.base.utils.MathCompat;

/**
 *
 * 工具类，用于{@link Bitmap}相关的操作
 *
 * <p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 2.3.0
 */
public class Bitmaps {

    /**
     * 尝试创建{@link Bitmap}，如果发生OOM，则以特定的{@code scale decrement}
     * 逐步减小缩放比率；例如：缩放比率初始值为1，{@code scale decrement}为0.05，如果发生OOM，
     * 则下一次尝试的缩放比率为0.95，以此类推。
     *
     * <p/>
     *
     * use 0.05 decrement
     */
    public static Bitmap trySize(int width, int height, @NonNull BitmapTrySize cb) {
        return trySize(width, height, 0.05f, cb);
    }

    /**
     * @param width     origin width
     * @param height    origin height
     * @param decrement OOM decrement
     * @param cb        callback
     */
    public static Bitmap trySize(int width, int height, float decrement, @NonNull BitmapTrySize cb) {
        // 防止OOM
        float scale = 1.0f;
        while (scale > 0) {
            int w = (int) (width * scale);
            int h = (int) (height * scale);
            try {
                Bitmap bm = cb.trySize(scale, width, height, w, h);
                cb.got(bm, scale, w, h);
                return bm;
            } catch (Throwable e) {
                if (e instanceof OutOfMemoryError) {
                    scale -= decrement;
                    continue;
                }
                cb.except(e);
                break;
            }
        }
        cb.none();
        return null;
    }

    @NonNull
    public static BitmapFactory.Options decodeSize(BitmapLoader loader) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.outWidth = -1;
        options.outHeight = -1;
        try {
            loader.load(options);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return options;
    }

    /**
     * @param loader 图片加载器
     * @param param 如果获取到宽高，则相应地会给{@link DecodeParam#outWidth}
     *              和{@link DecodeParam#outHeight}赋值；如果发生错误或获取不到，两个值为-1
     * @return 根据参数{@code param}计算得到的缩放比率
     */
    public static float calScale(BitmapLoader loader, @NonNull DecodeParam param) {
        float scale = 1;
        param.outWidth = -1;
        param.outHeight = -1;

        // check size first
        BitmapFactory.Options sizeOps = decodeSize(loader);
        if (sizeOps.outWidth <= 0 || sizeOps.outHeight <= 0) return scale;

        param.outWidth = sizeOps.outWidth;
        param.outHeight = sizeOps.outHeight;
        if (param.hasPreferredSize()) {
            float scaleW = Integer.MAX_VALUE;
            if (param.inPreferredWidth > 0) {
                scaleW = (float) param.inPreferredWidth / (float) sizeOps.outWidth;
            }
            float scaleH = Integer.MAX_VALUE;
            if (param.inPreferredHeight > 0) {
                scaleH = (float) param.inPreferredHeight / (float) sizeOps.outHeight;
            }
            scale = MathCompat.limitByRange(Math.min(scaleW, scaleH), 0, 1);
        } else if (param.hasPreferredScale()) {
            scale = param.inScale;
        }
        return scale;
    }

    public static int sizeByScaleOfScreenWidth(float ratio) {
        return (int) (DeviceInfos.getScreenWidth() * ratio);
    }

    public static int sizeByScaleOfScreenHeight(float ratio) {
        return (int) (DeviceInfos.getScreenHeight() * ratio);
    }

    public static int pixelsByScaleOfScreen(float ratio) {
        int s = DeviceInfos.getScreenWidth() * DeviceInfos.getScreenHeight();
        return (int) (s * ratio);
    }

    /**
     * 新增图片文件后，如果希望早一点在相册中显示，调用该方法强制刷新
     * @param targetFile 图片文件
     */
    public static void refreshGallery(@NonNull Context context, @NonNull final File targetFile) {
        if (!Files.exists(targetFile) || !targetFile.isFile()) {
            return;
        }
        final Context ctx = context.getApplicationContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // modified at 2017-04-07 14:33:28
                // 只要通知图库扫描即可
                //这个广播的目的就是更新图库，发了这个广播进入相册就可以
                ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(targetFile)));
            }
        }).start();
    }
}
