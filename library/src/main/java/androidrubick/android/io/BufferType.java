package androidrubick.android.io;

import android.support.annotation.IntRange;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/11/28.
 *
 * @since 1.0.0
 */
public enum BufferType {

    Byte {
        @Override
        public long toBytes(@IntRange(from = 1) long size) {
            return size;
        }

        @Override
        public long toChars(@IntRange(from = 1) long size) {
            return Math.max(1, size / 2);
        }
    },

    Char {
        @Override
        public long toBytes(@IntRange(from = 1) long size) {
            return x(size, 2, Long.MAX_VALUE / 2);
        }

        @Override
        public long toChars(@IntRange(from = 1) long size) {
            return size;
        }
    };

    /**
     * Scale d by m, checking for overflow.
     * This has a short name to make above code more readable.
     */
    static long x(long d, long m, long over) {
        if (d > +over) return Long.MAX_VALUE;
        if (d < -over) return Long.MIN_VALUE;
        return d * m;
    }

    /**
     * @since 1.0.0
     */
    public abstract long toBytes(@IntRange(from = 1) long size) ;

    /**
     * @since 1.0.0
     */
    public abstract long toChars(@IntRange(from = 1) long size) ;
}
