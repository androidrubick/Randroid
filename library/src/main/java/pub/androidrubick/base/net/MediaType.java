package pub.androidrubick.base.net;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pub.androidrubick.base.collection.CollectionsCompat;
import pub.androidrubick.base.collection.MapBuilder;
import pub.androidrubick.base.text.Charsets;
import pub.androidrubick.base.text.MapJoiner;
import pub.androidrubick.base.text.SimpleTokenizer;
import pub.androidrubick.base.text.Strings;
import pub.androidrubick.base.utils.Objects;
import pub.androidrubick.base.utils.Preconditions;

/**
 * 媒体类型
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/5/17 0017.
 *
 * @since 1.0
 */
public class MediaType {

    // TODO(gak): make these public?
    public static final String APPLICATION_TYPE = "application";
    public static final String TEXT_TYPE = "text";
    public static final String IMAGE_TYPE = "image";
    public static final String AUDIO_TYPE = "audio";
    public static final String VIDEO_TYPE = "video";
    /**
     * 通配符
     *
     * @since 1.0
     */
    public static final String WILDCARD = "*";

    /**
     * 单纯含有{ charset : UTF-8 }的Map
     *
     * @since 1.0
     */
    public static final Map<String, String> UTF_8_CONSTANT_PARAMETERS;
    static {
        HashMap<String, String> map = new HashMap<String, String>(1);
        map.put(HttpHeaderValues.P_CHARSET, Charsets.UTF_8.name());
        UTF_8_CONSTANT_PARAMETERS = Collections.unmodifiableMap(map);
    }

    private static final Map<MediaType, MediaType> KNOWN_TYPES = new HashMap<MediaType, MediaType>(16);

    private static MediaType createConstant(String type, String subtype) {
        return addKnownType(new MediaType(type, subtype, null));
    }

    private static MediaType addKnownType(MediaType mediaType) {
        KNOWN_TYPES.put(mediaType, mediaType);
        return mediaType;
    }

    /**
     * 通配类型。
     *
     * @since 1.0
     */
    public static final MediaType ANY = createConstant(WILDCARD, WILDCARD);

    /**
     * 文本类型的通配类型。
     *
     * @since 1.0
     */
    public static final MediaType ANY_TEXT = createConstant(TEXT_TYPE, WILDCARD);

    /**
     * 图片类型的通配类型。
     *
     * @since 1.0
     */
    public static final MediaType ANY_IMAGE = createConstant(IMAGE_TYPE, WILDCARD);

    /**
     * 音频类型的通配类型。
     *
     * @since 1.0
     */
    public static final MediaType ANY_AUDIO = createConstant(AUDIO_TYPE, WILDCARD);

    /**
     * 视频类型的通配类型。
     *
     * @since 1.0
     */
    public static final MediaType ANY_VIDEO = createConstant(VIDEO_TYPE, WILDCARD);

    /**
     * 应用类型的通配类型。
     *
     * @since 1.0
     */
    public static final MediaType ANY_APPLICATION = createConstant(APPLICATION_TYPE, WILDCARD);

    // for text types
    public static final MediaType TEXT_PLAIN = createConstant(TEXT_TYPE, "plain");
    public static final MediaType TEXT_CSS = createConstant(TEXT_TYPE, "css");
    public static final MediaType TEXT_CSV = createConstant(TEXT_TYPE, "csv");
    public static final MediaType TEXT_HTML = createConstant(TEXT_TYPE, "html");

    /**
     * <a href="http://www.rfc-editor.org/rfc/rfc4329.txt">RFC 4329</a> declares
     * {@link #APPLICATION_JAVASCRIPT application/javascript} to be the correct media type for JavaScript,
     * but this may be necessary in certain situations for compatibility.
     */
    public static final MediaType TEXT_JAVASCRIPT = createConstant(TEXT_TYPE, "javascript");
    /**
     * As described in <a href="http://www.ietf.org/rfc/rfc3023.txt">RFC 3023</a>, this constant
     * ({@code text/xml}) is used for XML documents that are "readable by casual users."
     * {@link #APPLICATION_XML} is provided for documents that are intended for applications.
     */
    public static final MediaType TEXT_XML = createConstant(TEXT_TYPE, "xml");

