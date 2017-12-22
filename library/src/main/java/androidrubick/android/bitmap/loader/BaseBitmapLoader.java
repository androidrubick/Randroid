package androidrubick.android.bitmap.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/12/21.
 *
 * @since 1.0.0
 */
public abstract class BaseBitmapLoader implements BitmapLoader {

    @Override
    public Bitmap load(@Nullable BitmapFactory.Options options) {
        if (null != options && options.inJustDecodeBounds) {
            options.outWidth = -1;
            options.outHeight = -1;
        }
        try {
            return load0(options);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    protected abstract Bitmap load0(@Nullable BitmapFactory.Options options) throws Throwable ;
}
