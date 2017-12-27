package androidrubick.android.bitmap;

import android.graphics.Bitmap;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

import java.io.File;

import androidrubick.android.async.AsyncOp;
import androidrubick.android.async.OpCallback;
import androidrubick.android.async.OpPCallback;
import androidrubick.android.bitmap.loader.BitmapLoader;
import androidrubick.android.bitmap.loader.BitmapLoaderFactory;

/**
 * 异步处理{@link Bitmap}相关的方法
 * <p>
 * </p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public class BitmapsAsync {

    private static abstract class Async extends AsyncOp<Void, Void, Bitmap> {
        private final OpPCallback<Bitmap> mCb;

        Async(@NonNull OpPCallback<Bitmap> cb) {
            mCb = cb;
        }

        @Override
        protected final void onPostExecute(Bitmap result) {
            mCb.opResult(null != result, result);
        }
    }

    /**
     * 异步加载图片
     *
     * @param loader {@link Bitmap}加载器
     * @since 1.0.0
     */
    public static void load(final BitmapLoader loader, final OpPCallback<Bitmap> cb) {
        load(loader, null, cb);
    }

    /**
     * 异步加载图片
     *
     * @param loader {@link Bitmap}加载器
     * @param cb     回调，如果加载成功回调将会返回最终加载的图片
     * @since 1.0.0
     */
    public static void load(final BitmapLoader loader, final DecodeParam param, final OpPCallback<Bitmap> cb) {
        if (null == cb) return;

        new Async(cb) {
            @Override
            protected final Bitmap doInBackground(Void... params) {
                return BitmapsSync.load(loader, param);
            }
        }.execute();
    }

    /**
     * 异步加载图片文件
     *
     * @param file 文件对象
     * @param cb   回调，如果加载成功回调将会返回最终加载的图片
     * @since 1.0.0
     */
    public static void load(final File file, final OpPCallback<Bitmap> cb) {
        load(file, null, cb);
    }

    /**
     * 异步加载图片文件
     *
     * @param file  文件对象
     * @param param 加载参数
     * @param cb    回调，如果加载成功回调将会返回最终加载的图片
     * @since 1.0.0
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
     * @since 1.0.0
     */
    public static void save(final Bitmap bm, final CompressParam param, final File file,
                            final OpCallback cb) {
        if (null == cb) return;

        new AsyncOp<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return BitmapsSync.save(bm, param, file);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                cb.opResult(null != result && result);
            }
        }.execute();
    }

    /**
     * 异步单次缩放图片，如果成功，返回缩放后的图片；如果失败，返回null
     *
     * @param bm    源图片
     * @param scale 缩放比率
     * @param cb    回调，如果创建成功，返回新的图片
     * @since 1.0.0
     */
    public static void scale(final Bitmap bm, @FloatRange(from = 0, to = 1, fromInclusive = false) final float scale,
                             final OpPCallback<Bitmap> cb) {
        if (null == cb) return;

        new Async(cb) {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return BitmapsSync.scale(bm, scale);
            }
        }.execute();
    }

    /**
     * 异步单次调整图片大小，如果成功，返回调整大小后的图片；如果失败，返回null
     *
     * @param bm     源图片
     * @param width  调整后的宽度
     * @param height 调整后的高度
     * @return 如果创建成功，返回新的图片
     * @since 1.0.0
     */
    public static void resize(final Bitmap bm, final int width, final int height, final OpPCallback<Bitmap> cb) {
        if (null == cb) return;

        new Async(cb) {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return BitmapsSync.resize(bm, width, height);
            }
        }.execute();
    }
}
