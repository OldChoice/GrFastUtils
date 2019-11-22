package gr.free.grfastuitils.myview;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import gr.free.grfastuitils.R;


/**
 * Create by guorui on 2019/11/22
 * Last update 2019/11/22
 * Description:左滑返回的基activity
 */
public class SlideBackActivity extends AppCompatActivity {

    String TAG = "SlideBackActivity";

    boolean isEage = false;//判断是否从左边缘划过来

    float shouldFinishPix = 0;
    public static float screenWidth = 0;
    float screenHeight = 0;

    int CANSLIDE_LENGTH = 16;

    View backView;
    SlideBackView slideBackView;

    //    View containerView;
    FrameLayout slideContainerView;

    float x;
    float y;
    float downX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;
        shouldFinishPix = screenWidth / 3;

        backView = LayoutInflater.from(this).inflate(R.layout.activitybase_slideback, null);
        slideBackView = backView.findViewById(R.id.slideBackView);

        FrameLayout container = (FrameLayout) getWindow().getDecorView();
        slideContainerView = new FrameLayout(this);
        slideContainerView.addView(backView);
        container.addView(slideContainerView);

        slideContainerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                x = motionEvent.getRawX();
                y = motionEvent.getRawY();
//                System.out.println(x + "------" + y);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = motionEvent.getRawX();
                        if (x <= dp2px(CANSLIDE_LENGTH)) {
                            isEage = true;
                            slideBackView.updateControlPoint(Math.abs(x));
                            setBackViewY(backView, (int) (motionEvent.getRawY()));
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float moveX = x - downX;
                        if (isEage) {
                            if (Math.abs(moveX) <= shouldFinishPix) {
                                slideBackView.updateControlPoint(Math.abs(moveX) / 2);
                            }
                            setBackViewY(backView, (int) (motionEvent.getRawY()));
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        //从左边缘划过来，并且最后在屏幕的三分之一外
                        if (isEage) {
                            if (x >= shouldFinishPix) {
                                slideBackSuccess();
                            }
                        }
                        isEage = false;
                        slideBackView.updateControlPoint(0);
                        break;
                }
                if (isEage) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void setBackViewY(View view, int y) {
        //判断是否超出了边界
        int topMargin = y - dp2px(SlideBackView.height) / 2;
//        if(topMargin < 0 || y > screenHeight-dp2px(SlideBackView.height)/2){
//            return;
//        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(view.getLayoutParams());
        layoutParams.topMargin = topMargin;
        view.setLayoutParams(layoutParams);
    }

    protected void slideBackSuccess() {
//        Log.d(TAG, "slideSuccess");
        super.finish();
    }

    private int dp2px(final float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