    public static final MediaType WML = createConstant(TEXT_TYPE, "vnd.wap.wml");

    // for image types
    public static final MediaType BMP = createConstant(IMAGE_TYPE, "bmp");
    /**
     * The media type for the <a href="http://en.wikipedia.org/wiki/Camera_Image_File_Format">Canon
     * Image File Format</a> ({@code crw} files), a widely-used "raw image" format for cameras. It is
     * found in {@code /etc/mime.types}, e.g. in <href=
     * "http://anonscm.debian.org/gitweb/?p=collab-maint/mime-support.git;a=blob;f=mime.types;hb=HEAD"
     * >Debian 3.48-1</a>.
     */
    public static final MediaType CRW = createConstant(IMAGE_TYPE, "x-canon-crw");
    public static final MediaType GIF = createConstant(IMAGE_TYPE, "gif");
    public static final MediaType ICO = createConstant(IMAGE_TYPE, "vnd.microsoft.icon");
    public static final MediaType JPEG = createConstant(IMAGE_TYPE, "jpeg");
    public static final MediaType PNG = createConstant(IMAGE_TYPE, "png");
    /**
     * The media type for the Photoshop File Format ({@code psd} files) as defined by <a href=
     * "http://www.iana.org/assignments/media-types/image/vnd.adobe.photoshop">IANA</a>, and found in
     * {@code /etc/mime.types}, e.g. <a href=
     * "http://svn.apache.org/repos/asf/httpd/httpd/branches/1.3.x/conf/mime.types"></a> of the Apache
     * <a href="http://httpd.apache.org/">HTTPD project</a>; for the specification, see
     * <href="http://www.adobe.com/devnet-apps/photoshop/fileformatashtml/PhotoshopFileFormats.htm">
     * Adobe Photoshop Document Format</a> and <a href=
     * "http://en.wikipedia.org/wiki/Adobe_Photoshop#File_format">Wikipedia</a>; this is the regular
     * output/input of Photoshop (which can also export to various image formats; note that files with
     * extension "PSB" are in a distinct but related format).
     * <p>This is a more recent replacement for the older, experimental type
     * {@code x-photoshop}: <a href="http://tools.ietf.org/html/rfc2046#section-6">RFC-2046.6</a>.
     *
     */
    public static final MediaType PSD = createConstant(IMAGE_TYPE, "vnd.adobe.photoshop");
    public static final MediaType SVG = createConstant(IMAGE_TYPE, "svg+xml");
    public static final MediaType TIFF = createConstant(IMAGE_TYPE, "tiff");
    public static final MediaType WEBP = createConstant(IMAGE_TYPE, "webp");

    /* audio types */
    public static final MediaType MP4_AUDIO = createConstant(AUDIO_TYPE, "mp4");
    public static final MediaType MPEG_AUDIO = createConstant(AUDIO_TYPE, "mpeg");
    public static final MediaType OGG_AUDIO = createConstant(AUDIO_TYPE, "ogg");
    public static final MediaType WEBM_AUDIO = createConstant(AUDIO_TYPE, "webm");

    /* video types */
    public static final MediaType MP4_VIDEO = createConstant(VIDEO_TYPE, "mp4");
    public static final MediaType MPEG_VIDEO = createConstant(VIDEO_TYPE, "mpeg");
    public static final MediaType OGG_VIDEO = createConstant(VIDEO_TYPE, "ogg");
    public static final MediaType QUICKTIME = createConstant(VIDEO_TYPE, "quicktime");
    public static final MediaType WEBM_VIDEO = createConstant(VIDEO_TYPE, "webm");
    public static final MediaType WMV = createConstant(VIDEO_TYPE, "x-ms-wmv");

    /* application types */

    // for json
    /**
     * application/json
     */
    public static final MediaType JSON = createConstant(APPLICATION_TYPE, "json");

    /**
     * <a href="http://www.rfc-editor.org/rfc/rfc4329.txt">RFC 4329</a> declares this to be the
     * correct media type for JavaScript, but {@link #TEXT_JAVASCRIPT text/javascript} may be
     * necessary in certain situations for compatibility.
     */
    public static final MediaType APPLICATION_JAVASCRIPT = createConstant(APPLICATION_TYPE, "javascript");

