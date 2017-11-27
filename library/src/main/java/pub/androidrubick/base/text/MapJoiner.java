package pub.androidrubick.base.text;

import java.util.Iterator;
import java.util.Map;

import pub.androidrubick.base.collection.CollectionsCompat;
import pub.androidrubick.base.utils.Function;
import pub.androidrubick.base.utils.Functions;
import pub.androidrubick.base.utils.Objects;

import static pub.androidrubick.base.utils.Preconditions.checkNotNull;

/**
 * 用于以特定方式将{@link java.util.Map}连接为字符串
 *
 * <p/>
 *
 * An object which joins pieces of text (specified as an array, {@link Iterable}, varargs or even a
 * {@link java.util.Map}) with a separator. It either appends the results to an {@link Appendable} or returns
 * them as a {@link String}. Example: <pre>   {@code
 *
 *   MapJoiner joiner = MapJoiner.by("; ").skipNulls();
 *    . . .
 *   return joiner.join("Harry", null, "Ron", "Hermione");}</pre>
 *
 * <p>This returns the string {@code "Harry; Ron; Hermione"}. Note that all input elements are
 * converted to strings using {@link Function#apply(Object)}
 * （默认为{@link Functions#TO_STRING}） before being appended.
 *
 * <p>If neither {@link #skipNullKeys()} nor {@link #useForNullKey(java.lang.CharSequence)} is specified, the joining
 * methods will use {@code null}.
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/5/19 0019.
 *
 * @since 1.0
 */
public class MapJoiner {

    /**
     * 基于指定的分割符的一个新的{@link MapJoiner}对象
     *
     * @param sep 元素分割字符串
     * @param keyValueSeparator 键值分割字符串
     * @return 基于指定的分割符的一个新的{@link MapJoiner}对象
     *
     * @throws java.lang.NullPointerException
     *
     * @since 1.0
     */
    public static MapJoiner by(CharSequence sep, CharSequence keyValueSeparator) {
        return new MapJoiner(sep, keyValueSeparator);
    }

