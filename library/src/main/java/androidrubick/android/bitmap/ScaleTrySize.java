package androidrubick.android.bitmap;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * use scale raw bitmap to try size
 * <p>
 * </p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public class ScaleTrySize extends BitmapTrySizeAdapter implements BitmapTrySize {

    private Bitmap mBm;
    /**
     * @since 1.0.0
     */
    public ScaleTrySize(@NonNull Bitmap bm) {
        super(bm.getWidth(), bm.getHeight());
        mBm = bm;
    }

    /**
     * @since 1.0.0
     */
    @Override
    public Bitmap trySize(float scale, int originW, int originH, int w, int h) {
        return BitmapsSync.scale(mBm, scale);
    }
}
