package pub.androidrubick.android.bitmap;

/**
 * callback for bitmap operations, with {@link android.graphics.Bitmap result bitmap}
 *
 * as the 2nd parameter
 *
 * <p>
 * <p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public interface OpPCallback<Result> {
    /**
     * @param success result of target operation, whether success
     * @param result result bitmap
     * @since 1.0.0
     */
    void opResult(boolean success, Result result);
}
