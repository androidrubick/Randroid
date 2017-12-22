package androidrubick.android.bitmap.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.ByteArrayInputStream;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/11/22.
 *
 * @since 1.0.0
 */
public class BmDataLoader extends BaseBitmapLoader implements BitmapLoader {

    private byte[] mData;

    /**
     * @since 1.0.0
     */
    public BmDataLoader(byte[] data) {
        mData = data;
    }

    /**
     * load {@link Bitmap}
     * @since 1.0.0
     */
    @Override
    protected Bitmap load0(@Nullable BitmapFactory.Options options) throws Throwable {
        if (null == mData) {
            return null;
        }
        return BitmapFactory.decodeStream(new ByteArrayInputStream(mData), null, options);
    }
}
