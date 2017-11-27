package pub.androidrubick.base.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;

import static pub.androidrubick.base.collection.CollectionsCompat.DEFAULT_CAPACITY;

/**
 * 简单的{@link java.util.Map}的创建器
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/5/17 0017.
 *
 * @since 1.0
 */
public class MapBuilder {

    /**
     * @since 1.0
     */
    public static MapBuilder newHashMap() {
        return newHashMap(DEFAULT_CAPACITY);
    }

    /**
     * @since 1.0
     */
    public static MapBuilder newHashMap(int capacity) {
        return new MapBuilder(new HashMap(capacity));
    }

    /**
     * @since 1.0
     */
    public static MapBuilder newWeakHashMap() {
        return new MapBuilder(new WeakHashMap());
    }

    /**
     * @since 1.0
     */
    public static MapBuilder newWeakHashMap(int capacity) {
        return new MapBuilder(new WeakHashMap(capacity));
    }

    /**
     * @since 1.0
     */
    public static MapBuilder newLinkedHashMap() {
        return newLinkedHashMap(DEFAULT_CAPACITY);
    }

    /**
     * @since 1.0
     */
    public static MapBuilder newLinkedHashMap(int capacity) {
        return new MapBuilder(new LinkedHashMap(capacity));
    }

    /**
     * @since 1.0
     */
    public static MapBuilder newTreeMap() {
        return new MapBuilder(new TreeMap());
    }

    protected Map mMap;
    protected MapBuilder(Map map) {
        mMap = map;
    }

    /**
     * @since 1.0
     */
    public MapBuilder put(Object key, Object value) {
        mMap.put(key, value);
        return this;
    }

    /**
     * @since 1.0
     */
    public MapBuilder putAll(Map otherMap) {
        CollectionsCompat.putAll(mMap, otherMap);
        return this;
    }

    /**
     * @since 1.0
     */
    public <T extends Map>T build() {
        return (T) mMap;
    }

}
