package androidrubick.android.bitmap;

import androidrubick.base.utils.MathCompat;

/**
 * 加载{@link android.graphics.Bitmap}会用到的参数
 * <p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public class DecodeParam {

    /**
     * 如果没有设置，值为-1；
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
     * 如果没有设置，值为-1；
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
     * 如果没有设置，值为-1；合理的值范围(0, 1)；
     *
     * <p></p>
     *
     * 注：
     * 创建时确保值为-1，或者范围在(0, 1)
     *
     * @since 1.0.0
     */
    public final float inScale;

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
        this.inScale = -1;
    }

    /**
     * @since 1.0.0
     */
    public static DecodeParam none() {
        return new DecodeParam();
    }

    /**
     * @since 1.0.0
     */
    public static DecodeParam preferredWidth(int w) {
        return new DecodeParam(w, -1);
    }

    /**
     * @since 1.0.0
     */
    public static DecodeParam preferredHeight(int h) {
        return new DecodeParam(-1, h);
    }

    /**
     * @since 1.0.0
     */
    public static DecodeParam preferredScale(float scale) {
        return new DecodeParam(scale);
    }

    /*package*/ DecodeParam(int inPreferredWidth, int inPreferredHeight) {
        this.inPreferredWidth = inPreferredWidth > 0 ? inPreferredWidth : -1;
        this.inPreferredHeight = inPreferredHeight > 0 ? inPreferredHeight : -1;
        this.inScale = -1;
    }

    /*package*/ DecodeParam(float scale) {
        this.inPreferredWidth = -1;
        this.inPreferredHeight = -1;
        scale = MathCompat.limitByRange(scale, 0, 1);
        // 如果scale == 0，或者scale == 1 都不需要缩放
        this.inScale = scale == 0 || scale == 1 ? -1 : scale;
    }

    /*package*/ boolean hasValidPreference() {
        return hasPreferredSize() || hasPreferredScale();
    }

    /*package*/ boolean hasPreferredSize() {
        return inPreferredWidth > 0 || inPreferredHeight > 0;
    }

    /*package*/ boolean hasPreferredScale() {
        return inScale != -1;
    }
}