    // media type for form data
    public static final MediaType FORM_DATA_URLENCODED = createConstant(APPLICATION_TYPE,
            "x-www-form-urlencoded");
    public static final MediaType FORM_DATA_MULTIPART = createConstant("multipart",
            "form-data");

    /**
     * As described in <a href="http://www.ietf.org/rfc/rfc3023.txt">RFC 3023</a>, this constant
     * ({@code application/xml}) is used for XML documents that are "unreadable by casual users."
     * {@link #TEXT_XML} is provided for documents that may be read by users.
     */
    public static final MediaType APPLICATION_XML = createConstant(APPLICATION_TYPE, "xml");

    public static final MediaType XHTML = createConstant(APPLICATION_TYPE, "xhtml+xml");

    public static final MediaType ZIP = createConstant(APPLICATION_TYPE, "zip");
    public static final MediaType GZIP = createConstant(APPLICATION_TYPE, "x-gzip");

    public static final MediaType OCTET_STREAM = createConstant(APPLICATION_TYPE, "octet-stream");

    /**
     * 指定特定主类型的通配类型，如text/*
     */
    public static MediaType of(String type) {
        return of(type, WILDCARD);
    }

    public static MediaType of(String type, String subtype) {
        return of(type, subtype, (Map) null);
    }

    public static MediaType of(String type, String subtype, String charset) {
        return of(type, subtype, MapBuilder.newHashMap().put(HttpHeaderValues.P_CHARSET, charset).build());
    }

    public static MediaType of(String type, String subtype, Map<String, String> parameters) {
        return new MediaType(type, subtype, parameters);
    }

    private final String type;
    private final String subtype;
    private Map<String, String> parameters;
    private String mDisplayName;
    private MediaType(String type, String subtype, Map<String, String> parameters) {
        Preconditions.checkNotNull(type, "type is null");
        Preconditions.checkNotNull(subtype, "subtype is null");
        this.type = type;
        this.subtype = subtype;
        this.parameters = parameters;
        this.mDisplayName = generateName();
    }

    /**
     * 获取该MediaType的名称，如text/plain
     *
     * @since 1.0
     */
    public String name() {
        return mDisplayName;
    }

    /**
     * 附加一个新的参数
     *
     * @return 一个新的对象
     *
     * @since 1.0
     */
    public MediaType withParameter(String key, String value) {
        return withParameters(MapBuilder
                .newHashMap(1 + CollectionsCompat.size(this.parameters))
                        .put(key, value)
                .build());
    }

    /**
     * 附加一些新的参数
     *
     * @return 一个新的对象，如果参数为空Map，则返回当前对象
     *
     * @since 1.0
     */
    public MediaType withParameters(Map<String, String> map) {
        if (CollectionsCompat.isEmpty(map)) {
            return this;
        }
        return of(this.type, this.subtype, CollectionsCompat.putAll(map, this.parameters));
    }

    /**
     * 不包含当前对象参数的一个“新对象”
     *
     * @return 如果当前对象含有参数，则返回一个新的对象；如果参数为空Map，则返回当前对象
     *
     * @since 1.0
     */
    public MediaType withoutParameters() {
        if (CollectionsCompat.isEmpty(parameters)) {
            return this;
        }
        return of(this.type, this.subtype, (Map) null);
    }

    /**
     * 向当前对象添加/覆盖{@link HttpHeaderValues#P_CHARSET charset}参数
     *
     * @return 一个新的对象
     *
     * @since 1.0
     */
    public MediaType withCharset(String charset) {
        return withParameter(HttpHeaderValues.P_CHARSET, Preconditions.checkNotNull(charset));
    }

    /**
     *
     * @since 1.0
     */
    public String type() {
        return type;
    }

    /**
     *
     * @since 1.0
     */
    public String subtype() {
        return subtype;
    }

    /** Returns true if either the type or subtype is the wildcard.
     *
     * @since 1.0 */
    public boolean hasWildcard() {
        return WILDCARD.equals(type) || WILDCARD.equals(subtype);
    }

