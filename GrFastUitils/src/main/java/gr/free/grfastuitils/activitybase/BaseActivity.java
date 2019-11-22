package gr.free.grfastuitils.activitybase;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import gr.free.grfastuitils.R;
import gr.free.grfastuitils.tools.StatusBarUtils;

public abstract class BaseActivity extends AppCompatActivity implements ActivityBaseSet {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.white, R.color.white);
//        StatusBarUtils.setNoTitleView(this);
        AppManager.getAppManager().addActivity(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initValues() {
        // 初始化数据

    }

    @Override
    public void findView() {
        // 初始化控件

    }

    @Override
    public void setView() {
        // 设置控件显示状态

    }

    @Override
    public void setListener() {
        // 设置触发点击

    }

    /**
     * 从当前界面到指定的activity界面，不传数据
     */
    public void toStartActivity(Class<?> cls) {

        startActivity(new Intent(this, cls));

    }


    @Override
    protected void onDestroy() {
        // 由于调用系统拍照的时候会有些界面的周期被系统相机影响,所以其它基本主界面在BaseAboutPicActivity中,不过单单去掉了这一个而已
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

}