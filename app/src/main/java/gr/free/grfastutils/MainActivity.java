package gr.free.grfastutils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import gr.free.grfastuitils.GrUtilsInstance;
import gr.free.grfastuitils.tools.MyToast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GrUtilsInstance.getInstance(this);
        MyToast.showShort("az");
    }
}
