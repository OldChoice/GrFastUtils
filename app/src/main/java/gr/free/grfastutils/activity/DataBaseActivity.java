package gr.free.grfastutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastutils.R;
import gr.free.grfastutils.tools.Sharpreferens1;

/**
 * Create by guorui on 2020/1/8
 * Last update 2020/1/8
 * Description:处理数据，数据库等操作
 */
public class DataBaseActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private Button btnSubmit;
    private Sharpreferens1 sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        initValues();
        findView();
        setView();
        setListener();
        System.out.println(Sharpreferens1.getUserName());
        String sf=sp.getUserName();
    }

    @Override
    public void initValues() {
        Sharpreferens1.setUserName("sss");

    }

    @Override
    public void findView() {
        ivBack = findViewById(R.id.activity_topback_submit_back);
        tvTitle = findViewById(R.id.activity_topback_submit_titile);
        btnSubmit = findViewById(R.id.activity_topback_submit_primit);

    }

    @Override
    public void setView() {
        tvTitle.setText("数据操作");
        btnSubmit.setVisibility(View.GONE);

    }

    @Override
    public void setListener() {
        ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_topback_submit_back:
                finish();
                break;
        }
    }


}
