package pub.androidrubick.base.net;

/**
 * Contains constant definitions for the HTTP methods.
 *
 * <p/>
 * <p/>
 * Created by Yin Yong on 15/5/15.
 *
 * @since 1.0
 */
public enum HttpMethod {

    /**
     * @since 1.0
     */
    GET("GET"),

    /**
     * @since 1.0
     */
    POST("POST") {
        @Override
        public boolean canContainBody() {
            return true;
        }
    },

    /**
     * @since 1.0
     */
    PUT("PUT") {
        @Override
        public boolean canContainBody() {
            return true;
        }
    },

    /**
     * @since 1.0
     */
    DELETE("DELETE"),

    /**
     * @since 1.0
     */
    HEAD("HEAD"),

    /**
     * @since 1.0
     */
    OPTIONS("OPTIONS"),

    /**
     * @since 1.0
     */
    TRACE("TRACE"),

    /**
     * @since 1.0
     */
    PATCH("PATCH") {
        @Override
        public boolean canContainBody() {
            return true;
        }
    };

    private String name;

    HttpMethod(String name) {
        this.name = name;
    }

    /**
     * 获取请求方法名称
     *
     * @since 1.0
     */
    public String getName() {
        return this.name;
    }

    /**
     * 是否能包含请求体
     *
     * @since 1.0
     */
    public boolean canContainBody() {
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }
}
