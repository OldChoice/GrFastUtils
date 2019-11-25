package gr.free.grfastutils.activity;

import android.os.Bundle;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastuitils.tools.LoadUtils;
import gr.free.grfastuitils.tools.MyToast;
import gr.free.grfastuitils.tools.PackageUtils;
import gr.free.grfastutils.R;

public class MainActivity extends BaseActivity {

    private SweetAlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyToast.showShort("az");
        System.out.println(PackageUtils.getVersionCode(this) + "---" + PackageUtils.getAppName(this) + "----" + PackageUtils.getVersionName(this));

        loadingDialog = new LoadUtils().showLoadingDialog(this, loadingDialog);
        toStartActivity(ChoosePersonMultiActivity.class);

    }

    @Override
    public void initValues() {
        super.initValues();
    }
}
