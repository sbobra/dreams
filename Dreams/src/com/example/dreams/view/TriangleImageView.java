package com.example.dreams.view;

import com.example.dreams.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class TriangleImageView extends ImageView {

	private Paint paint;
	private boolean selected = false;
	Context context;
	public int color = -1;
    Path path = new Path();
    Point a, b, c;

	public TriangleImageView(Context context) {
		super(context);
		this.context = context;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		a = new Point(0, (int)convertDpToPixel(50, context));
		b = new Point((int)convertDpToPixel(50, context), (int)convertDpToPixel(50, context));
		c = new Point((int)convertDpToPixel(50, context), 0);
		// TODO Auto-generated constructor stub
	}
	
	public TriangleImageView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    this.context=context;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		a = new Point(0, (int)convertDpToPixel(50, context));
		b = new Point((int)convertDpToPixel(50, context), (int)convertDpToPixel(50, context));
		c = new Point((int)convertDpToPixel(50, context), 0);
	}

	public TriangleImageView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    this.context=context;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		a = new Point(0, (int)convertDpToPixel(50, context));
		b = new Point((int)convertDpToPixel(50, context), (int)convertDpToPixel(50, context));
		c = new Point((int)convertDpToPixel(50, context), 0);
	}
	
	
	
	@Override
	public void onDraw(Canvas canvas){
	    path.setFillType(FillType.EVEN_ODD);
	    path.moveTo(a.x, a.y);
	    path.lineTo(b.x, b.y);
	    path.lineTo(c.x, c.y);
	    path.lineTo(a.x, a.y);
	    path.close();

	    canvas.drawPath(path, paint);
//		c.drawCircle(convertDpToPixel(30, getContext()), convertDpToPixel(30, getContext()), convertDpToPixel(30, getContext()), paint);
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
