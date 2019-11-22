package gr.free.grfastuitils.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Create by guorui on 2018/12/13
 * Last update 2018/12/13
 * Description:页面侧拉返回
 */
public class SlideBackView extends View {
    String TAG = "SlideBackView";

    Path path, arrowPath;
    Paint paint, arrowPaint;

    float controlX = 0;//曲线的控制点

    //    String backViewColor = "#B3000000";

    float backViewHeight = 0;
    public static float width = 40;
    public static float height = 260;

    public SlideBackView(Context context) {
        this(context, null);
    }

    public SlideBackView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backViewHeight = dp2px(260);

        path = new Path();
        arrowPath = new Path();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
//        paint.setColor(Color.parseColor("#303F9F"));
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);

        arrowPaint = new Paint();
        arrowPaint.setAntiAlias(true);
        arrowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        arrowPaint.setColor(Color.WHITE);
        arrowPaint.setStrokeWidth(4);

        setAlpha(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //拉出来的背景部分
        path.reset();
        path.moveTo(0, 0);
        path.quadTo(0, backViewHeight / 4, controlX / 3, backViewHeight * 3 / 8);
        path.quadTo(controlX * 5 / 8, backViewHeight / 2, controlX / 3, backViewHeight * 5 / 8);
        path.quadTo(0, backViewHeight * 6 / 8, 0, backViewHeight);
        canvas.drawPath(path, paint);

//        //画里面的箭头
//        arrowPath.reset();
//        arrowPath.moveTo(controlX/10+(dp2px(10)*(controlX/(SlideBackActivity.screenWidth/6))),backViewHeight*15/32);
//        arrowPath.lineTo(controlX/10,backViewHeight*16.1f/32);
//        arrowPath.moveTo(controlX/10,backViewHeight*15.9f/32);
//        arrowPath.lineTo(controlX/10+(dp2px(10)*(controlX/(SlideBackActivity.screenWidth/6))),backViewHeight*17/32);
//
//        arrowPath.moveTo(controlX / 10 + (dp2px(10) * (controlX / (SlideBackActivity.screenWidth / 6))),
//                backViewHeight * 15.5f / 32 - controlX / 6 + (dp2px(10) * (controlX / (SlideBackActivity.screenWidth / 3))));
//        arrowPath.lineTo(controlX / 10 + (dp2px(10) * (controlX / (SlideBackActivity.screenWidth / 6))),
//                backViewHeight * 15.5f / 32 + controlX / 6 + (dp2px(10) * (controlX / (SlideBackActivity.screenWidth / 3))));


        //画里面的箭头
        arrowPath.reset();
        arrowPath.moveTo(controlX / 6, backViewHeight * 16.1f / 32);
        arrowPath.lineTo(controlX / 10 + (dp2px(10) * (controlX / (SlideBackActivity.screenWidth / 6))),
                backViewHeight * 16.1f / 32 - controlX / 8);

        arrowPath.moveTo(controlX / 6, backViewHeight * 16.0f / 32);
        arrowPath.lineTo(controlX / 10 + (dp2px(10) * (controlX / (SlideBackActivity.screenWidth / 6))),
                backViewHeight * 16.0f / 32 + controlX / 8);


        canvas.drawPath(arrowPath, arrowPaint);

        setAlpha(controlX / (SlideBackActivity.screenWidth / 6));
    }

    public void updateControlPoint(float controlX) {
//        Log.d(TAG, "updateControlPoint: " + controlX);
        this.controlX = controlX;
        invalidate();
    }

    public float dp2px(final float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }
}
