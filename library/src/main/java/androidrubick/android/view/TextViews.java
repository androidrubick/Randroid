package androidrubick.android.view;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

/**
 * {@doc}
 * <p></p>
 * Created by Yin Yong on 2017/12/22.
 *
 * @since 1.0.0
 */
public class TextViews {

    private TextViews() { /* no instance needed */ }

    /**
     * @return param <code>tv</code>
     * @since 1.0.0
     */
    public static <TV extends TextView>TV text(TV tv, CharSequence text) {
        if (null != tv) {
            tv.setText(text);
        }
        return tv;
    }

    /**
     * @return param <code>tv</code>
     * @since 1.0.0
     */
    public static <TV extends TextView>TV text(TV tv, int resId) {
        return null == tv ? null : text(tv, tv.getResources().getText(resId));
    }

    /**
     * 给{@link EditText}设置文本，并将指针移到最后的位置
     *
     * @return param <code>et</code>
     * @since 1.0.0
     */
    public static <ET extends EditText>ET text(ET et, CharSequence text) {
        if (null != et) {
            et.setText(text);
            if (!TextUtils.isEmpty(et.getText())) {
                et.setSelection(et.getText().length());
            }
        }
        return et;
    }

    /**
     * 给{@link EditText}设置文本，并将指针移到最后的位置
     *
     * @return param <code>et</code>
     * @since 1.0.0
     */
    public static <ET extends EditText>ET text(ET et, int resId) {
        return null == et ? null : text(et, et.getResources().getText(resId));
    }

}
