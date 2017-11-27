package pub.androidrubick.base.text;

import java.util.Arrays;

import pub.androidrubick.base.utils.ArraysCompat;
import pub.androidrubick.base.utils.Objects;

import static pub.androidrubick.base.utils.Preconditions.checkNotNull;
import static pub.androidrubick.base.utils.Preconditions.checkState;

/**
 * 根据指定的字符或者字符串，遍历/局部遍历目标字符串
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/5/31 0031.
 *
 * @since 1.0
 */
public class SimpleTokenizer {

    private String mInput;
    private int position = 0;

    /**
     * 根据目标字符串创建实例
     *
     * @param input 目标字符串
     *
     * @since 1.0
     */
    public SimpleTokenizer(String input) {
        mInput = checkNotNull(input);
    }

    /**
     * 从当前位置A遍历，直到某个位置B字符不存在于<code>chars</code>中。
     *
     * @return A-B之间的字符。如果当前位置就不存在于<code>chars</code>中，返回空字符串
     *
     * @since 1.0
     */
    public String consumeTokens(char...chars) {
        checkState(hasMore());
        Arrays.sort(chars);
        final int lastPos = position;
        while(hasMore()) {
            if (ArraysCompat.binarySearch(chars, 0, chars.length, previewChar()) < 0) {
                break;
            }
            position ++;
        }
        return lastPos == position ? Strings.EMPTY : mInput.substring(lastPos, position);
    }

    /**
     * 从当前位置A遍历，直到某个位置B字符不是字符<code>c</code>。
     *
     * @return A-B之间的字符。如果当前位置就不是字符<code>c</code>，返回空字符串
     *
     * @since 1.0
     */
    public String consumeChar(char c) {
        checkState(hasMore());
        final int lastPos = position;
        while(hasMore()) {
            if (c != previewChar()) {
                break;
            }
            position ++;
        }
        return lastPos == position ? Strings.EMPTY : mInput.substring(lastPos, position);
    }

    /**
     * 从当前位置A遍历，直到某个位置B字符不存在于<code>tokens</code>中。
     *
     * @return A-B之间的字符。如果当前位置就不存在于<code>tokens</code>中，返回空字符串
     *
     * @since 1.0
     */
    public String consumeTokens(String tokens) {
        return consumeTokens(tokens.toCharArray());
    }

    /**
     * 从当前位置A遍历，直到某个位置B不是<code>target</code>。
     *
     * @return A-B之间的字符。如果当前位置就不是<code>target</code>,返回空字符串
     *
     * @since 1.0
     */
    public String consumeTarget(String target) {
        checkNotNull(target);
        if (Objects.isEmpty(target)) {
            return Strings.EMPTY;
        }
        checkState(hasMore());
        final int lastPos = position;
        while(hasMore()) {
            if (!mInput.startsWith(target, position)) {
                break;
            }
            position += target.length();
        }
        return lastPos == position ? Strings.EMPTY : mInput.substring(lastPos, position);
    }

    /**
     *
     * @since 1.0
     */
    public String consumeNonChar(char c) {
        checkState(hasMore());
        final int lastPos = position;
        while(hasMore()) {
            if (c == previewChar()) {
                break;
            }
            position ++;
        }
        return lastPos == position ? Strings.EMPTY : mInput.substring(lastPos, position);
    }

    /**
     *
     * @since 1.0
     */
    public String consumeNonTokens(char...chars) {
        checkState(hasMore());
        Arrays.sort(chars);
        final int lastPos = position;
        while(hasMore()) {
            if (ArraysCompat.binarySearch(chars, 0, chars.length, previewChar()) >= 0) {
                break;
            }
            position ++;
        }
        return lastPos == position ? Strings.EMPTY : mInput.substring(lastPos, position);
    }

    /**
     *
     * @since 1.0
     */
    public String consumeNonTokens(String tokens) {
        return consumeNonTokens(tokens.toCharArray());
    }

    /**
     *
     * @since 1.0
     */
    public String consumeNonTarget(String target) {
        checkNotNull(target);
        if (Objects.isEmpty(target)) {
            return Strings.EMPTY;
        }
        checkState(hasMore());
        final int lastPos = position;
        int index = mInput.indexOf(target, position);
        if (index >= 0) {
            position = index;
        } else {
            position = mInput.length();
        }
        return lastPos == position ? Strings.EMPTY : mInput.substring(lastPos, position);
    }

    /**
     * preview char at current index
     *
     * @since 1.0
     */
    public char previewChar() {
        checkState(hasMore());
        return mInput.charAt(position);
    }

    /**
     * 判断是否有更多的字符
     *
     * @since 1.0
     */
    public boolean hasMore() {
        return (position >= 0) && (position < mInput.length());
    }

    /**
     * 重置当前位置
     *
     * @since 1.0
     */
    public void reset() {
        this.position = 0;
    }

}
