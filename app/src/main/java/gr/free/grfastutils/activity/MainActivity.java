package gr.free.grfastutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastutils.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button btn1, btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MyToast.showShort("az");
//        System.out.println(PackageUtils.getVersionCode(this) + "---" + PackageUtils.getAppName(this) + "----" + PackageUtils.getVersionName(this));
        initValues();
        findView();
        setView();
        setListener();

    }

    @Override
    public void initValues() {

    }

    @Override
    public void findView() {
        btn1 = findViewById(R.id.main_btn1);
        btn2 = findViewById(R.id.main_btn2);

    }

    @Override
    public void setView() {

    }

    @Override
    public void setListener() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn1:
                //网络访问获取列表
                toStartActivity(ChoosePersonMultiActivity.class);
                break;
            case R.id.main_btn2:
                toStartActivity(DataBaseActivity.class);
                break;
        }
    }
}
