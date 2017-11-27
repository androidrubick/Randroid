/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pub.androidrubick.base.net;

/**
 * Contains constant definitions for the HTTP header field values & value fields. See:
 * <ul>
 * <li><a href="http://www.ietf.org/rfc/rfc2109.txt">RFC 2109</a>
 * <li><a href="http://www.ietf.org/rfc/rfc2183.txt">RFC 2183</a>
 * <li><a href="http://www.ietf.org/rfc/rfc2616.txt">RFC 2616</a>
 * <li><a href="http://www.ietf.org/rfc/rfc2965.txt">RFC 2965</a>
 * <li><a href="http://www.ietf.org/rfc/rfc5988.txt">RFC 5988</a>
 * </ul>
 *
 * from guava
 *
 * @since 1.0
 */
public final class HttpHeaderValues {
    private HttpHeaderValues() {
    }

    /**
     * @see HttpHeaders#CONNECTION
     *
     * @since 1.0
     */
    public static final String KEEP_ALIVE = "Keep-Alive";

    /**
     * @see HttpHeaders#CONTENT_ENCODING
     *
     * @since 1.0
     */
    public static final String CONTENT_ENCODING_GZIP = "gzip";


    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // optional parameter keys
    /**
     * 参数名 boundary
     *
     * @since 1.0
     */
    public static final String P_BOUNDARY = "boundary";

    /**
     * 参数名 charset
     *
     * @since 1.0
     */
    public static final String P_CHARSET = "charset";
}
