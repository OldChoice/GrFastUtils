package gr.free.grfastutils;

import android.os.Bundle;

import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastuitils.tools.MyToast;
import gr.free.grfastuitils.tools.PackageUtils;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyToast.showShort("az");
        System.out.println(PackageUtils.getVersionCode(this)+"---"+PackageUtils.getAppName(this)+"----"+PackageUtils.getVersionName(this));


    }

    @Override
    public void initValues() {
        super.initValues();
    }
}
