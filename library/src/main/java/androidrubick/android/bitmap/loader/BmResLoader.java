package androidrubick.android.bitmap.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/11/22.
 *
 * @since 1.0.0
 */
public class BmResLoader extends BaseBitmapLoader implements BitmapLoader {

    private Context mContext;
    private int mRes;

    /**
     * @since 1.0.0
     */
    public BmResLoader(@NonNull Context context, int res) {
        mContext = context;
        mRes = res;
    }

    /**
     * load {@link Bitmap}
     * @since 1.0.0
     */
    @Override
    protected Bitmap load0(@Nullable BitmapFactory.Options options) throws Throwable {
        if (null == mContext) {
            return null;
        }
        return BitmapFactory.decodeResource(mContext.getResources(), mRes, options);
    }
}
