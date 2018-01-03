package androidrubick.android.io;

/**
 * <p></p>
 * Created by Yin Yong on 2017/11/28.
 *
 * @since 1.0.0
 */
public enum BufferType {

    Bit {
        @Override
        public long toBits(long size) {
            return size;
        }

        @Override
        public long toBytes(long size) {
            return caseSignDivide(size, 8);
        }

        @Override
        public long toChars(long size) {
            return caseSignDivide(size, 16);
        }
    },

    Byte {
        @Override
        public long toBits(long size) {
            return caseSignX(size, 8, Long.MAX_VALUE / 8);
        }

        @Override
        public long toBytes(long size) {
            return size;
        }

        @Override
        public long toChars(long size) {
            return caseSignDivide(size, 2);
        }
    },

    Char {
        @Override
        public long toBits(long size) {
            return caseSignX(size, 2 * 8, Long.MAX_VALUE / (2 * 8));
        }

        @Override
        public long toBytes(long size) {
            return caseSignX(size, 2, Long.MAX_VALUE / 2);
        }

        @Override
        public long toChars(long size) {
            return size;
        }
    };

    static long caseSignDivide(long d, long divider) {
        if (d == 0) {
            return 0;
        }
        long result = Math.max(1, Math.abs(d) / divider);
        return d < 0 ? - result : result;
    }

    static long caseSignX(long d, long m, long over) {
        long result = x(Math.abs(d), m, over);
        return d < 0 ? - result : result;
    }

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
    public abstract long toBits(long size) ;

    /**
     * @since 1.0.0
     */
    public abstract long toBytes(long size) ;

    /**
     * @since 1.0.0
     */
    public abstract long toChars(long size) ;
}
