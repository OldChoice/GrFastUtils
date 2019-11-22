package gr.free.grfastuitils.tools;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Create by guorui on 2019/6/17
 * Last update 2019/6/17
 * Description:状态栏相关工具类
 */
public class StatusBarUtils {
    //    设置顶部和底部导航颜色为相同
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));//顶部
                window.setNavigationBarColor(activity.getResources().getColor(colorResId));//底部
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置顶部和底部不同颜色
    public static void setWindowStatusBarColor(Activity activity, int topColor, int bottomColor) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //下面这两段是小米高版本mui顶部字体颜色适配
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.setStatusBarColor(activity.getResources().getColor(topColor));
                window.setNavigationBarColor(activity.getResources().getColor(bottomColor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));
                window.setNavigationBarColor(dialog.getContext().getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //透明导航栏
    public static void setNoTitleView(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 透明状态栏
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // // 透明导航栏，就像华为的这个没有菜单等操作键，都是在最下面划出，他是导航栏的一种
//            activity.getWindow().addFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //透明导航栏，字体白色
    public static void setNoTitleViewAll(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //透明导航栏字体黑色
    public static void setNoTitleViewAllBlack(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //透明导航栏并全屏
    public static void setNoTitleViewFull(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 透明状态栏
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                // // 透明导航栏，就像华为的这个没有菜单等操作键，都是在最下面划出，他是导航栏的一种
//            activity.getWindow().addFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

