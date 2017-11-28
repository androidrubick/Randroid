package androidrubick.android.bitmap;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.FloatRange;

import java.io.File;

import androidrubick.android.bitmap.loader.BitmapLoader;
import androidrubick.android.bitmap.loader.BitmapLoaderFactory;

/**
 * 异步处理{@link Bitmap}相关的方法
 * <p>
 * <p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 2.3.0
 */
public class BitmapsAsync {

    /**
     * 异步加载图片
     *
     * @param loader {@link Bitmap}加载器
     */
    public static void load(final BitmapLoader loader, final OpPCallback<Bitmap> cb) {
        load(loader, DecodeParam.NOTHING, cb);
    }

    /**
     * 异步加载图片
     *
     * @param loader {@link Bitmap}加载器
     * @param cb     回调，如果加载成功回调将会返回最终加载的图片
     */
    public static void load(final BitmapLoader loader, final DecodeParam param, final OpPCallback<Bitmap> cb) {
        if (null == cb) return;
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return BitmapsSync.load(loader, param);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                cb.opResult(null != result, result);
            }
        }.execute((Void[]) null);
    }

    /**
     * 异步加载图片文件
     *
     * @param file 文件对象
     * @param cb   回调，如果加载成功回调将会返回最终加载的图片
     */
    public static void load(final File file, final OpPCallback<Bitmap> cb) {
        load(file, DecodeParam.NOTHING, cb);
    }

    /**
     * 异步加载图片文件
     *
     * @param file  文件对象
     * @param param 加载参数
     * @param cb    回调，如果加载成功回调将会返回最终加载的图片
     */
    public static void load(final File file, final DecodeParam param, final OpPCallback<Bitmap> cb) {
        load(BitmapLoaderFactory.fromFile(file), param, cb);
    }

    /**
     * 异步保存图片文件
     *
     * @param bm    源图片
     * @param param 保存时用的压缩参数，包含格式和质量
     * @param file  要保存到的文件对象
     * @param cb    回调
     */
    public static void save(final Bitmap bm, final CompressParam param, final File file,
                            final OpCallback cb) {
        if (null == cb) return;

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return BitmapsSync.save(bm, param, file);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                cb.opResult(null != result && result);
            }
        }.execute((Void[]) null);
    }

    /**
     * 异步保存图片文件
     *
     * @param bm    源图片
     * @param scale 缩放比率
     * @param cb    回调，如果创建成功，返回新的图片
     */
    public static void scale(final Bitmap bm, @FloatRange(from = 0, to = 1, fromInclusive = false) final float scale,
                             final OpPCallback<Bitmap> cb) {
        if (null == cb) return;

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return BitmapsSync.scale(bm, scale);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                cb.opResult(null != result, result);
            }
        }.execute((Void[]) null);
    }
}
