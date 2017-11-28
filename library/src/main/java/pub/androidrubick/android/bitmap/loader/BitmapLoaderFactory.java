package pub.androidrubick.android.bitmap.loader;

import android.content.Context;

import java.io.File;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/11/22.
 *
 * @since 1.0.0
 */
public class BitmapLoaderFactory {

    /**
     * @since 1.0.0
     */
    public static BitmapLoader fromFile(File file) {
        return new BmFileLoader(file);
    }

    /**
     * @since 1.0.0
     */
    public static BitmapLoader fromRes(Context context, int res) {
        return new BmResLoader(context, res);
    }

    /**
     * @since 1.0.0
     */
    public static BitmapLoader fromData(byte[] data) {
        return new BmDataLoader(data);
    }

}
