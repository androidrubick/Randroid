package androidrubick.android.bitmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

import androidrubick.android.async.ARSchedulers;
import androidrubick.android.bitmap.loader.BitmapLoader;
import androidrubick.android.device.DeviceInfos;
import androidrubick.android.io.Files;
import androidrubick.base.logging.ARLogger;
import androidrubick.base.utils.ArraysCompat;

/**
 * 工具类，用于{@link Bitmap}相关的操作
 * <p>
 * <p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public class Bitmaps {

    /**
     *
     * catch any exceptions;
     *
     * return null, if any error occurs;
     *
     * @since 1.0.0
     */
    public static Bitmap loadIgnoreExc(BitmapLoader loader, BitmapFactory.Options options) {
        try {
            return loader.load(options);
        } catch (Throwable e) {
            ARLogger.warning("Bitmaps load error", e);
            return null;
        }
    }

    /**
     * @since 1.0.0
     */
    @NonNull
    public static BitmapFactory.Options decodeSize(BitmapLoader loader) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.outWidth = -1;
        options.outHeight = -1;
        loadIgnoreExc(loader, options);
        return options;
    }

    /**
     * @param source maybe null, use {@link Bitmap.Config#ARGB_8888} instead
     * @return
     */
    @NonNull
    public static Bitmap.Config useConfig(@Nullable Bitmap source) {
        return useConfig(source, Bitmap.Config.ARGB_8888);
    }

    /**
     * @param source maybe null, use {@code defConfig} instead
     */
    @NonNull
    public static Bitmap.Config useConfig(@Nullable Bitmap source, @NonNull Bitmap.Config defConfig) {
        Bitmap.Config newConfig = defConfig;
        Bitmap.Config config = null;
        if (null != source) {
            config = source.getConfig();
        }
        // GIF files generate null configs, assume ARGB_8888
        if (null != config) {
            switch (config) {
                case RGB_565:
                    newConfig = Bitmap.Config.RGB_565;
                    break;
                case ALPHA_8:
                    newConfig = Bitmap.Config.ALPHA_8;
                    break;
                case ARGB_4444:
                case ARGB_8888:
                    newConfig = Bitmap.Config.ARGB_8888;
                    break;
                default:
                    newConfig = defConfig;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (config == Bitmap.Config.RGBA_F16) {
                            newConfig = Bitmap.Config.RGBA_F16;
                        }
                    }
                    break;
            }
        }
        return newConfig;
    }

    /**
     * determine scale factor from target {@code param} with the {@link BitmapLoader loader}
     *
     * provided
     *
     * @param loader 图片加载器
     * @param param  如果获取到宽高，则相应地会给{@link DecodeParam#outWidth}
     *               和{@link DecodeParam#outHeight}赋值；如果发生错误或获取不到，两个值为-1
     * @return 2-len array, 根据参数{@code param}计算得到的缩放比率
     * @since 1.0.0
     */
    public static float[] calScale(BitmapLoader loader, DecodeParam param) {
        if (null == param || !param.hasValidPreference()) {
            return ArraysCompat.by(1f, 1f);
        }

        param.outWidth = -1;
        param.outHeight = -1;
        // check size first
        BitmapFactory.Options sizeOps = decodeSize(loader);
        if (sizeOps.outWidth <= 0 || sizeOps.outHeight <= 0) return ArraysCompat.by(1f, 1f);

        param.outWidth = sizeOps.outWidth;
        param.outHeight = sizeOps.outHeight;
        return calScale(sizeOps.outWidth, sizeOps.outHeight, param);
    }

    /**
     * determine scale factor from target {@code param} with the specific
     * {@code w}, {@code h}
     *
     * @param w     宽
     * @param h     高
     * @return 2-len array, 根据参数{@code param}计算得到的缩放比率
     * @since 1.0.0
     */
    public static float[] calScale(int w, int h, DecodeParam param) {
        float scaleX = 1, scaleY = 1;
        if (null == param || !param.hasValidPreference()) {
            return ArraysCompat.by(scaleX, scaleY);
        }
        if (w < 0 || h < 0) {
            return ArraysCompat.by(scaleX, scaleY);
        }

        if (param.hasPreferredSize()) {
            float scaleW = Integer.MAX_VALUE;
            if (param.inPreferredWidth > 0) {
                scaleW = (float) param.inPreferredWidth / (float) w;
            }
            float scaleH = Integer.MAX_VALUE;
            if (param.inPreferredHeight > 0) {
                scaleH = (float) param.inPreferredHeight / (float) h;
            }
            if (param.inUniformScale) {
                scaleX = scaleY = Math.min(scaleW, scaleH);
            } else {
                scaleX = scaleW;
                scaleY = scaleH;
            }
        } else if (param.hasPreferredScale()) {
            scaleX = param.inScaleX;
            scaleY = param.inScaleY;
        } else if (param.hasPreferredPixels()) {
            double ms = (double) param.inPreferredPixels / (double) (w * h);
            scaleX = scaleY = (float) Math.pow(ms, 0.5);
        }
        return ArraysCompat.by(scaleX > 0 ? scaleX : 1, scaleY > 0 ? scaleY : 1);
    }

    /**
     * @since 1.0.0
     */
    public static int sizeByScaleOfScreenWidth(float ratio) {
        return (int) (DeviceInfos.screenWidth() * ratio);
    }

    /**
     * @since 1.0.0
     */
    public static int sizeByScaleOfScreenHeight(float ratio) {
        return (int) (DeviceInfos.screenHeight() * ratio);
    }

    /**
     * @since 1.0.0
     */
    public static int pixelsByScaleOfScreen(float ratio) {
        int s = DeviceInfos.screenWidth() * DeviceInfos.screenHeight();
        return (int) (s * ratio);
    }

    /**
     * 新增图片文件后，如果希望早一点在相册中显示，调用该方法强制刷新
     *
     * @param targetFile target image file
     * @since 1.0.0
     */
    public static void refreshGallery(@NonNull Context context, @NonNull final File targetFile) {
        if (!Files.exists(targetFile) || !targetFile.isFile()) {
            return;
        }
        final Context ctx = context.getApplicationContext();
        ARSchedulers.newThread(new Runnable() {
            @Override
            public void run() {
                // modified at 2017-04-07 14:33:28
                // 只要通知图库扫描即可
                //这个广播的目的就是更新图库，发了这个广播进入相册就可以
                ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(targetFile)));
            }
        });
    }
}
