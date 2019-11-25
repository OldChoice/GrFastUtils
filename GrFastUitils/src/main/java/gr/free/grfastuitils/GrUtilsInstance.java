package gr.free.grfastuitils;

import android.content.Context;

/**
 * 框架统一管理上下文
 */
public class GrUtilsInstance {
    /**
     * 上下文对象
     */
    private static Context mContext;

    /**
     * 获得单例实例，在Application初始化的时候使用
     *
     * @param context
     */
    public static void getmContext(Context context) {
        mContext = context;
    }

    public static Context getInstance() {
        return mContext;
    }

}
