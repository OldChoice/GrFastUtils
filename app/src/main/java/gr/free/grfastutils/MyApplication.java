package gr.free.grfastutils;

import android.app.Application;
import android.content.Context;

import gr.free.grfastuitils.GrUtilsInstance;

/**
 * Create by guorui on 2019/11/22
 * Last update 2019/11/22
 * Description:
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        GrUtilsInstance.getmContext(getInstance());

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static MyApplication getInstance() {
        return instance;
    }


}

