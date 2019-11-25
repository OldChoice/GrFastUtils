package gr.free.grfastutils;

import android.os.Bundle;

import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastuitils.tools.MyToast;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyToast.showShort("az");



    }

    @Override
    public void initValues() {
        super.initValues();
    }
}
