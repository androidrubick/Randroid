package pub.androidrubick.base.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import pub.androidrubick.base.utils.ArraysCompat;

import static pub.androidrubick.base.utils.Objects.isNull;

/**
 *
 * 扩展{@link java.util.Collections}没有支持的方法
 *
 * <p/>
 * <p/>
 * Created by Yin Yong on 15/5/21.
 *
 * @since 1.0
 */
public class CollectionsCompat {
    private CollectionsCompat() { }

    public static final int DEFAULT_CAPACITY = 1 << 3;

    /**
     * 判断一个集合是否是空的
     *
     * @since 1.0
     */
    public static boolean isEmpty(Collection<?> c) {
        return size(c) == 0;
    }

    /**
     * 判断一个Map是否是空的
     *
     * @since 1.0
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return size(map) == 0;
    }

    /**
     * 获得集合的长度
     *
     * @param c 集合
     * @return 集合的长度
     *
     * @since 1.0
     */
    public static int size(Collection<?> c) {
        return isNull(c) ? 0 : c.size();
    }

    /**
     * 获得Map的长度
     *
     * @param map 集合
     * @return Map的长度
     *
     * @since 1.0
     */
    public static int size(Map<?, ?> map) {
        return isNull(map) ? 0 : map.size();
    }

    /**
     * 将另外一个集合的元素全部添加到目标集合中
     * @param dest 目标集合
     * @param src 数据来源集合
     * @param <E>
     * @param <R>
     * @return 目标集合
     *
     * @since 1.0
     */
    public static <E, R extends Collection<? super E>>R addAll(R dest, Collection<E> src) {
        if (isNull(dest) || isEmpty(src)) {
            return dest;
        }
        dest.addAll(src);
        return dest;
    }

    /**
     * 将一个数组的元素全部添加到目标集合中
     *
     * @param dest 目标数组
     * @param src 数据来源集合
     * @param <E>
     * @param <R>
     * @return 目标集合
     *
     * @since 1.0
     */
    public static <E, R extends Collection<? super E>>R appendAll(R dest, E...src) {
        if (isNull(dest) || ArraysCompat.isEmpty(src)) {
            return dest;
        }
        Collections.addAll(dest, src);
        return dest;
    }

    /**
     * 将另外一个集合的元素从<code>location</code>位置开始全部添加到目标列表中
     *
     * @param dest 目标列表
     * @param src 数据来源集合
     * @param <E>
     * @param <R>
     * @return 目标列表
     *
     * @since 1.0
     */
    public static <E, R extends List<? super E>>R addAll(R dest, int location, Collection<E> src) {
        if (isNull(dest) || isEmpty(src)) {
            return dest;
        }
        dest.addAll(location, src);
        return dest;
    }

    /**
     * 将一个数组的元素从<code>location</code>位置开始添加到目标列表中
     *
     * @param dest 目标列表
     * @param src 数据来源数组
     * @param <E>
     * @param <R>
     * @return 目标列表
     *
     * @since 1.0
     */
    public static <E, R extends List<? super E>>R insertAll(R dest, int location, E...src) {
        if (isNull(dest) || ArraysCompat.isEmpty(src)) {
            return dest;
        }
        for (E item: src) {
            dest.add(location ++, item);
        }
        return dest;
    }

    /**
     * 将另外一个Map的元素全部添加到目标Map中
     *
     * @param dest 目标列表
     * @param src 数据来源Map
     * @return 目标Map
     *
     * @since 1.0
     */
    public static <K, V, R extends Map<? super K, ? super V>>R putAll(R dest, Map<K, V> src) {
        if (isNull(dest) || isEmpty(src)) {
            return dest;
        }
        dest.putAll(src);
        return dest;
    }

    /**
     * 将另外一个Map的元素全部添加到目标Map中，
     *
     * 如果<code>key</code>重复，则不添加
     *
     * @param dest 目标列表
     * @param src 数据来源Map
     * @return 目标Map
     *
     * @since 1.0
     */
    public static <K, V, R extends Map<? super K, ? super V>>R putAllUnCover(R dest, Map<K, V> src) {
        if (isNull(dest) || isEmpty(src)) {
            return dest;
        }
        for (Map.Entry<K, V> item: src.entrySet()) {
            putUnCover(dest, item.getKey(), item.getValue());
        }
        return dest;
    }

    /**
     * 如果<code>key</code>已存在，则覆盖
     *
     * @since 1.0
     */
    public static <K, V, R extends Map<? super K, ? super V>>boolean put(R map, K key, V value) {
        if (isNull(map)) {
            return false;
        }
        map.put(key, value);
        return true;
    }

    /**
     * 如果<code>key</code>已存在，则不添加
     *
     * @since 1.0
     */
    public static <K, V, R extends Map<? super K, ? super V>>boolean putUnCover(R map, K key, V value) {
        if (isNull(map)) {
            return false;
        }
        if (map.containsKey(key)) {
            return false;
        }
        map.put(key, value);
        return true;
    }

    /**
     * 从<code>map</code>中获取<code>key</code>对应的值
     *
     * @since 1.0
     */
    public static <K, V, R extends Map<? super K, V>, Result extends V>Result getValue(R map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return (Result) map.get(key);
    }

    /**
     * 从<code>map</code>中获取<code>key</code>对应的值
     *
     * @since 1.0
     */
    public static <K, V, R extends Map<? super K, V>, Result extends V>Result getValue(R map, K key, Result placeholder) {
        if (isEmpty(map)) {
            return placeholder;
        }
        Result result = (Result) map.get(key);
        if (null == result && !map.containsKey(key)) {
            result = placeholder;
        }
        return result;
    }

    /**
     * 判断<code>map</code>中hi否有<code>key</code>
     *
     * @since 1.0
     */
    public static <K, V, R extends Map<? super K, V>>boolean containsKey(R map, K key) {
        return !isEmpty(map) && map.containsKey(key);
    }

    /**
     * 判断<code>map</code>中hi否有<code>value</code>
     *
     * @since 1.0
     */
    public static <K, V, R extends Map<K, ? super V>>boolean containsValue(R map, V value) {
        return !isEmpty(map) && map.containsValue(value);
    }

}
