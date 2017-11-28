package pub.androidrubick.android.bitmap;

import android.graphics.Bitmap;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public abstract class BitmapTrySizeAdapter implements BitmapTrySize {
    /**
     * this method may throw OOM
     *
     * @param scale   scale factor, [0, 1]
     * @param originW origin width
     * @param originH origin height
     * @param w       new width to try
     * @param h       new height to try
     * @since 1.0.0
     */
    public abstract Bitmap trySize(float scale, int originW, int originH, int w, int h);

    /**
     * @since 1.0.0
     */
    @Override
    public void got(Bitmap bm, float scale, int w, int h) {
    }

    /**
     * @since 1.0.0
     */
    @Override
    public void except(Throwable e) {
    }

    /**
     * @since 1.0.0
     */
    @Override
    public void none() {
    }
}
