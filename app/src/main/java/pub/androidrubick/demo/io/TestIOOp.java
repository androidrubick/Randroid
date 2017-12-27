package pub.androidrubick.demo.io;

import android.os.SystemClock;
import android.support.annotation.NonNull;

import java.io.File;

import androidrubick.android.app.ARContext;
import androidrubick.android.io.BufferType;
import androidrubick.android.io.op.IOCallback;
import androidrubick.android.io.op.IOOp;
import androidrubick.base.logging.ARLogger;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/12/5.
 */
public class TestIOOp {

    public static void test() {
        ARLogger.debug("test0 = " + IOOp.fromContent("test").asRaw());
        ARLogger.debug("test1 = " + IOOp.from("test".getBytes()).asString());
        ARLogger.debug("test1 = " + IOOp.from(new File(ARContext.app().getCacheDir(), "yytest")).asString());

        IOOp.fromContent("yytest" + SystemClock.elapsedRealtime()).async()
                .callback(new IOCallback() {
                    @Override
                    public void onProgress(long readThisTime, long readTotal, @NonNull BufferType bt) {
                        ARLogger.error("onProgress " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete(long readTotal, @NonNull BufferType bt) {
                        ARLogger.error("onComplete " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onFailed(Throwable e, long readTotal, @NonNull BufferType bt) {
                        ARLogger.error("onFailed " + Thread.currentThread().getName());
                    }
                }).to(new File(ARContext.app().getCacheDir(), "yytest"));
    }

}
