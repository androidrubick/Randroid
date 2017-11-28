package pub.androidrubick.base.io;

import java.io.File;

import pub.androidrubick.base.utils.ArraysCompat;
import pub.androidrubick.base.utils.NumberUtils;

/**
 * util to produce / get file information
 *
 * <p></p>
 *
 * Created by Yin Yong on 2017/11/27.
 *
 * @since 1.0.0
 */
public class FileInfos {

    /**
     * 简单地生成文件大小的字符串，如100.00KB，10.20M, e.g.
     *
     * @see NumberUtils#formatByUnit(double, double, double, int, String...)
     * @see NumberUtils#formatByUnit(double, double, int, String...)
     * @since 1.0
     */
    public static String calFileSizeString(double bytes) {
        if (0D >= bytes) {
            bytes = 0D;
        }
        return NumberUtils.formatByUnit(bytes, 1024D, 900D, 2, "B", "KB", "M", "G", "T");
    }

    /**
     * 计算指定文件/文件夹的大小
     *
     * @since 1.0
     */
    public static long calFileSize(File file) {
        if (!Files.exists(file)) {
            return 0L;
        }
        if (file.isDirectory()) {
            long size = 0L;
            File[] childFile = file.listFiles();
            if (!ArraysCompat.isEmpty(childFile)) {
                for (File cFile : childFile) {
                    size += calFileSize(cFile);
                }
            }
            return size;
        } else {
            return file.length();
        }
    }

    /**
     * 计算指定文件夹中的文件和文件夹的数量；
     * <p>
     * <p/>
     * <p>
     * 返回长度为3，数组第一项为文件数，第二项为文件夹数，第三项为总数目
     *
     * @param recursive     是否递归地计算子文件夹
     * @param includeHidden 是否包括隐藏文件/文件夹
     * @since 1.0
     */
    public static int[] calFileAndDirCount(File path, boolean recursive, boolean includeHidden) {
        int[] target = new int[3];
        calFileAndDirCount(path, target, recursive, includeHidden);
        target[2] = target[0] + target[1];
        return target;
    }

    /**
     * 计算指定文件夹中的文件和文件夹的数量；
     * <p/>
     *
     * @param target        数组第一项为文件数，第二项为文件夹数
     * @param recursive     是否递归地计算子文件夹
     * @param includeHidden 是否包括隐藏文件/文件夹
     * @since 1.0
     */
    public static void calFileAndDirCount(File path, int target[], boolean recursive, boolean includeHidden) {
        if (path.isDirectory()) {
            File[] childFile = path.listFiles();
            if (ArraysCompat.isEmpty(childFile)) return;

            for (File cFile : childFile) {
                // 如果不需要计算隐藏文件，直接继续循环
                if (cFile.isHidden() && !includeHidden) continue;

                if (cFile.isDirectory()) {
                    if (ArraysCompat.len(target) > 1)
                        target[1] += 1;
                    // 如果不需要递归调用，则直接继续循环
                    if (!recursive) continue;
                }
                calFileAndDirCount(cFile, target, recursive, includeHidden);
            }
        } else if (path.isFile()) {
            if (ArraysCompat.len(target) > 0)
                target[0] += 1;
        }
    }

}
