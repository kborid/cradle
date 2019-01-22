package com.pioneer.cradle.PLocProvider.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class RectView extends View {

	public RectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RectView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RectView(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(measuredWidth, measuredWidth);
	}


}
