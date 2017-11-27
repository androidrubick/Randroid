package pub.androidrubick.base.utils;

/**
 * 针对Function的操作
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/3/27.
 *
 * @since 1.0
 */
public class Functions {

    private Functions() {}

    private static class ToStringFunc implements Function<Object, String> {
        @Override
        public String apply(Object input) {
            return Objects.toString(input);
        }
    }

    /**
     * 一个类似调用{@link Object#toString()}的函数
     *
     * @see Objects#toString(Object)
     *
     * @since 1.0
     */
    public static final ToStringFunc TO_STRING = new ToStringFunc();

}
