package androidrubick.android.view;

import android.view.View;
import android.widget.AdapterView;

/**
 * {@doc}
 * <p/>
 * Created by Yin Yong on 2017/3/19.
 *
 * @since 1.0.0
 */
public class Views {

    /**
     * @since 1.0.0
     */
    public static void setVisible(View view, boolean show) {
        if (null != view) {
            view.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * @since 1.0.0
     */
    public static void gone(View view) {
        if (null != view) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * @since 1.0.0
     */
    public static void visible(View view) {
        if (null != view) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @since 1.0.0
     */
    public static void invisible(View view) {
        if (null != view) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @since 1.0.0
     */
    public static boolean isVisible(View view) {
        return isVisibilityEqual(view, View.VISIBLE);
    }

    /**
     * @since 1.0.0
     */
    public static boolean isInvisible(View view) {
        return isVisibilityEqual(view, View.INVISIBLE);
    }

    /**
     * @since 1.0.0
     */
    public static boolean isGone(View view) {
        return isVisibilityEqual(view, View.GONE);
    }

    private static boolean isVisibilityEqual(View view, int visibility) {
        if (null == view) {
            return false;
        }
        return view.getVisibility() == visibility;
    }

    /**
     * View的最大宽度；int一共32位，在View中前8位用来标识{@link View#MEASURED_STATE_MASK}
     *
     * 的模式
     *
     * @see View#MEASURED_SIZE_MASK
     * @see View#MEASURED_STATE_MASK
     * @since 1.0.0
     */
    public static int maxWidth() {
        return View.MEASURED_SIZE_MASK;
    }

    /**
     * View的最大高度；int一共32位，在View中前8位用来标识{@link View#MEASURED_STATE_MASK}
     *
     * 的模式
     *
     * @see View#MEASURED_SIZE_MASK
     * @see View#MEASURED_STATE_MASK
     * @since 1.0.0
     */
    public static int maxHeight() {
        return View.MEASURED_SIZE_MASK;
    }

    /**
     * @since 1.0.0
     */
    public static void click(View v, View.OnClickListener l) {
        if (null != v) {
            v.setOnClickListener(l);
        }
    }

    /**
     * @since 1.0.0
     */
    public static void longClick(View v, View.OnLongClickListener l) {
        if (null != v) {
            v.setOnLongClickListener(l);
        }
    }

    /**
     * @since 1.0.0
     */
    public static void itemClick(AdapterView<?> v, AdapterView.OnItemClickListener l) {
        if (null != v) {
            v.setOnItemClickListener(l);
        }
    }

}