    protected final Joiner mJoiner;
    protected final CharSequence mKeyValueSep;
    protected CharSequence mEntryPrefix;
    protected CharSequence mEntrySuffix;
    protected boolean mSkipNullKeys;
    protected boolean mSkipNullValues;
    protected CharSequence mNullKeyText = Strings.NULL;
    protected CharSequence mNullValueText = Strings.NULL;
    protected Function mToStringFuncOfKey = Functions.TO_STRING;
    protected Function mToStringFuncOfValue = Functions.TO_STRING;
    protected MapJoiner(CharSequence sep, CharSequence keyValueSeparator) {
        mJoiner = new Joiner(sep) {
            @Override
            public <T, A extends Appendable> A appendTo(A appendable, Function<? super T, ? extends CharSequence> toStringFunc, Iterator<T> it) {
                checkNotNull(appendable);
                checkNotNull(toStringFunc);
                appendPreOrSuffixIfNeeded(appendable, mPrefix);
                if (!Objects.isNull(it)) {
                    // 先打印第一个
                    while (it.hasNext()) {
                        Map.Entry<?, ?> item = Objects.getAs(it.next());
                        if (Objects.isNull(item.getKey()) && mSkipNullKeys) continue;
                        if (Objects.isNull(item.getValue()) && mSkipNullValues) continue;
                        // 添加第一项
                        toStringOfEntry(appendable, item);
                        break;
                    }
                    while (it.hasNext()) {
                        Map.Entry<?, ?> item = Objects.getAs(it.next());
                        if (Objects.isNull(item.getKey()) && mSkipNullKeys) continue;
                        if (Objects.isNull(item.getValue()) && mSkipNullValues) continue;
                        appendResult(appendable, mSep);
                        toStringOfEntry(appendable, item);
                    }
                }
                appendPreOrSuffixIfNeeded(appendable, mSuffix);
                return appendable;
            }

            private <K, V>void toStringOfEntry(Appendable appendable, Map.Entry<K, V> item) {
                appendPreOrSuffixIfNeeded(appendable, mEntryPrefix);
                appendResult(appendable, toStringOf(mToStringFuncOfKey, item.getKey(), mNullKeyText));
                appendResult(appendable, mKeyValueSep);
                appendResult(appendable, toStringOf(mToStringFuncOfValue, item.getValue(), mNullValueText));
                appendPreOrSuffixIfNeeded(appendable, mEntrySuffix);
            }
        };
        mKeyValueSep = checkNotNull(keyValueSeparator, "keyValueSeparator is null");
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // 一些设置

    /**
     * 设置前缀、后缀（内容为空时也显示前缀后缀）
     * @param prefix 前缀
     * @param suffix 后缀
     *
     * @since 1.0
     */
    public MapJoiner withPreAndSuffix(CharSequence prefix, CharSequence suffix) {
        mJoiner.withPreAndSuffix(prefix, suffix);
        return this;
    }

    /**
     * 设置每一项的前缀、后缀
     * @param prefix 前缀
     * @param suffix 后缀
     *
     * @since 1.0
     */
    public MapJoiner withEntryPreAndSuffix(CharSequence prefix, CharSequence suffix) {
        mEntryPrefix = prefix;
        mEntrySuffix = suffix;
        return this;
    }

    /**
     * 跳过键值为null的对象。（默认没有设置此项）
     *
     * <p/>
     *
     * 如果{@link #useForNullKey(CharSequence)}在之前调用，则{@link #useForNullKey(CharSequence)}失效；
     * <br/>
     * 如果{@link #useForNullKey(CharSequence)}在之后调用，则该方法失效；
     *
     * @since 1.0
     */
    public MapJoiner skipNullKeys() {
        mSkipNullKeys = true;
        mNullKeyText = Strings.NULL;
        return this;
    }

    /**
     * 跳过值为null的对象。（默认没有设置此项）
     *
     * <p/>
     *
     * 如果{@link #useForNullValue(CharSequence)}在之前调用，则{@link #useForNullValue(CharSequence)}失效；
     * <br/>
     * 如果{@link #useForNullValue(CharSequence)}在之后调用，则该方法失效；
     *
     * @since 1.0
     */
    public MapJoiner skipNullValues() {
        mSkipNullValues = true;
        mNullValueText = Strings.NULL;
        return this;
    }

    /**
     * 用<code>nullKeyText</code>替换键值为null的对象。
     *
     * <p/>
     *
     * 注意：
     * <ul>
     *     <li>该替换值不会应用{@link Function}</li>
     *     <li>如果为null，将抛出{@link java.lang.NullPointerException}</li>
     * </ul>
     *
     * <p/>
     *
     * 如果{@link #skipNullKeys()}在之前调用，则{@link #skipNullKeys()}失效；
     * <br/>
     * 如果{@link #skipNullKeys()}在之后调用，则该方法失效；
     *
     * @throws java.lang.NullPointerException
     *
     * @since 1.0
     */
    public MapJoiner useForNullKey(CharSequence nullKeyText) {
        mSkipNullKeys = false;
        mNullKeyText = checkNotNull(nullKeyText);
        return this;
    }

    /**
     * 用<code>nullValueText</code>替换值为null的对象。
     *
     * <p/>
     *
     * 注意：
     * <ul>
     *     <li>该替换值不会应用{@link Function}</li>
     *     <li>如果为null，将抛出{@link java.lang.NullPointerException}</li>
     * </ul>
     *
     * <p/>
     *
     * @throws java.lang.NullPointerException
     *
     * @since 1.0
     */
    public MapJoiner useForNullValue(CharSequence nullValueText) {
        mSkipNullValues = false;
        mNullValueText = checkNotNull(nullValueText);
        return this;
    }

    public <I, R extends CharSequence>MapJoiner withToStringFuncOfKey(Function<I, R> toStringFuncOfKey) {
        mToStringFuncOfKey = checkNotNull(toStringFuncOfKey);
        return this;
    }

    public <I, R extends CharSequence>MapJoiner withToStringFuncOfValue(Function<I, R> toStringFuncOfValue) {
        mToStringFuncOfValue = checkNotNull(toStringFuncOfValue);
        return this;
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // for append to another Appendable
    public <K, V>String join(Map<K, V> map) {
        StringBuilder temp = new StringBuilder(calCapacity(CollectionsCompat.size(map)));
        return appendTo(temp, map).toString();
    }

    public <K, V, E extends Map.Entry<K, V>>String join(Iterable<E> iterable) {
        return join(Objects.isNull(iterable) ? (Iterator) null : iterable.iterator());
    }

    public <K, V, E extends Map.Entry<K, V>>String join(Iterator<E> it) {
        StringBuilder temp = new StringBuilder();
        return appendTo(temp, it).toString();
    }

    public <K, V, A extends Appendable>A appendTo(A appendable, Map<K, V> map) {
        return (A) appendTo(appendable, Objects.isNull(map) ? (Iterable) null : map.entrySet());
    }

    public <K, V, E extends Map.Entry<K, V>, A extends Appendable>A appendTo(A appendable, Iterable<E> iterable) {
        return (A) appendTo(appendable, Objects.isNull(iterable) ? (Iterator) null : iterable.iterator());
    }

    public <K, V, E extends Map.Entry<K, V>, A extends Appendable>A appendTo(A appendable, Iterator<E> it) {
        checkNotNull(appendable);
        mJoiner.appendTo(appendable, it);
        return appendable;
    }

    protected int calCapacity(int size) {
        return Math.min(Math.max(size * 16, 16), 1 << 16);
    }

}
