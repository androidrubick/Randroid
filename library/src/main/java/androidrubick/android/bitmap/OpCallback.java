package androidrubick.android.bitmap;

/**
 * callback for bitmap operations
 *
 * <p>
 * Created by Yin Yong on 2017/11/15.
 *
 * @since 1.0.0
 */
public interface OpCallback {

    /**
     * @param success result of target operation, whether success
     * @since 1.0.0
     */
    void opResult(boolean success) ;
}
