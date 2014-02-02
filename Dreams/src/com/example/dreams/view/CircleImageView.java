package com.example.dreams.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.example.dreams.R;

public class CircleImageView extends ImageView {

	private Paint paint;
	private boolean selected = false;
	Context context;
	Bitmap image;
	public int color = -1;
	boolean drawCircle = false;


	public CircleImageView(Context context) {
		super(context);
		this.context = context;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		image = BitmapFactory.decodeResource(context.getResources(), R.drawable.check_off);
		// TODO Auto-generated constructor stub
	}
	
	public CircleImageView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    this.context=context;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		image = BitmapFactory.decodeResource(context.getResources(), R.drawable.check_off);
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    this.context=context;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		image = BitmapFactory.decodeResource(context.getResources(), R.drawable.check_off);
	}
	
	
	
	@Override
	public void onDraw(Canvas c){
		if (drawCircle) {
			c.drawCircle(convertDpToPixel(30, getContext()), convertDpToPixel(30, getContext()), convertDpToPixel(30, getContext()), paint);
			if (selected)
				c.drawBitmap(image, 0, 0, null);
		} else {
			super.onDraw(c);
		}
	}
	
	public void setColor(int c) {
		if (c == 0) {
			c = context.getResources().getColor(R.color.red);
		} else if (c == 1) {
			c = context.getResources().getColor(R.color.green);
		} else if (c==2) {
			c = context.getResources().getColor(R.color.blue);
		}
 		paint.setColor(c);
		color = c;
		drawCircle = true;
		invalidate();
	}
	
	public void toggleSelected() {
		selected = !selected;
		invalidate();
	}
	
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}

}
