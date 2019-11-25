package gr.free.grfastuitils.activitybase;

import android.os.Bundle;
import android.view.WindowManager;


import gr.free.grfastuitils.R;
import gr.free.grfastuitils.tools.StatusBarUtils;

/**
 * Create by guorui on 2019/11/22
 * Last update 2019/11/22
 * Description:简单使用baseActivity
 */
public abstract class BaseActivity extends GrBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.white, R.color.white);
        // StatusBarUtils.setNoTitleView(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
    }

}