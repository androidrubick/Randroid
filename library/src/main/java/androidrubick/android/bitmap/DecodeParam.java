package androidrubick.android.bitmap;

import android.support.annotation.FloatRange;

/**
 * 加载{@link android.graphics.Bitmap}会用到的参数
 * <p></p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public class DecodeParam {

    /**
     * 如果没有设置，值为-1；否则 > 0
     *
     * <p></p>
     *
     * {@link #inPreferredWidth} 和 {@link #inPreferredHeight} 可以只设置一个；
     *
     * <p></p>
     *
     * 如果{@link #inPreferredWidth} 和 {@link #inPreferredHeight}都设置了，
     *
     * 将会尽量按照二者与原图的宽高比率的最小值进行缩放
     *
     * @since 1.0.0
     */
    public final int inPreferredWidth;

    /**
     * 如果没有设置，值为-1；否则 > 0
     *
     * <p></p>
     *
     * {@link #inPreferredWidth} 和 {@link #inPreferredHeight} 可以只设置一个；
     *
     * <p></p>
     *
     * 如果{@link #inPreferredWidth} 和 {@link #inPreferredHeight}都设置了，
     *
     * 将会尽量按照二者与原图的宽高比率的最小值进行缩放
     *
     * @since 1.0.0
     */
    public final int inPreferredHeight;

    /**
     * 如果没有设置，值为-1；
     *
     * <p></p>
     *
     * @since 1.0.0
     */
    public final int inPreferredPixels;

    /**
     * 如果没有设置，值为-1；合理的值范围(0, +∞)；
     *
     * <p></p>
     *
     * 注：
     * 创建时确保值为-1，或者范围在(0, +∞)；
     *
     * 与{@link #inScaleY}值要么都为-1，要么都是范围在(0, +∞)
     *
     * @since 1.0.0
     */
    public final float inScaleX;

    /**
     * 如果没有设置，值为-1；合理的值范围(0, +∞)；
     *
     * <p></p>
     *
     * 注：
     * 创建时确保值为-1，或者范围在(0, +∞)；
     *
     * 与{@link #inScaleX}值要么都为-1，要么都是范围在(0, +∞)
     *
     * @since 1.0.0
     */
    public final float inScaleY;

    /**
     * whether use the same amount to scale
     */
    public final boolean inUniformScale;

    /**
     * @since 1.0.0
     */
    public int outWidth = -1;

    /**
     * @since 1.0.0
     */
    public int outHeight = -1;

    /*package*/ DecodeParam() {
        this.inPreferredWidth = -1;
        this.inPreferredHeight = -1;
        this.inPreferredPixels = -1;
        this.inScaleX = -1;
        this.inScaleY = -1;
        this.inUniformScale = false;
    }

    /**
     * @since 1.0.0
     */
    public static DecodeParam none() {
        return new DecodeParam();
    }

    /**
     * Uniform scale mode
     *
     * @since 1.0.0
     */
    public static DecodeParam preferredWidth(int w) {
        return new DecodeParam(w, -1, true);
    }

    /**
     * Uniform scale mode
     *
     * @since 1.0.0
     */
    public static DecodeParam preferredHeight(int h) {
        return new DecodeParam(-1, h, true);
    }

    /**
     * Non-uniform scale mode
     *
     * @since 1.0.0
     */
    public static DecodeParam preferredSize(int w, int h) {
        return preferredSize(w, h, false);
    }

    /**
     * @param uniformScale whether use the same amount to scale(min of scaleX , scaleY)
     * @since 1.0.0
     */
    public static DecodeParam preferredSize(int w, int h, boolean uniformScale) {
        return new DecodeParam(w, h, uniformScale);
    }

    /**
     * Uniform scale mode
     *
     * @param scale (0, +∞)
     * @since 1.0.0
     */
    public static DecodeParam preferredScale(@FloatRange(from = 0, fromInclusive = false) float scale) {
        return new DecodeParam(scale);
    }

    /**
     * @param scaleX (0, +∞)
     * @param scaleY (0, +∞)
     * @since 1.0.0
     */
    public static DecodeParam preferredScale(@FloatRange(from = 0, fromInclusive = false) float scaleX,
                                             @FloatRange(from = 0, fromInclusive = false) float scaleY) {
        return new DecodeParam(scaleX, scaleY);
    }

    /**
     * Uniform scale mode
     *
     * @since 1.0.0
     */
    public static DecodeParam preferredPixels(int pixels) {
        return new DecodeParam(pixels);
    }

    /*package*/ DecodeParam(int inPreferredWidth, int inPreferredHeight, boolean uniformScale) {
        inPreferredWidth = inPreferredWidth > 0 ? inPreferredWidth : -1;
        inPreferredHeight = inPreferredHeight > 0 ? inPreferredHeight : -1;
        if (!uniformScale && (inPreferredWidth == -1 || inPreferredHeight == -1)) {
            // if non-uniform scale, both inPreferredWidth & inPreferredHeight must be valid
            this.inPreferredWidth = this.inPreferredHeight = -1;
        } else {
            this.inPreferredWidth = inPreferredWidth;
            this.inPreferredHeight = inPreferredHeight;
        }
        this.inPreferredPixels = -1;
        this.inScaleX = -1;
        this.inScaleY = -1;
        this.inUniformScale = uniformScale;
    }

    /*package*/ DecodeParam(int pixels) {
        this.inPreferredWidth = -1;
        this.inPreferredHeight = -1;
        this.inPreferredPixels = pixels > 0 ? pixels : -1;
        this.inScaleX = -1;
        this.inScaleY = -1;
        this.inUniformScale = true;
    }

    /*package*/ DecodeParam(float scale) {
        this.inPreferredWidth = -1;
        this.inPreferredHeight = -1;
        this.inPreferredPixels = -1;
        // 如果scale == 0，或者scale == 1 都不需要缩放
        this.inScaleX = this.inScaleY = (scale > 0 && scale != 1) ? scale : -1;
        this.inUniformScale = true;
    }

    /*package*/ DecodeParam(float scaleX, float scaleY) {
        this.inPreferredWidth = -1;
        this.inPreferredHeight = -1;
        this.inPreferredPixels = -1;
        scaleX = scaleX > 0 ? scaleX : -1;
        scaleY = scaleY > 0 ? scaleY : -1;
        if (scaleX == -1 || scaleY == -1) {
            // 只要有一项是-1，则为invalid
            this.inScaleX = this.inScaleY = -1;
        } else {
            this.inScaleX = scaleX;
            this.inScaleY = scaleY;
        }
        this.inUniformScale = false;
    }

    /*package*/ boolean hasValidPreference() {
        return hasPreferredSize() || hasPreferredScale() || hasPreferredPixels();
    }

    /*package*/ boolean hasPreferredSize() {
        return inPreferredWidth > 0 || inPreferredHeight > 0;
    }

    /*package*/ boolean hasPreferredScale() {
        return inScaleX != -1 && inScaleY != -1;
    }

    /*package*/ boolean hasPreferredPixels() {
        return inPreferredPixels > 0;
    }
}
