package pub.androidrubick.base.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import pub.androidrubick.base.collection.CollectionsCompat;
import pub.androidrubick.base.text.MapJoiner;
import pub.androidrubick.base.text.Strings;
import pub.androidrubick.base.utils.Exceptions;
import pub.androidrubick.base.utils.Function;

/**
 * A Uniform Resource Locator that identifies the location of an Internet
 * resource as specified by <a href="http://www.ietf.org/rfc/rfc1738.txt">RFC
 * 1738</a>.
 *
 * <h3>Parts of a URL</h3>
 * A URL is composed of many parts. This class can both parse URL strings into
 * parts and compose URL strings from parts. For example, consider the parts of
 * this URL:
 * {@code http://username:password@host:8080/directory/file?query#ref}:
 * <table>
 * <tr><th>Component</th><th>Example value</th><th>Also known as</th></tr>
 * <tr><td>{@link #getProtocol() Protocol}</td><td>{@code http}</td><td>scheme</td></tr>
 * <tr><td>{@link #getAuthority() Authority}</td><td>{@code username:password@host:8080}</td><td></td></tr>
 * <tr><td>{@link #getUserInfo() User Info}</td><td>{@code username:password}</td><td></td></tr>
 * <tr><td>{@link #getHost() Host}</td><td>{@code host}</td><td></td></tr>
 * <tr><td>{@link #getPort() Port}</td><td>{@code 8080}</td><td></td></tr>
 * <tr><td>{@link #getFile() File}</td><td>{@code /directory/file?query}</td><td></td></tr>
 * <tr><td>{@link #getPath() Path}</td><td>{@code /directory/file}</td><td></td></tr>
 * <tr><td>{@link #getQuery() Query}</td><td>{@code query}</td><td></td></tr>
 * <tr><td>{@link #getRef() Ref}</td><td>{@code ref}</td><td>fragment</td></tr>
 * </table>
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/9/10.
 *
 * @since 1.0
 */
public class HttpUrls {

    /**
     *
     * @since 1.0
     */
    public static final char SEP_QUERY = '?';

    /**
     *
     * @since 1.0
     */
    public static final char SEP_FRAGMENT = '#';

    private HttpUrls() { /* no instance needed */ }

    /**
     * 向<code>url</code>后，添加类似"a=1&b=2"的<code>queryString</code>。
     *
     * 返回组装后的url
     *
     * @param originUrl 原始url
     * @param queryString 确保其正确性
     *
     * @see #toUrlEncodedQueryString(java.util.Map, String)
     * @see #toUrlEncodedQueryString(java.util.Map, Function, Function)
     *
     * @since 1.0
     */
    public static String appendQueryString(String originUrl, String queryString) {
        if (Strings.isEmpty(queryString)) {
            return originUrl;
        }
        String url = originUrl;
        int indexOfQueryStart = originUrl.lastIndexOf(SEP_QUERY);
        if (indexOfQueryStart < 0) {
            url += "?";
        } else if (indexOfQueryStart != originUrl.length() - 1 && !originUrl.endsWith("&")) {
            url += "&";
        }
        url += queryString;
        return url;
    }

    /**
     *
     * @since 1.0
     */
    public static String appendParamsAsQueryString(String originUrl, Map<?, ?> params, final String charsetName) {
        String queryString = toUrlEncodedQueryString(params, charsetName);
        return appendQueryString(originUrl, queryString);
    }

    /**
     * 使用简单的{@link URLEncoder}将参数Map转为url请求参数：
     *
     * <pre>
     *     null --> ""
     *
     *     [] --> ""
     *
     *     [{a1:1}, {a2:2}] --> a1=1&a2=2...
     * </pre>
     *
     * @since 1.0
     */
    public static String toUrlEncodedQueryString(Map<?, ?> params, final String charsetName) {
        if (CollectionsCompat.isEmpty(params)) {
            return Strings.EMPTY;
        }
        Function<Object, String> func = toUrlEncodedStringFunc(charsetName);
        return toUrlEncodedQueryString(params, func, func);
    }

    /**
     * 根据指定的字符编码返回方法
     * @param charsetName 字符编码
     *
     * @since 1.0
     */
    public static Function<Object, String> toUrlEncodedStringFunc(final String charsetName) {
        return new Function<Object, String>() {
            @Override
            public String apply(Object input) {
                try {
                    return URLEncoder.encode(String.valueOf(input), charsetName);
                } catch (UnsupportedEncodingException e) {
                    throw Exceptions.asRuntime(e);
                }
            }
        };
    }

    /**
     * 使用自定义的toString函数将参数Map转为url请求参数：
     *
     * <pre>
     *     null --> ""
     *
     *     [] --> ""
     *
     *     [{a1:1}, {a2:2}] --> a1=1&a2=2...
     * </pre>
     *
     * @since 1.0
     */
    public static String toUrlEncodedQueryString(Map<?, ?> params,
                                                 Function<Object, ? extends CharSequence> toStringFunc) {
        return toUrlEncodedQueryString(params, toStringFunc, toStringFunc);
    }

    /**
     * 使用自定义的toString函数将参数Map转为url请求参数：
     *
     * <pre>
     *     null --> ""
     *
     *     [] --> ""
     *
     *     [{a1:1}, {a2:2}] --> a1=1&a2=2...
     * </pre>
     *
     * @since 1.0
     */
    public static String toUrlEncodedQueryString(Map<?, ?> params,
                                                 Function<Object, ? extends CharSequence> toStringOfKey,
                                                 Function<Object, ? extends CharSequence> toStringOfValue) {
        return MapJoiner.by("&", "=")
                .withToStringFuncOfKey(toStringOfKey)
                .withToStringFuncOfValue(toStringOfValue)
                .join(params);
    }

}
