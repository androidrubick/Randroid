package pub.androidrubick.base.io;

import java.nio.charset.Charset;

import pub.androidrubick.base.BuildConfig;

/**
 * IO 操作中的相关常量
 * 
 * @author Yin Yong
 *
 * @since 1.0
 *
 */
public interface IOConstants {
	/**
	 * 默认的字符大小（用于传输/缓存等）
     *
     * @since 1.0
	 */
	int DEF_BUFFER_SIZE = 8192;

	/**
	 * 默认的编码
	 *
	 * @since 1.0
	 */
	String DEF_CHARSET_NAME = BuildConfig.ProjectEncoding;

	/**
	 * 默认的编码
     *
     * @since 1.0
	 */
	Charset DEF_CHARSET = Charset.forName(DEF_CHARSET_NAME);
}
