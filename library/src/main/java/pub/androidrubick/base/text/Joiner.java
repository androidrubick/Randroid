package pub.androidrubick.base.text;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import pub.androidrubick.base.collection.CollectionsCompat;
import pub.androidrubick.base.utils.ArraysCompat;
import pub.androidrubick.base.utils.Exceptions;
import pub.androidrubick.base.utils.Function;
import pub.androidrubick.base.utils.Functions;
import pub.androidrubick.base.utils.Objects;

import static pub.androidrubick.base.utils.Preconditions.checkNotNull;

/**
 * 用于字符串连接
 *
 * <p/>
 *
 * An object which joins pieces of text (specified as an array, {@link Iterable}, varargs or even a
 * {@link java.util.Map}) with a separator. It either appends the results to an {@link Appendable} or returns
 * them as a {@link String}. Example: <pre>   {@code
 *
 *   Joiner joiner = Joiner.by("; ").skipNulls();
 *    . . .
 *   return joiner.join("Harry", null, "Ron", "Hermione");}</pre>
 *
 * <p>This returns the string {@code "Harry; Ron; Hermione"}. Note that all input elements are
 * converted to strings using {@link Function#apply(Object)}
 * （默认为{@link Functions#TO_STRING}） before being appended.
 *
 * <p>If neither {@link #skipNulls()} nor {@link #useForNull(java.lang.CharSequence)} is specified, the joining
 * methods will use {@code null}.
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/5/19 0019.
 *
 * @since 1.0
 */
public class Joiner {

    /**
     * 基于空字符串分割符的一个新的{@link Joiner}对象
     *
     * @return 基于指定的分割符一个新的{@link Joiner}对象
     *
     * @since 1.0
     */
    public static Joiner by() {
        return new Joiner(Strings.EMPTY);
    }

    /**
     * 基于指定的分割符的一个新的{@link Joiner}对象
     *
     * @param sep 分割字符串
     * @return 基于指定的分割符的一个新的{@link Joiner}对象
     *
     * @exception java.lang.NullPointerException
     *
     * @since 1.0
     */
    public static Joiner by(CharSequence sep) {
        return new Joiner(sep);
    }

    /**
     *
     * 基于指定的分割字符的一个新的{@link Joiner}对象
     *
     * @param sepChar 分割字符
     * @return 基于指定的分割字符的一个新的{@link Joiner}对象
     *
     * @since 1.0
     */
    public static Joiner by(char sepChar) {
        return new Joiner(String.valueOf(sepChar));
    }

