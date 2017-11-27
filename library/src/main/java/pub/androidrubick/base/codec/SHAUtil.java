package pub.androidrubick.base.codec;

import java.security.MessageDigest;

/**
 * 生成sha校验码
 *
 * @since 1.0
 */
public class SHAUtil {

    public static byte[] digest2Bytes(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            return md.digest(bytes);
        } catch (Exception localNoSuchAlgorithmException) {
            return null;
        }
    }

    /**
     * digest <code>bytes</code>
     * @param bytes raw byte array
     */
    public static String digest2Str(byte[] bytes) {
        byte[] dBytes = digest2Bytes(bytes);
        if (null != dBytes) {
            return CByte.bytes2Hex(digest2Bytes(bytes));
        }
        return null;
    }

    /**
     * digest <code>str</code>, using the default charset
     * @param str raw string
     */
    public static String digest2Str(String str) {
        return digest2Str(str.getBytes());
    }
}
