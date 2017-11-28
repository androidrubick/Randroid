package pub.androidrubick.base.utils;

/**
 * <p/>
 *
 * Created by Yin Yong on 2015/9/8.
 *
 * @since 1.0
 */
public class MathCompat {

    private MathCompat() { /* no instance needed */ }

    /**
     * 让<code>src</code>保持在[<code>min</code>, <code>max</code>]区间
     */
    public static int limitByRange(int src, int min, int max) {
        final int MIN = Math.min(min, max);
        final int MAX = Math.max(min, max);
        return Math.min(Math.max(src, MIN), MAX);
    }

    /**
     * 让<code>src</code>保持在[<code>min</code>, <code>max</code>]区间
     */
    public static long limitByRange(long src, long min, long max) {
        final long MIN = Math.min(min, max);
        final long MAX = Math.max(min, max);
        return Math.min(Math.max(src, MIN), MAX);
    }

    /**
     * 让<code>src</code>保持在[<code>min</code>, <code>max</code>]区间
     */
    public static float limitByRange(float src, float min, float max) {
        final float MIN = Math.min(min, max);
        final float MAX = Math.max(min, max);
        return Math.min(Math.max(src, MIN), MAX);
    }

    /**
     * 让<code>src</code>保持在[<code>min</code>, <code>max</code>]区间
     */
    public static double limitByRange(double src, double min, double max) {
        final double MIN = Math.min(min, max);
        final double MAX = Math.max(min, max);
        return Math.min(Math.max(src, MIN), MAX);
    }



    /**
     * 如果<code>src</code>不在[<code>min</code>, <code>max</code>]区间内，则返回<code>placeholder</code>
     */
    public static int limitByRange(int src, int min, int max, int placeholder) {
        int compareRes = limitByRange(src, min, max);
        if (compareRes != src) {
            compareRes = placeholder;
        }
        return compareRes;
    }

    /**
     * 如果<code>src</code>不在[<code>min</code>, <code>max</code>]区间内，则返回<code>placeholder</code>
     */
    public static long limitByRange(long src, long min, long max, long placeholder) {
        long compareRes = limitByRange(src, min, max);
        if (compareRes != src) {
            compareRes = placeholder;
        }
        return compareRes;
    }

    /**
     * 如果<code>src</code>不在[<code>min</code>, <code>max</code>]区间内，则返回<code>placeholder</code>
     */
    public static float limitByRange(float src, float min, float max, float placeholder) {
        float compareRes = limitByRange(src, min, max);
        if (compareRes != src) {
            compareRes = placeholder;
        }
        return compareRes;
    }

    /**
     * 如果<code>src</code>不在[<code>min</code>, <code>max</code>]区间内，则返回<code>placeholder</code>
     */
    public static double limitByRange(double src, double min, double max, double placeholder) {
        double compareRes = limitByRange(src, min, max);
        if (compareRes != src) {
            compareRes = placeholder;
        }
        return compareRes;
    }



    /**
     * 如果<code>src</code>小于<code>target</code>，则返回<code>placeholder</code>
     */
    public static int ifLessThan(int src, int target, int placeholder) {
        return src < target ? placeholder : src;
    }

    /**
     * 如果<code>src</code>小于<code>target</code>，则返回<code>placeholder</code>
     */
    public static long ifLessThan(long src, long target, long placeholder) {
        return src < target ? placeholder : src;
    }

    /**
     * 如果<code>src</code>小于<code>target</code>，则返回<code>placeholder</code>
     */
    public static float ifLessThan(float src, float target, float placeholder) {
        return src < target ? placeholder : src;
    }

    /**
     * 如果<code>src</code>小于<code>target</code>，则返回<code>placeholder</code>
     */
    public static double ifLessThan(double src, double target, double placeholder) {
        return src < target ? placeholder : src;
    }

}
