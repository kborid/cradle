package com.pioneer.cradle.PLocProvider.components;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class CircleView extends ImageView {
	
	private static final float radius_width_ratio = 0.4f;
	
	public CircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initView();
	}

	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public CircleView(Context context) {
		super(context);
		initView();
	}
	
	private void initView() {
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if(!onTouch(event)) {
			return false;
		}
		return super.dispatchTouchEvent(event);
	}
	
	
	public boolean onTouch(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			Point center = new Point(getWidth()/2, getHeight()/2);
			float distance = (event.getX() -center.x)*(event.getX() -center.x) + (event.getY() -center.y)*(event.getY() -center.y);
			
			if(distance < getWidth()*radius_width_ratio * getWidth()*radius_width_ratio){
				return true;
			}
			
			return false;
		}
		return true;
	}
	
	
}
