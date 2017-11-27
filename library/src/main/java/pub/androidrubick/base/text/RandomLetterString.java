package pub.androidrubick.base.text;

import java.util.Random;

import pub.androidrubick.base.utils.MathPreconditions;

/**
 * {@doc}
 * <p>
 * Created by Yin Yong on 2017/11/24.
 *
 * @since 1.0
 */
public class RandomLetterString {

    private final int mLen;
    public RandomLetterString(int len) {
        mLen = MathPreconditions.checkPositive("RandomString len", len);
    }

    public String next() {
        char[] letterArray = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                        'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
        };

        final int total = letterArray.length;
        final int n = mLen;
        Random rd = new Random();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < n; i++)// 随即10个拿出来看看
        {
            buffer.append(letterArray[rd.nextInt(total)]);
        }
        return buffer.toString();
    }

}
