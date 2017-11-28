package androidrubick.android.async;

import android.os.AsyncTask;

/**
 * add method {@link #execute()} with no parameters
 *
 * <p>
 * Created by Yin Yong on 2017/11/28.
 *
 * @since 1.0.0
 */
public abstract class AsyncOp<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    public final void execute() {
        execute((Params[]) null);
    }

}
