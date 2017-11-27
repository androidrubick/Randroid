package pub.androidrubick.android.view;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;

import androidrubick.base.R;

import static pub.androidrubick.base.utils.Preconditions.checkNotNull;

/**
 * 工具类，用于直接获取内部View，内部封装了ViewHolder的逻辑。
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/4/11 0011.
 */
public class ViewHolderUtil {

    private ViewHolderUtil() { /* no instance needed */ }

    /**
     * NOTE: <b>该方法将会覆盖convert view的tag</b>
     *
     * @param view convert view
     * @param id child view id
     * @param <T>
     * @return target view object
     */
    public static <T extends View> T get(@NonNull View view, @IdRes int id) {
        checkNotNull(view, "param view is null");
        Object tag = view.getTag(R.id.ar_base_view_holder);
        if (!(tag instanceof SparseArray)) { // clear tag
            view.setTag(null);
            tag = null;
        }
        if (null == tag) {
            view.setTag(R.id.ar_base_view_holder, tag = new SparseArray<>());
        }
        SparseArray<View> viewHolder = (SparseArray<View>) tag;
        View childView = viewHolder.get(id);
        if (null == childView) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

}