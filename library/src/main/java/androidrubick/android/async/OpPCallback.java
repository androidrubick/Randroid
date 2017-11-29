package androidrubick.android.async;

/**
 * callback for common operations, with a parameter
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
     * @param result result
     * @since 1.0.0
     */
    void opResult(boolean success, Result result);
}
