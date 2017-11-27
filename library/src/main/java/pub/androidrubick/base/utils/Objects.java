package pub.androidrubick.base.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Helper functions that can operate on any {@code Object}.
 * <br/>
 * 对所有Object对象试用的工具类
 *
 * <p/>
 *
 * Created by yong01.yin on 2014/12/30.
 *
 * @since 1.0
 */
public class Objects {

    private Objects() {}

    /**
     * 判断一个对象是否为空
     *
     * @since 1.0
     */
    public static boolean isNull(Object o) {
        return null == o;
    }

    /**
     * 判断一个对象是否为“空”——null 或者是对象意义上的空，如空字符串，
     *
     * 空数组，空集合，空字典等
     *
     * @since 1.0
     */
    public static boolean isEmpty(Object o) {
        if (isNull(o)) {
            return true;
        }
        if (o instanceof CharSequence) {
            return getAs(o, CharSequence.class).length() == 0;
        }
        if (o.getClass().isArray()) {
            return Array.getLength(o) == 0;
        }
        if (o instanceof Collection) {
            return getAs(o, Collection.class).isEmpty();
        }
        return (o instanceof Map) && getAs(o, Map.class).isEmpty();
    }

    /**
     * Null-safe equivalent of {@code a.equals(b)}.
     *
     * @since 1.0
     */
    public static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    /**
     * Returns true if both arguments are null,
     * the result of {@link java.util.Arrays#equals} if both arguments are primitive arrays,
     * the result of {@link java.util.Arrays#deepEquals} if both arguments are arrays of reference types,
     * and the result of {@link #equals} otherwise.
     *
     * @since 1.0
     */
    public static boolean deepEquals(Object a, Object b) {
        if (a == null || b == null) {
            return a == b;
        } else if (a instanceof Object[] && b instanceof Object[]) {
            return Arrays.deepEquals((Object[]) a, (Object[]) b);
        } else if (a instanceof boolean[] && b instanceof boolean[]) {
            return Arrays.equals((boolean[]) a, (boolean[]) b);
        } else if (a instanceof byte[] && b instanceof byte[]) {
            return Arrays.equals((byte[]) a, (byte[]) b);
        } else if (a instanceof char[] && b instanceof char[]) {
            return Arrays.equals((char[]) a, (char[]) b);
        } else if (a instanceof double[] && b instanceof double[]) {
            return Arrays.equals((double[]) a, (double[]) b);
        } else if (a instanceof float[] && b instanceof float[]) {
            return Arrays.equals((float[]) a, (float[]) b);
        } else if (a instanceof int[] && b instanceof int[]) {
            return Arrays.equals((int[]) a, (int[]) b);
        } else if (a instanceof long[] && b instanceof long[]) {
            return Arrays.equals((long[]) a, (long[]) b);
        } else if (a instanceof short[] && b instanceof short[]) {
            return Arrays.equals((short[]) a, (short[]) b);
        }
        return a.equals(b);
    }

    /**
     * Convenience wrapper for {@link Arrays#hashCode}, adding varargs.
     * This can be used to compute a hash code for an object's fields as follows:
     * {@code Objects.hash(a, b, c)}.
     *
     * @since 1.0
     */
    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    /**
     * Returns 0 for null or {@code o.hashCode()}.
     *
     * @since 1.0
     */
    public static int hashCode(Object o) {
        return (o == null) ? 0 : o.hashCode();
    }

    /**
     * Returns "null" for null or {@code o.toString()}.
     */
    public static String toString(Object o) {
        return toString(o, "null");
    }

    /**
     * Returns {@code nullString} for null or {@code o.toString()}.
     *
     * @since 1.0
     */
    public static String toString(Object o, String nullString) {
        return isNull(o) ? nullString : toStringInternal(o);
    }

    private static String toStringInternal(Object o) {
//        Class<?> clzOfO = o.getClass();
//        if (clzOfO.isArray()) {
//
//        } else if () {
//
//        }
        return o.toString();
    }

