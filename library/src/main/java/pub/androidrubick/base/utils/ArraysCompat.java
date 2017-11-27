package pub.androidrubick.base.utils;

import java.lang.reflect.Array;

import static pub.androidrubick.base.utils.Objects.isNull;

/**
 * {@link java.util.Arrays}的兼容版本
 *
 * <p/>
 *
 * 有些方法在1.6及以上版本才支持，用该工具类兼容这些方法
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/4/25 0025.
 *
 * @since 1.0
 */
public class ArraysCompat {

    private ArraysCompat() { /* no instance needed */ }

    /**
     * 判断数组是否是空数组
     *
     * @since 1.0
     */
    public static boolean isEmpty(Object target) {
        return isNull(target) || (target.getClass().isArray() && Array.getLength(target) == 0);
    }

    /**
     * 借用可变参数的便利性，生成一个由参数元素组成的数组
     *
     * @param elements 数组元素
     *
     * @since 1.0
     */
    public static <T>T[] by(T...elements) {
        return elements;
    }

    /**
     * 获取数组的长度，如果数组为null，则返回0；
     *
     * @since 1.0
     */
    public static int len(Object[] arr) {
        return isNull(arr) ? 0 : arr.length;
    }

    /**
     * 获取数组的长度，如果数组为null，则返回0；
     * @since 1.0.0
     */
    public static int len(byte[] arr) {
        return isNull(arr) ? 0 : arr.length;
    }

    /**
     * 获取数组的长度，如果数组为null，则返回0；
     * @since 1.0.0
     */
    public static int len(char[] arr) {
        return isNull(arr) ? 0 : arr.length;
    }

    /**
     * 获取数组的长度，如果数组为null，则返回0；
     * @since 1.0.0
     */
    public static int len(int[] arr) {
        return isNull(arr) ? 0 : arr.length;
    }

    /**
     * 获取数组的长度，如果数组为null，则返回0；
     * @since 1.0.0
     */
    public static int len(long[] arr) {
        return isNull(arr) ? 0 : arr.length;
    }

    /**
     * 获取数组的长度，如果数组为null，则返回0；
     * @since 1.0.0
     */
    public static int len(float[] arr) {
        return isNull(arr) ? 0 : arr.length;
    }

    /**
     * 获取数组的长度，如果数组为null，则返回0；
     * @since 1.0.0
     */
    public static int len(double[] arr) {
        return isNull(arr) ? 0 : arr.length;
    }

    /**
     * 获取数组的长度，如果数组为null，则返回0；
     * @since 1.0.0
     */
    public static int len(boolean[] arr) {
        return isNull(arr) ? 0 : arr.length;
    }

    /**
     * Checks that the range described by {@code offset} and {@code count} doesn't exceed
     * {@code arrayLength}.
     *
     * @since 1.0
     * @hide
     */
    public static void checkOffsetAndCount(int arrayLength, int offset, int count) {
        if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
            throw new ArrayIndexOutOfBoundsException("length=" + arrayLength + "; regionStart=" + offset
                    + "; regionLength=" + count);
        }
    }

    /**
     * Checks that the range described by {@code start} and {@code end} doesn't exceed
     * {@code len}.
     *
     * @since 1.0
     * @hide
     */
    public static void checkStartAndEnd(int len, int start, int end) {
        if (start < 0 || end > len) {
            throw new ArrayIndexOutOfBoundsException("start < 0 || end > len."
                    + " start=" + start + ", end=" + end + ", len=" + len);
        }
        if (start > end) {
            throw new IllegalArgumentException("start > end: " + start + " > " + end);
        }
    }

}