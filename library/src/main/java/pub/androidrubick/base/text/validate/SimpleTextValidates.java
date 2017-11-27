package pub.androidrubick.base.text.validate;

import java.util.regex.Pattern;

import pub.androidrubick.base.text.Patterns;
import pub.androidrubick.base.text.Strings;

/**
 * 简单的字符串验证类
 *
 * <p/>
 *
 * Created by yong01.yin on 2014/12/29.
 *
 * @since 1.0
 */
public class SimpleTextValidates {

    private SimpleTextValidates() {}

    public static final String PATTERN_NUMBER = "^(\\-)?\\d+(\\.\\d+)?$";
    public static final String PATTERN_CHINESE = "^" + Patterns.LanguagePatterns.CHINESE_CHAR + "+$";
    public static final String PATTERN_NONE_CHINESE = "^" + Patterns.LanguagePatterns.NON_CHINESE_CHAR + "+$";

    /**
     * 判断字符串是否是手机号（简单版）：十一位数字，以1为开头的
     *
     * @since 1.0
     */
    public static boolean simpleValidateCellphone(CharSequence charSequence) {
        return checkEmptyThenValidate(Patterns.PhonePatterns.CELL_PHONE, charSequence);
    }

    public static boolean validateMail(CharSequence charSequence) {
        return checkEmptyThenValidate(Patterns.EmailPatterns.EMAIL, charSequence);
    }

    /**
     * 是否是纯数字（没有小数点和正负号）
     *
     * @since 1.0
     */
    public static boolean isPureNumber(CharSequence charSequence) {
        if (Strings.isEmpty(charSequence)) {
            return false;
        }
        for (int i = 0; i < charSequence.length(); i++) {
            if (!Character.isDigit(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是纯字母
     *
     * @since 1.0
     */
    public static boolean isPureLetter(CharSequence charSequence) {
        if (Strings.isEmpty(charSequence)) {
            return false;
        }
        for (int i = 0; i < charSequence.length(); i++) {
            if (!Character.isLetter(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是纯数字和字母
     */
    public static boolean isLetterOrNumber(CharSequence charSequence) {
        if (Strings.isEmpty(charSequence)) {
            return false;
        }
        for (int i = 0; i < charSequence.length(); i++) {
            if (!Character.isLetterOrDigit(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是数字（实数，整数，小数，负数等）
     *
     * @since 1.0
     */
    public static boolean isNumber(CharSequence charSequence) {
        return checkEmptyThenValidate(PATTERN_NUMBER, charSequence);
    }

    public static boolean simpleValidateUsername(CharSequence charSequence, int minLen, int maxlen) {
        return checkEmptyThenValidate("^[a-z][a-z0-9_]{" + Math.max(0, minLen - 1) + "," + Math.max(0, maxlen - 1) + "}$", charSequence);
    }

    /**
     * 简单验证是否全是中文字符（一般用作单行表单数据验证）
     *
     * @since 1.0
     */
    public static boolean isChinese(CharSequence charSequence) {
        return checkEmptyThenValidate(PATTERN_CHINESE, charSequence);
    }

    /**
     * 简单验证是否含有中文字符（一般用作单行表单数据验证）
     *
     * @since 1.0
     */
    public static boolean hasChinese(CharSequence charSequence) {
        return !checkEmptyThenValidate(PATTERN_NONE_CHINESE, charSequence);
    }

    /**
     * 简单验证是否是正确的URL（[schema://]domain[:port][/path][?query][#fragment]）
     *
     * @since 1.0
     */
    public static boolean isValidUrl(CharSequence charSequence) {
        return checkEmptyThenValidate(Patterns.URLPatterns.URL, charSequence);
    }

    private static boolean checkEmptyThenValidate(String regex, CharSequence input) {
        return !Strings.isEmpty(input) && Pattern.matches(regex, input);
    }
}