    /**
     * Creates an instance of {@link ToStringHelper}.
     *
     * <p>This is helpful for implementing {@link Object#toString()}.
     * Specification by example: <pre>   {@code
     *   // Returns "ClassName{}"
     *   Objects.toStringHelper(this)
     *       .toString();
     *
     *   // Returns "ClassName{x=1}"
     *   Objects.toStringHelper(this)
     *       .add("x", 1)
     *       .toString();
     *
     *   // Returns "MyObject{x=1}"
     *   Objects.toStringHelper("MyObject")
     *       .add("x", 1)
     *       .toString();
     *
     *   // Returns "ClassName{x=1, y=foo}"
     *   Objects.toStringHelper(this)
     *       .add("x", 1)
     *       .add("y", "foo")
     *       .toString();
     *
     *   // Returns "ClassName{x=1}"
     *   Objects.toStringHelper(this)
     *       .omitNullValues()
     *       .add("x", 1)
     *       .add("y", null)
     *       .toString();
     *   }}</pre>
     *
     * <p>Note that in GWT, class names are often obfuscated.
     *
     * @param self the object to generate the string for (typically {@code this}),
     *        used only for its class name
     *
     * @since 1.0
     */
    public static ToStringHelper toStringHelper(Object self) {
        return new ToStringHelper(simpleName(self.getClass()));
    }

    /**
     * Creates an instance of {@link ToStringHelper} in the same manner as
     * {@link Objects#toStringHelper(Object)}, but using the name of {@code clazz}
     * instead of using an instance's {@link Object#getClass()}.
     *
     * <p>Note that in GWT, class names are often obfuscated.
     *
     * @param clazz the {@link Class} of the instance
     *
     * @since 1.0
     */
    public static ToStringHelper toStringHelper(Class<?> clazz) {
        return new ToStringHelper(simpleName(clazz));
    }

    /**
     * Creates an instance of {@link ToStringHelper} in the same manner as
     * {@link Objects#toStringHelper(Object)}, but using {@code className} instead
     * of using an instance's {@link Object#getClass()}.
     *
     * @param className the name of the instance type
     *
     * @since 1.0
     */
    public static ToStringHelper toStringHelper(String className) {
        return new ToStringHelper(className);
    }

    /**
     * {@link Class#getSimpleName()} is not GWT compatible yet, so we
     * provide our own implementation.
     *
     * @since 1.0
     */
    private static String simpleName(Class<?> clazz) {
        String name = clazz.getName();

        // the nth anonymous class has a class name ending in "Outer$n"
        // and local inner classes have names ending in "Outer.$1Inner"
        name = name.replaceAll("\\$[0-9]+", "\\$");

        // we want the name of the inner class all by its lonesome
        int start = name.lastIndexOf('$');

        // if this isn't an inner class, just find the start of the
        // top level class name.
        if (start == -1) {
            start = name.lastIndexOf('.');
        }
        return name.substring(start + 1);
    }

    public static <T> T getAs(Object o) {
        if (null == o) {
            return (T) null;
        }
        try {
            return (T) o;
        } catch (Exception e) {
            return (T)null;
        }
    }

    public static <T> T getAs(Object o, Class<T> clz) {
        if (null == o) {
            return (T) null;
        }
        try {
            return (T) o;
        } catch (Exception e) {
            return (T)null;
        }
    }

    /**
     * 如果指定对象为空，则返回代替对象
     * @param o 目标对象
     * @param another 代替对象
     * @return 如果指定对象不为空，则返回原对象<code>o</code>；如果指定对象为空，则返回代替对象<code>another</code>。
     *
     * @since 1.0
     */
    public static <T, U extends T>T getOr(T o, U another) {
        return isNull(o) ? another : o;
    }

    /**
     * 如果指定对象不为空，则返回对象1；如果指定对象为空，则返回代替对象2
     *
     * @since 1.0
     */
    public static <T, U>U getOr(T o, U sth, U sthAnother) {
        return isNull(o) ? sth : sthAnother;
    }

    /**
     * 返回目标类型的默认值；
     *
     * 比如，int为0，引用数据类型默认值都为null
     *
     * @since 1.0
     */
    public static <T>T defValue(Class<T> type) {
        return (T) Primitives.defValueOf(type);
    }

}
