package androidrubick.android.bitmap;

import android.graphics.Bitmap;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;

/**
 *
 * try a non-OOM size {@link Bitmap} with some user settings
 *
 * <p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public abstract class BitmapTrySizeOp implements BitmapTrySize {

    private final int mOriginWidth;
    private final int mOriginHeight;
    private float mDecrement = 0.05f;
    private float mPreferredScaleStart = 1f;
    private float mPreferredScaleEnd = 0f;

    /**
     * @param originWidth  origin width, (0, +)
     * @param originHeight origin height, (0, +)
     * @since 1.0.0
     */
    public BitmapTrySizeOp(int originWidth, int originHeight) {
        mOriginWidth = Math.max(0, originWidth);
        mOriginHeight = Math.max(0, originHeight);
    }

    /**
     * @param scale (0, +)
     * @since 1.0.0
     */
    public BitmapTrySizeOp preferredScale(
            @FloatRange(from = 0, fromInclusive = false) float scale
    ) {
        mPreferredScaleStart = Math.max(0, scale);
        mPreferredScaleEnd = 0f;
        return this;
    }

    /**
     * @param scaleStart (0, +)
     * @param scaleEnd   (0, +)
     * @since 1.0.0
     */
    public BitmapTrySizeOp preferredScaleRange(
            @FloatRange(from = 0, fromInclusive = false) float scaleStart,
            @FloatRange(from = 0, fromInclusive = false) float scaleEnd
    ) {
        mPreferredScaleStart = Math.max(0, scaleStart);
        mPreferredScaleEnd = Math.max(0, scaleEnd);
        return this;
    }

    /**
     * scale decrement, when OOM, decrease scale by this factor
     *
     * @param decrement decrement factor, (0, +), default is 0.05
     * @since 1.0.0
     */
    public BitmapTrySizeOp decBy(@FloatRange(from = 0, fromInclusive = false) float decrement) {
        mDecrement = Math.max(0, decrement);
        return this;
    }

    /**
     * @return if cannot get preferred bitmap, or exception occurs, return null
     * @since 1.0.0
     */
    @Nullable
    public Bitmap performTry() {
        final int width = mOriginWidth;
        final int height = mOriginHeight;
        final float decrement = mDecrement;
        final float scaleStart = mPreferredScaleStart;
        final float scaleEnd = mPreferredScaleEnd;
        // 防止OOM
        float scale = scaleStart;
        while (scale > scaleEnd) {
            int w = (int) (width * scale);
            int h = (int) (height * scale);
            try {
                Bitmap bm = trySize(scale, width, height, w, h);
                got(bm, scale, w, h);
                return bm;
            } catch (Throwable e) {
                if (e instanceof OutOfMemoryError) {
                    scale -= decrement;
                    continue;
                }
                except(e);
                break;
            }
        }
        none();
        return null;
    }

    /**
     * this method may throw OOM;
     * when OOM occurs, new try will follow.
     *
     * <p/>
     *
     * 当OOM时，将进行下一轮尝试
     *
     * @param scale   scale factor, (0, +)
     * @param originW origin width
     * @param originH origin height
     * @param w       new width to try
     * @param h       new height to try
     * @since 1.0.0
     */
    protected abstract Bitmap trySize(float scale, int originW, int originH, int w, int h);

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
