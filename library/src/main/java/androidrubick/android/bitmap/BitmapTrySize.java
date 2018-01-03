package androidrubick.android.bitmap;

import android.graphics.Bitmap;

/**
 * 尝试创建{@link Bitmap}，方式发生OOM的回调类；
 *
 * 用于找到最合适（不会导致OOM）的尺寸的图片；
 *
 * <p>
 *     尝试创建{@link Bitmap}，如果发生OOM，则以特定的{@code scale decrement}
 * 逐步减小缩放比率；例如：缩放比率初始值为1，{@code scale decrement}为0.05，如果发生OOM，
 * 则下一次尝试的缩放比率为0.95，以此类推。
 * </p>
 *
 * <p>
 *     <i>operation is sync</i>
 * </p>
 *
 * <p>
 * </p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public interface BitmapTrySize {

    /**
     * @param bm    result {@link Bitmap}
     * @param scale result scale factor, (0, +)
     * @param w     result w
     * @param h     result h
     * @since 1.0.0
     */
    void got(Bitmap bm, float scale, int w, int h);

    /**
     * @since 1.0.0
     */
    void except(Throwable e);

    /**
     * @since 1.0.0
     */
    void none();

}
