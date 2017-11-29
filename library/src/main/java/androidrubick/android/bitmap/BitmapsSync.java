package androidrubick.android.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;

import androidrubick.android.bitmap.loader.BitmapLoader;
import androidrubick.android.bitmap.loader.BitmapLoaderFactory;
import androidrubick.android.io.IOUtils;

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
        if (null == param || !param.hasValidPreference()) return loader.load(null);

        float scale = Bitmaps.calScale(loader, param);
        if (param.outWidth <= 0 || param.outHeight <= 0) return null;

        int sampleSize = Math.max(1, Math.round(1f / scale));

        BitmapFactory.Options sampleOps = new BitmapFactory.Options();
        sampleOps.inSampleSize = sampleSize;
        Bitmap bm = loader.load(sampleOps);

        if (null == bm) {
            return null;
        }

        // 因为sample size得到的未必是目标大小
        // check size
        final int w = bm.getWidth();
        final int h = bm.getHeight();

        final int targetW = (int) (param.outWidth * scale);
        final int targetH = (int) (param.outHeight * scale);

        // 五分之一的出入
        final float minDeltaScale = 0.2f;
        final int minDelta = Math.min((int) (param.outWidth * minDeltaScale),
                (int) (param.outHeight * minDeltaScale));

        Bitmap reScaleBm = null;
        if (w - targetW > minDelta || h - targetH > minDelta) {
            // 如果获得的图片大小比实际需要的大，需要进行resize
            float reScale = (float) targetW / (float) w;
            reScaleBm = scale(bm, reScale);
        } else if (targetW - w > minDelta || targetH - h > minDelta) {
            // 如果获得的图片大小比实际需要的小，需要进行resize
            float reScale = (float) targetW / (float) w;
            reScaleBm = scale(bm, reScale);
        }
        if (null != reScaleBm) {
            bm.recycle();
            bm = reScaleBm;
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
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.close(outputStream);
        }
    }

    /**
     * 单次缩放图片，如果成功，返回缩放后的图片；如果失败，返回null
     *
     * @param bm    源图片
     * @param scale 缩放比率，(0, +∞)
     * @return 如果创建成功，返回新的图片
     * @since 1.0.0
     */
    @Nullable
    public static Bitmap scale(Bitmap bm, @FloatRange(from = 0, fromInclusive = false) float scale) {
        if (null == bm || scale <= 0f) {
            return null;
        }
        int w = (int) (bm.getWidth() * scale);
        int h = (int) (bm.getHeight() * scale);
        try {
            Bitmap.Config config = bm.getConfig();
            if (null == config) {
                config = Bitmap.Config.RGB_565;
            }
            Bitmap result = Bitmap.createBitmap(w, h, config);
            Canvas canvas = new Canvas(result);
            canvas.scale(scale, scale);
            canvas.drawBitmap(bm, 0, 0, null);
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 单次调整图片大小，如果成功，返回调整大小后的图片；如果失败，返回null
     *
     * @param bm     源图片
     * @param width  调整后的宽度
     * @param height 调整后的高度
     * @return 如果创建成功，返回新的图片
     * @since 1.0.0
     */
    @Nullable
    public static Bitmap resize(Bitmap bm, int width, int height) {
        if (null == bm || width <= 0 || height <= 0) {
            return null;
        }
        float w = bm.getWidth();
        float h = bm.getHeight();
        float scaleX = width / w;
        float scaleY = height / h;
        try {
            Bitmap.Config config = bm.getConfig();
            if (null == config) {
                config = Bitmap.Config.RGB_565;
            }
            Bitmap result = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(result);
            canvas.scale(scaleX, scaleY);
            canvas.drawBitmap(bm, 0, 0, null);
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
