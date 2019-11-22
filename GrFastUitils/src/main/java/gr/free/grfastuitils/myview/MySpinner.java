package gr.free.grfastuitils.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;

public class MySpinner extends AppCompatSpinner {

	public MySpinner(Context context, AttributeSet attrs, int defStyle, int mode) {
		super(context, attrs, defStyle, mode);
		// TODO Auto-generated constructor stub
	}

	public MySpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MySpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MySpinner(Context context, int mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	public MySpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		
		 Paint Paint = new Paint();
		 Paint.setColor(Color.BLACK);
			Paint.setStrokeWidth(5);

			Path path = new Path();
			float del=getHeight()/7;
			path.moveTo(getWidth()-del, Math.abs(del*3- getHeight()));// 此点为多边形的起点
			path.lineTo(getWidth()-del,getHeight()-del);
			path.lineTo( getWidth()-del*3,getHeight()-del);
			path.close(); // 使这些点构成封闭的多边形
			canvas.drawPath(path, Paint);
		
		super.onDraw(canvas);
		
	}
}
