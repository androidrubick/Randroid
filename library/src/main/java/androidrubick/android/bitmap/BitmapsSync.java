package androidrubick.android.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;

import androidrubick.android.bitmap.loader.BitmapLoader;
import androidrubick.android.bitmap.loader.BitmapLoaderFactory;
import androidrubick.android.io.IOUtils;
import androidrubick.base.logging.ARLogger;

import static androidrubick.android.bitmap.Bitmaps.calScale;
import static androidrubick.android.bitmap.Bitmaps.loadIgnoreExc;
import static androidrubick.android.bitmap.Bitmaps.useConfig;

/**
 * 同步处理{@link Bitmap}相关的方法
 * <p>
 * <p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public class BitmapsSync {

    /**
     * 同步加载图片文件
     *
     * @param file 要加载的文件对象
     * @return 如果加载成功回调将会返回最终加载的图片
     * @since 1.0.0
     */
    public static Bitmap load(File file) {
        return load(file, null);
    }

    /**
     * 同步加载图片文件
     *
     * @param file  文件对象
     * @param param 加载参数
     * @return 如果加载成功回调将会返回最终加载的图片
     * @since 1.0.0
     */
    @Nullable
    public static Bitmap load(File file, DecodeParam param) {
        return load(BitmapLoaderFactory.fromFile(file), param);
    }

    /**
     * 同步加载图片
     *
     * @param loader {@link Bitmap}加载器
     * @return 如果加载成功回调将会返回最终加载的图片
     * @since 1.0.0
     */
    public static Bitmap load(BitmapLoader loader) {
        return load(loader, null);
    }

    /**
     * 同步加载图片
     *
     * @param loader {@link Bitmap}加载器
     * @return 如果加载成功回调将会返回最终加载的图片
     * @since 1.0.0
     */
    public static Bitmap load(BitmapLoader loader, DecodeParam param) {
        if (null == loader) return null;

        // use dummy
        if (null == param || !param.hasValidPreference()) return loadIgnoreExc(loader, null);

        float scale[] = Bitmaps.calScale(loader, param);
        if (param.outWidth <= 0 || param.outHeight <= 0) return null;

        final int ow = param.outWidth;
        final int oh = param.outHeight;
        int sampleSize = Math.max(1, Math.round(1f / Math.min(scale[0], scale[1])));

        BitmapFactory.Options sampleOps = new BitmapFactory.Options();
        sampleOps.inSampleSize = sampleSize;

        Bitmap bm = loadIgnoreExc(loader, sampleOps);
        if (null == bm) {
            return null;
        }

        // 因为sample size得到的未必是目标大小
        // check size
        final int w = bm.getWidth();
        final int h = bm.getHeight();

        // get result size
        param.outWidth = w;
        param.outHeight = h;

        final int targetW = (int) (ow * scale[0]);
        final int targetH = (int) (oh * scale[1]);

        // 五分之一的出入
        final float minDeltaScale = 0.2f;
        final int minDelta = Math.min((int) (w * minDeltaScale), (int) (h * minDeltaScale));

        Bitmap reScaleBm = null;
        if (Math.abs(w - targetW) > minDelta || Math.abs(h - targetH) > minDelta) {
            // 如果获得的图片大小比实际需要的大，需要进行resize
            reScaleBm = resize(bm, DecodeParam.preferredScale((float) targetW / (float) w,
                    (float) targetH / (float) h));
        }
        if (null != reScaleBm) {
            bm.recycle();
            bm = reScaleBm;

            // get result size
            param.outWidth = bm.getWidth();
            param.outHeight = bm.getHeight();
        }
        return bm;
    }

    /**
     * 同步保存图片文件
     *
     * @param bm    源图片
     * @param param 保存时用的压缩参数，包含格式和质量
     * @param file  要保存到的文件对象
     * @return 是否保存成功
     * @since 1.0.0
     */
    public static boolean save(Bitmap bm, CompressParam param, File file) {
        if (null == bm || null == param || null == file || file.isDirectory()) {
            return false;
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bm.compress(param.format, param.quality, outputStream);
            outputStream.flush();
            return true;
        } catch (Exception e) {
            ARLogger.warning("BitmapsSync save error", e);
            return false;
        } finally {
            IOUtils.close(outputStream);
        }
    }

    /**
     * 单次调整图片大小，如果成功，返回调整大小后的图片；如果失败，返回null
     *
     * @param bm     源图片
     * @param param  改变尺寸的参数
     * @return 如果创建成功，返回新的图片
     * @since 1.0.0
     */
    public static Bitmap resize(Bitmap bm, DecodeParam param) {
        if (null == bm) {
            return null;
        }
        final int width = bm.getWidth(), height = bm.getHeight();
        float[] scale = calScale(width, height, param);
        float scaleX = scale[0];
        float scaleY = scale[1];
        int w = (int) (width * scaleX);
        int h = (int) (height * scaleY);
        try {
            Bitmap result = Bitmap.createBitmap(w, h, useConfig(bm, Bitmap.Config.RGB_565));
            Canvas canvas = new Canvas(result);
            canvas.scale(scaleX, scaleY);
            canvas.drawBitmap(bm, 0, 0, null);
            return result;
        } catch (Throwable e) {
            ARLogger.warning("BitmapsSync resize error", e);
            return null;
        }
    }
}
