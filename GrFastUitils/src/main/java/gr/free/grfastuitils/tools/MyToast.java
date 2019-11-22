package gr.free.grfastuitils.tools;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Field;

import gr.free.grfastuitils.GrUtilsInstance;


/**
 * Toast统一管理类
 */
public class MyToast {
    private static Toast toast;
    private static Toast toast1;
    /**
     * 上下文对象
     */
    private static Context mContext = GrUtilsInstance.getmContext();

    /**
     * 短时间显示Toast
     */
    public static void showShort(CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(int value) {
        String msg = value + "";
        if (null == toast) {
            toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     */
    public static void showLong(CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     */
    public static void showLong(int value) {
        String msg = value + "";
        if (null == toast) {
            toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     */
    public static void show(CharSequence message, int duration) {
        if (null == toast) {
            toast = Toast.makeText(mContext, message, duration);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     */
    public static void show(int value, int duration) {
        String msg = value + "";
        if (null == toast) {
            toast = Toast.makeText(mContext, msg, duration);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * Hide the toast, if any.
     */
    public static void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }

    /**
     * 如果我们不是单单在下面提示几个字的信息时，而且要过几秒消失不能像dialog一样，就直接把系统toast居中显示就行了
     */
    public static void showMsg(CharSequence message) {

        if (null == toast) {
            toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();

    }

    /**
     * 在顶部显示Toast
     */
    public static void showTipTop(String content) {
        if (null == toast1) {
            toast1 = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.CENTER, 0, 0);

        } else {
            toast1.setText(content);
        }
        try {
            Object mTN = null;
            mTN = getField(toast1, "mTN");
            // 通过反射判断系统toast是否为空,如果不为空就继续通过反射获取当前toast在什么位置然后在那个位置添加动画
            if (mTN != null) {
                // 通过这个参数获取toast的位置,这些都是系统隐藏的只能通过反射获取,是用object就是因为映射的时候不知道他是什么格式的
                Object mParams = getField(mTN, "mParams");

                if (mParams != null && mParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
//					params.windowAnimations = R.style.Lite_Animation_Toast;
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        toast1.show();
    }

    /**
     * 反射字段,其实这里还是在判断toast是否存在
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getField(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(object);
        }
        return null;
    }
}