    protected final CharSequence mSep;
    protected CharSequence mPrefix;
    protected CharSequence mSuffix;
    protected boolean mSkipNulls;
    protected CharSequence mNullText = Strings.NULL;
    protected Function mToStringFunc = Functions.TO_STRING;
    protected Joiner(CharSequence sep) {
        this(sep, null, null);
    }
    protected Joiner(CharSequence sep, CharSequence prefix, CharSequence suffix) {
        mSep = checkNotNull(sep, "separator is null");
        mPrefix = prefix;
        mSuffix = suffix;
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // 一些设置

    /**
     * 设置前缀、后缀（内容为空时也显示前缀后缀）
     *
     * <p/>
     *
     * 注意：null将等同于空字符串
     *
     * @param prefix 前缀
     * @param suffix 后缀
     *
     * @since 1.0
     */
    public Joiner withPreAndSuffix(CharSequence prefix, CharSequence suffix) {
        mPrefix = prefix;
        mSuffix = suffix;
        return this;
    }

    /**
     * 跳过值为null的对象。（默认没有设置此项）
     *
     * <p/>
     *
     * 如果{@link #useForNull(CharSequence)}在之前调用，则{@link #useForNull(CharSequence)}失效；
     * <br/>
     * 如果{@link #useForNull(CharSequence)}在之后调用，则该方法失效；
     *
     * @since 1.0
     */
    public Joiner skipNulls() {
        mSkipNulls = true;
        mNullText = Strings.NULL;
        return this;
    }

    /**
     * 用<code>nullText</code>替换值为null的对象。
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
     * 如果{@link #skipNulls()}在之前调用，则{@link #skipNulls()}失效；
     * <br/>
     * 如果{@link #skipNulls()}在之后调用，则该方法失效；
     *
     * @throws java.lang.NullPointerException
     *
     * @since 1.0
     */
    public Joiner useForNull(final CharSequence nullText) {
        mSkipNulls = false;
        mNullText = checkNotNull(nullText, "nullText is null");
        return this;
    }

    /**
     * 设置默认的{@link Object#toString()}函数，除非使用特定方法传入参数
     *
     * @since 1.0
     */
    public <T>Joiner withToStringFunc(Function<T, ? extends CharSequence> toStringFunc) {
        mToStringFunc = checkNotNull(toStringFunc);
        return this;
    }

    // for array
    public <T>String join(T[] parts) {
        return join((Function<? super T, ? extends CharSequence>) mToStringFunc, parts);
    }

    public <T>String join(Function<? super T, ? extends CharSequence> toStringFunc, T[] parts) {
        checkNotNull(toStringFunc);
        StringBuilder temp = new StringBuilder(calCapacity(ArraysCompat.len(parts)));
        return appendTo(temp, toStringFunc, parts).toString();
    }

    public <T>String join(Collection<T> c) {
        return join((Function<? super T, ? extends CharSequence>) mToStringFunc, c);
    }

    public <T>String join(Function<? super T, ? extends CharSequence> toStringFunc, Collection<T> c) {
        checkNotNull(toStringFunc);
        StringBuilder temp = new StringBuilder(calCapacity(CollectionsCompat.size(c)));
        return appendTo(temp, toStringFunc, c).toString();
    }

    public <T>String join(Iterable<T> it) {
        return join((Function<? super T, ? extends CharSequence>) mToStringFunc, it);
    }

    public <T>String join(Function<? super T, ? extends CharSequence> toStringFunc, Iterable<T> iterable) {
        return join(toStringFunc, Objects.isNull(iterable) ? (Iterator) null : iterable.iterator());
    }

    public <T>String join(Iterator<T> it) {
        return join((Function<? super T, ? extends CharSequence>) mToStringFunc, it);
    }

    public <T>String join(Function<? super T, ? extends CharSequence> toStringFunc, Iterator<T> it) {
        checkNotNull(toStringFunc);
        StringBuilder temp = new StringBuilder();
        return appendTo(temp, toStringFunc, it).toString();
    }


    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // for append to another Appendable
    public <T, A extends Appendable>A appendTo(A appendable, T[] parts) {
        return appendTo(appendable, (Function<? super T, ? extends CharSequence>) mToStringFunc, parts);
    }

    public <T, A extends Appendable>A appendTo(A appendable, Function<? super T, ? extends CharSequence> toStringFunc, T[] parts) {
        checkNotNull(appendable);
        checkNotNull(toStringFunc);
        appendPreOrSuffixIfNeeded(appendable, mPrefix);
        if (!Objects.isEmpty(parts)) {
            // 先打印第一个
            int index = 0;
            for (; index < parts.length; index++) {
                if (Objects.isNull(parts[index]) && mSkipNulls) continue;
                // 添加第一项
                appendResult(appendable, toStringOf(toStringFunc, parts[index], mNullText));
                index ++;
                break;
            }
            for (; index < parts.length; index++) {
                if (Objects.isNull(parts[index]) && mSkipNulls) continue;
                appendResult(appendable, mSep);
                appendResult(appendable, toStringOf(toStringFunc, parts[index], mNullText));
            }
        }
        appendPreOrSuffixIfNeeded(appendable, mSuffix);
        return appendable;
    }

    public <T, A extends Appendable>A appendTo(A appendable, Iterable<T> iterable) {
        return (A) appendTo(appendable, mToStringFunc, (Iterable) iterable);
    }

    public <T, A extends Appendable>A appendTo(A appendable, Function<? super T, ? extends CharSequence> toStringFunc, Iterable<T> iterable) {
        return (A) appendTo(appendable, toStringFunc, Objects.isNull(iterable) ? (Iterator) null : iterable.iterator());
    }

    public <T, A extends Appendable>A appendTo(A appendable, Iterator<T> it) {
        return (A) appendTo(appendable, mToStringFunc, (Iterator) it);
    }

    public <T, A extends Appendable>A appendTo(A appendable, Function<? super T, ? extends CharSequence> toStringFunc, Iterator<T> it) {
        checkNotNull(appendable);
        checkNotNull(toStringFunc);
        appendPreOrSuffixIfNeeded(appendable, mPrefix);
        if (!Objects.isNull(it)) {
            // 先打印第一个
            while (it.hasNext()) {
                T item = it.next();
                if (Objects.isNull(item) && mSkipNulls) continue;
                // 添加第一项
                appendResult(appendable, toStringOf(toStringFunc, item, mNullText));
                break;
            }
            while (it.hasNext()) {
                T item = it.next();
                if (Objects.isNull(item) && mSkipNulls) continue;
                appendResult(appendable, mSep);
                appendResult(appendable, toStringOf(toStringFunc, item, mNullText));
            }
        }
        appendPreOrSuffixIfNeeded(appendable, mSuffix);
        return appendable;
    }

    protected void appendPreOrSuffixIfNeeded(Appendable appendable, CharSequence xfix) {
        if (!Objects.isEmpty(xfix)) {
            appendResult(appendable, xfix);
        }
    }

    protected void appendResult(Appendable appendable, CharSequence result) {
        try {
            appendable.append(result);
        } catch (IOException e) {
            throw Exceptions.asRuntime(e);
        }
    }

    protected <T>CharSequence toStringOf(Function<? super T, ? extends CharSequence> toStringFunc, T value, CharSequence nullText) {
        return Objects.isNull(value) ? nullText : toStringFunc.apply(value);
    }

    protected int calCapacity(int size) {
        return Math.min(Math.max(size * 16, 8), 1 << 16);
    }

}
