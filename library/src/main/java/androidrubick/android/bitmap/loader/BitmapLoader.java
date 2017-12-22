package androidrubick.android.bitmap.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

/**
 *
 * bitmap loader
 *
 * <p>
 * Created by Yin Yong on 2017/11/22.
 *
 * @since 1.0.0
 */
public interface BitmapLoader {

    /**
     * load {@link Bitmap}
     *
     * @param options null-ok
     * @return if {@link android.graphics.BitmapFactory.Options#inJustDecodeBounds} is set true,
     * return null; or load failed, return null; otherwise, return target bitmap
     * @since 1.0.0
     */
    Bitmap load(@Nullable BitmapFactory.Options options) ;

}
