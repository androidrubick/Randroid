package pub.androidrubick.base.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static pub.androidrubick.base.collection.CollectionsCompat.DEFAULT_CAPACITY;

/**
 * 简单的{@link java.util.List}的创建器
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/8/5 0005.
 *
 * @since 1.0
 */
public class ListBuilder {

    /**
     * @since 1.0
     */
    public static ListBuilder newArrayList() {
        return newArrayList(DEFAULT_CAPACITY);
    }

    /**
     * @since 1.0
     */
    public static ListBuilder newArrayList(int capacity) {
        return new ListBuilder(new ArrayList(capacity));
    }

    /**
     * @since 1.0
     */
    public static ListBuilder newLinkedList() {
        return new ListBuilder(new LinkedList());
    }

    protected List mList;
    protected ListBuilder(List list) {
        mList = list;
    }

    /**
     * @since 1.0
     */
    public ListBuilder add(Object value) {
        mList.add(value);
        return this;
    }

    /**
     * @since 1.0
     */
    public ListBuilder addAll(Collection otherCollection) {
        CollectionsCompat.addAll(mList, otherCollection);
        return this;
    }

    /**
     * @since 1.0
     */
    public ListBuilder appendAll(Object...arr) {
        CollectionsCompat.appendAll(mList, arr);
        return this;
    }

    /**
     * @since 1.0
     */
    public ListBuilder addAll(int location, Collection otherCollection) {
        CollectionsCompat.addAll(mList, location, otherCollection);
        return this;
    }

    /**
     * @since 1.0
     */
    public ListBuilder insertAll(int location, Object...arr) {
        CollectionsCompat.insertAll(mList, location, arr);
        return this;
    }

    /**
     * @since 1.0
     */
    public ListBuilder set(int location, Object object) {
        mList.set(location, object);
        return this;
    }

    /**
     * 创建并返回结果列表
     *
     * @since 1.0
     */
    public <T extends List>T build() {
        return (T) mList;
    }
}
