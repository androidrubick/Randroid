package androidrubick.android.bitmap;

import android.graphics.Bitmap;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * 保存{@link Bitmap}时，使用该压缩参数
 * <p>
 * </p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public class CompressParam {
    /**
     * @since 1.0.0
     */
    public final Bitmap.CompressFormat format;
    /**
     * @since 1.0.0
     */
    @IntRange(from = 1, to = 100)
    public final int quality;

    /**
     * @since 1.0.0
     */
    public CompressParam(@NonNull Bitmap.CompressFormat format) {
        this(format, 100);
    }

    /**
     * @since 1.0.0
     */
    public CompressParam(@NonNull Bitmap.CompressFormat format,
                         @IntRange(from = 1, to = 100) int quality) {
        this.format = format;
        this.quality = quality;
    }

    /**
     * @since 1.0.0
     */
    public static CompressParam jpeg() {
        return new CompressParam(Bitmap.CompressFormat.JPEG);
    }

    /**
     * @since 1.0.0
     */
    public static CompressParam jpeg(@IntRange(from = 1, to = 100) int quality) {
        return new CompressParam(Bitmap.CompressFormat.JPEG, quality);
    }

    /**
     * @since 1.0.0
     */
    public static CompressParam png() {
        return new CompressParam(Bitmap.CompressFormat.PNG);
    }

    /**
     * @since 1.0.0
     */
    public static CompressParam png(@IntRange(from = 1, to = 100) int quality) {
        return new CompressParam(Bitmap.CompressFormat.PNG, quality);
    }
}