    /**
     *
     * @since 1.0
     */
    public String charset() {
        return CollectionsCompat.isEmpty(this.parameters) ? null :
                this.parameters.get(HttpHeaderValues.P_CHARSET);
    }

    protected String generateName() {
        final String typeSep = "/";
        final String parSep = "; ";
        final int len = type.length() + subtype.length() + typeSep.length() + CollectionsCompat.size(parameters) * 32;
        final StringBuilder builder = new StringBuilder(len)
                .append(type).append(typeSep).append(subtype);
        if (!CollectionsCompat.isEmpty(parameters)) {
            builder.append(parSep);
            MapJoiner.by(parSep, "=")
                    .appendTo(builder, parameters);
        }
        return builder.toString();
    }

    /**
     * 当前对象是否属于{@code mediaTypeRange}，该方法仅仅比较类型和二级类型
     *
     * @since 1.0
     */
    public boolean is(MediaType mediaTypeRange) {
        return (Objects.equals(mediaTypeRange.type, WILDCARD) || Objects.equals(mediaTypeRange.type, this.type))
                && (Objects.equals(mediaTypeRange.subtype, WILDCARD) || Objects.equals(mediaTypeRange.subtype, this.subtype));
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.subtype, this.parameters);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof MediaType) {
            MediaType that = (MediaType) obj;
            return Objects.equals(this.type, that.type)
                    && Objects.equals(this.subtype, that.subtype)
                    && Objects.equals(this.parameters, that.parameters);
        } else {
            return false;
        }
    }

    private static final char TYPE_END_TOKENS = '/';
    private static final String SUBTYPE_END_TOKENS = "()<>@,;:\\\"/[]?= ";
    private static final String PARAM_KEY_END_TOKENS = SUBTYPE_END_TOKENS;
    private static final String PARAM_VALUE_END_TOKENS = PARAM_KEY_END_TOKENS;

    public static MediaType parseOrNull(String origin) {
        try {
            return parse(origin);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @throws NullPointerException if {@code origin} is null
     * @throws IllegalArgumentException if {@code origin} is of invalid format
     *
     * @since 1.0
     */
    public static MediaType parse(String origin) {
        Preconditions.checkNotNull(origin);
        SimpleTokenizer tokenizer = new SimpleTokenizer(origin);
        String type = tokenizer.consumeNonChar(TYPE_END_TOKENS);
        Preconditions.checkArgument(!Strings.isEmpty(type), "no type found");
        tokenizer.consumeChar(TYPE_END_TOKENS);
        Preconditions.checkArgument(tokenizer.hasMore(), "no subtype found");
        String subtype = tokenizer.consumeNonTokens(SUBTYPE_END_TOKENS);
        Preconditions.checkArgument(!Strings.isEmpty(subtype), "no subtype found");
        tokenizer.consumeTokens(SUBTYPE_END_TOKENS);

        if (!tokenizer.hasMore()) {
            return MediaType.of(type, subtype);
        }

        Map<String, String> paramMap = null;
        while (tokenizer.hasMore()) {
            String key = tokenizer.consumeNonTokens(PARAM_KEY_END_TOKENS);
            Preconditions.checkArgument(!Strings.isEmpty(key), "err key");
            if (tokenizer.hasMore()) {
                tokenizer.consumeTokens(PARAM_KEY_END_TOKENS);
            }
            Preconditions.checkArgument(tokenizer.hasMore(), "no value found of key %s", key);
            String value = tokenizer.consumeNonTokens(PARAM_VALUE_END_TOKENS);
            Preconditions.checkArgument(!Strings.isEmpty(value), "no value found of key %s", key);
            if (tokenizer.hasMore()) {
                tokenizer.consumeTokens(PARAM_VALUE_END_TOKENS);
            }
            if (Objects.isNull(paramMap)) {
                paramMap = new HashMap<>(2);
            }
            paramMap.put(key, value);
        }
        if (Objects.isNull(paramMap)) {
            return MediaType.of(type, subtype);
        }
        return MediaType.of(type, subtype, paramMap);
    }
}
