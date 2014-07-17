package com.example.cycleview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


public class SelectCircleImageView extends ImageView implements View.OnClickListener{
	private Paint paint = new Paint();
	Bitmap mDrawBitmap = null;
	private boolean isSelectable = false;
	private boolean isSelected = false;
	
	/**
	 * 边框 长度 比例 默认为0.05
	 */
	private float RIM=0.05f;
	RGBColor defaultRGBColor = new RGBColor(255, 0, 0, 0);
	RGBColor unSelectedColor;
	RGBColor seletedColor;
	public SelectCircleImageView(Context context) {
		super(context);
		init();
	}

	public SelectCircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SelectCircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public void init(){
		this.setOnClickListener(this);
		setSelectable(true);
		setSelectColor(new RGBColor(255,255,255,255),new RGBColor(255,0,255,127));
	}
	public void setSelectable(boolean isSelectable){
		this.isSelectable = isSelectable;
	}
	public boolean getSelectable(){
		return isSelectable;
	}
	public void setSelected(boolean isSelected){
		this.isSelected = isSelected;
		this.invalidate();
	}
	public boolean getSelected(){
		return isSelected;
	}
	
	public void setRIM(float rim)
	{
		RIM = rim;
	}
	public float getRIM(){
		return RIM;
	}
	
	public void setSelectColor(RGBColor unSelectedRgb,RGBColor selectedRgb){
		unSelectedColor = unSelectedRgb;
		seletedColor = selectedRgb;
	}
	public RGBColor getSelectedColor(){
		return seletedColor;
	}
	public RGBColor getunSelectedColor(){
		return unSelectedColor;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (null != drawable&& drawable instanceof BitmapDrawable) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			if(bitmap == null){
				super.onDraw(canvas);
				return;
			}
			if(mDrawBitmap!=null&&mDrawBitmap!=bitmap){
				mDrawBitmap.recycle();
			}
			mDrawBitmap = toRoundCorner(bitmap);
			final Rect rect = new Rect(0, 0, mDrawBitmap.getWidth(), mDrawBitmap.getHeight());
			final Rect rect2 = new Rect(0, 0, getWidth(), getHeight());
			paint.reset();
			canvas.drawBitmap(mDrawBitmap, rect, rect2, paint);
		} else {
			super.onDraw(canvas);
		}
	}

	private Bitmap toRoundCorner(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		int x = bitmap.getWidth();
		float rim=0;
		if(isSelectable){
			rim = RIM*x;
		}
		canvas.drawCircle(x / 2, x / 2, x / 2-rim, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		if(isSelectable){
			Paint tmpPaint = new Paint();
			tmpPaint.setAntiAlias(true); //消除锯齿 
			tmpPaint.setStyle(Style.STROKE);  //绘制空心圆或 空心矩形 
			if(isSelected){
				if(seletedColor!=null){
					tmpPaint.setARGB(seletedColor.a, seletedColor.r ,seletedColor.g, seletedColor.b);
				}else{
					tmpPaint.setARGB(defaultRGBColor.a, defaultRGBColor.r ,defaultRGBColor.g, defaultRGBColor.b);
				}
			}else{
				if(unSelectedColor!=null){
					tmpPaint.setARGB(unSelectedColor.a, unSelectedColor.r ,unSelectedColor.g, unSelectedColor.b);
				}else{
					tmpPaint.setARGB(defaultRGBColor.a, defaultRGBColor.r ,defaultRGBColor.g, defaultRGBColor.b);
				}
			}
			tmpPaint.setStrokeWidth(rim);
			canvas.drawCircle(x / 2, x / 2, x / 2-rim/2, tmpPaint);
		}
		return output;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
			boolean selected = !this.getSelected();
			this.setSelected(selected);
	}

}