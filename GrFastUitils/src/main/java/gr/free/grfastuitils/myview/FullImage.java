package gr.free.grfastuitils.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 自动让图片铺满控件显示的ImageView<br/>
 * 何谓铺满控件：就是图片等比缩放。然后占满控件。多余的部分会加载在控件之外，也就是看不见的地方
 */
public class FullImage extends AppCompatImageView implements OnGlobalLayoutListener {

	private boolean once;
	private Matrix matrix;

	public FullImage(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FullImage(Context context) {
		this(context, null);
	}

	public FullImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// init
		init();
	}

	private void init() {
		once = true;
		matrix = new Matrix();
		// setScaleType(ScaleType.MATRIX);// 这一步不加，以上全是白费
	}

	@Override
	protected void onAttachedToWindow() {
		// 注册 OnGlobalLayoutListener 的监听
		ViewTreeObserver observer = getViewTreeObserver();
		observer.addOnGlobalLayoutListener(this);
		super.onAttachedToWindow();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onDetachedFromWindow() {
		// 反注册 OnGlobalLayoutListener 的监听
		getViewTreeObserver().removeOnGlobalLayoutListener(this);
		super.onDetachedFromWindow();
	}

	@Override
	public void onGlobalLayout() {
		if (once) {// 初始化操作，只需要搞一次即可。但是onGlobalLayout 会被多次调用

			// 1. 获取当前控件，加载之后的(this)的宽高
			int width = this.getWidth();
			int height = this.getHeight();

			// 2. 获取显示的图片，以及宽高
			Drawable drawable = this.getDrawable();// 获取显示的图片
			if (drawable == null)
				return;
			int dw = drawable.getIntrinsicWidth();// 获取图片的宽高
			int dh = drawable.getIntrinsicHeight();// 获取图片的宽高
			// 5.不仅要缩放，还要让图片居中(图片中心点和控件中心点重合)，也就是平移
			int dx = Math.round(width * 1.0f / 2 - dw * 1.0f / 2);// x方向：向右移动：控件宽度的一半-图片宽度的一般
			int dy = Math.round(height * 1.0f / 2 - dh * 1.0f / 2);// y方法:向下移动:控件高度的一半-图片高度的一半
			// ___TODO:一定要先平移再进行缩放拆走
			matrix.postTranslate(dx, dy);// 这个OK

			// 3. 根据控件宽高一级图片宽高，设置缩放比例
			float scale = 1.0f;
			// case 1:图片宽度>控件宽度，高度<控件高度
			if (dw > width && dh < height) {// 如果要铺满控件，且等比缩放，则
				/** // scale = width * 1.0f / dw;// <1,缩小的 */
				scale = height * 1.0f / dh;// >1,放大
				// System.out.println("case 1:" + scale);
			}
			// case 2:图片宽度<控件宽度，高度>控件高度
			if (dw < width && dh > height) {// 如果要完全显示图片，且等比缩放，则
				/** // scale = height / dh;// <1,缩小的 */
				scale = width * 1.0f / dw;// >1,放大
				// System.out.println("case 2:" + scale);
			}

			// case 3:图片宽度>控件宽度，高度>控件高度
			if (dw > width && dh > height) {// 如果要铺满控件，且等比缩放，则
				scale = Math.max(width * 1.0f / dw, height * 1.0f / dh);// <1,缩小的
				// System.out.println("case 3:" + scale);
			}
			// case 4:图片宽度<控件宽度，高度<控件高度
			if (dw < width && dh < height) {// 如果要完全显示图片，且等比缩放，则
				scale = Math.max(width * 1.0f / dw, height * 1.0f / dh);// >1,放大的
				// System.out.println("case 4:" + scale);
			}
			// ------图片与控件的尺寸比较，只有以上4种情况
			// 4. 根据缩放比例进行缩放
			matrix.postScale(scale, scale, width / 2, height / 2);

			this.setImageMatrix(matrix);
			once = false;
		}
	}
}