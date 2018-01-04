package androidrubick.android.bitmap;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * try a non-OOM size by scaling the origin bitmap
 * <p>
 * </p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public class ScaleBitmapTrySizeOp extends BitmapTrySizeOp implements BitmapTrySize {

    private Bitmap mBm;
    /**
     * @since 1.0.0
     */
    public ScaleBitmapTrySizeOp(@NonNull Bitmap bm) {
        super(bm.getWidth(), bm.getHeight());
        mBm = bm;
    }

    /**
     * @since 1.0.0
     */
    @Override
    protected final Bitmap trySize(float scale, int originW, int originH, int w, int h) {
        return BitmapsSync.resize(mBm, DecodeParam.preferredScale(scale));
    }
}
