package pub.androidrubick.base.utils;

import java.math.BigInteger;

/**
 * 包含数学计算、比较等相关验证的工具类；
 *
 * <br/>
 *
 * 验证参数是否符合条件，如果不符合则抛出{@link java.lang.IllegalArgumentException}异常
 *
 * <p/>
 *
 * Created by yong01.yin on 2015/1/5.
 *
 * @since 1.0
 */
public class MathPreconditions {

    public static int checkPositive(String role, int x) {
        if (x <= 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
        }
        return x;
    }

    public static long checkPositive(String role, long x) {
        if (x <= 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
        }
        return x;
    }

    public static BigInteger checkPositive(String role, BigInteger x) {
        if (x.signum() <= 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
        }
        return x;
    }

    public static int checkNonNegative(String role, int x) {
        if (x < 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    public static long checkNonNegative(String role, long x) {
        if (x < 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    public static BigInteger checkNonNegative(String role, BigInteger x) {
        if (x.signum() < 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    public static double checkNonNegative(String role, double x) {
        if (!(x >= 0)) { // not x < 0, to work with NaN.
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }

    public static void checkRoundingUnnecessary(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }

    public static void checkInRange(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("not in range");
        }
    }

    public static void checkNoOverflow(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("overflow");
        }
    }

    public static int checkNoLessThanMin(String role, int x, int min) {
        if (x < min) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= " + min);
        }
        return x;
    }

    private MathPreconditions() {}
}
