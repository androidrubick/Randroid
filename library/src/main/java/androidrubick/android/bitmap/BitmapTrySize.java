package androidrubick.android.bitmap;

import android.graphics.Bitmap;

/**
 * 尝试创建{@link Bitmap}，方式发生OOM的回调类；
 *
 * 用于找到最合适（不会导致OOM）的尺寸的图片；
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
     * this method may throw OOM;
     * when OOM occurs, new try will follow.
     *
     * <p/>
     *
     * 当OOM时，将进行下一轮尝试
     *
     * @param scale   scale factor, [0, 1]
     * @param originW origin width
     * @param originH origin height
     * @param w       new width to try
     * @param h       new height to try
     * @see Bitmaps#trySize
     * @since 1.0.0
     */
    Bitmap trySize(float scale, int originW, int originH, int w, int h);

    /**
     * @param bm result {@link Bitmap}
     * @param scale result scale factor, [0, 1]
     * @param w result w
     * @param h result h
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
