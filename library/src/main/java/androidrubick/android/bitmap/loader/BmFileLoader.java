package androidrubick.android.bitmap.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.File;

import androidrubick.android.io.Files;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/11/22.
 *
 * @since 1.0.0
 */
public class BmFileLoader extends BaseBitmapLoader implements BitmapLoader {

    private File mFile;

    /**
     * @since 1.0.0
     */
    public BmFileLoader(File file) {
        mFile = file;
    }

    /**
     * load {@link Bitmap}
     * @since 1.0.0
     */
    @Override
    protected Bitmap load0(@Nullable BitmapFactory.Options options) throws Throwable {
        if (null == mFile || !mFile.isFile()) {
            return null;
        }
        return BitmapFactory.decodeFile(mFile.getAbsolutePath(), options);
    }
}
