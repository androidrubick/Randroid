package pub.androidrubick.base.logging;

/**
 * Created by Yin Yong on 16/12/12.
 */
public class DummyLog implements ARLog {
    @Override
    public void debug(String msg) {

    }

    @Override
    public void info(String msg) {

    }

    @Override
    public void warning(String msg) {

    }

    @Override
    public void warning(String msg, Throwable err) {

    }

    @Override
    public void error(String msg) {

    }

    @Override
    public void error(String msg, Throwable err) {

    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public int getLevel() {
        return 0;
    }
}
