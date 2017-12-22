package androidrubick.android.bitmap.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 *
 * load visible rect of target view
 *
 * <p></p>
 * Created by Yin Yong on 2017/11/28.
 *
 * @since 1.0.0
 */
public class BmViewLoader extends BaseBitmapLoader implements BitmapLoader {

    private View mView;
    public BmViewLoader(@NonNull View view) {
        mView = view;
    }

    @Override
    protected Bitmap load0(@Nullable BitmapFactory.Options options) throws Throwable {
        if (null == mView) {
            return null;
        }
        if (null != options && options.inJustDecodeBounds) {
            options.outWidth = mView.getWidth();
            options.outHeight = mView.getHeight();
            return null;
        }

        final int oW = mView.getWidth();
        final int oH = mView.getHeight();
        if (oW == 0 && oH == 0) {
            return null;
        }

        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        float scale = 1f;
        if (null != options) {
            if (null != options.inPreferredConfig) {
                config = options.inPreferredConfig;
            }
            if (options.inSampleSize > 1) {
                scale = 1f / (float) options.inSampleSize;
            }
        }

        Bitmap bm = Bitmap.createBitmap((int) (oW * scale), (int) (oH * scale), config);
        bm.setDensity(mView.getResources().getDisplayMetrics().densityDpi);
        Canvas canvas = new Canvas(bm);
        canvas.scale(scale, scale);
        mView.draw(canvas);
        return bm;
    }
}
