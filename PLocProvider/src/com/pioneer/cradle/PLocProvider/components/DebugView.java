package com.pioneer.cradle.PLocProvider.components;


import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class DebugView extends ImageView {
	
	private int count = 0;
	private DebugOnListener listener;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1) {
				count = 0;
			}
		};
	};

	public DebugView(Context context) {
		super(context);
		
//		setBackgroundColor(Color.RED);
		
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				count++;
				if(count == 4) {
					count = 0;
					if(listener != null) {
						listener.onDebugOn();
					}
				} else {
					sendMessageForCancel();
				}
			}
		});
	}
	
	public void addToViewLayout(RelativeLayout l) {
		l.addView(this);
		RelativeLayout.LayoutParams lp = (LayoutParams) getLayoutParams();
		lp.height = DensityUtil.dp2px(getContext(), 60);
		lp.width = DensityUtil.dp2px(getContext(), 60);;
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	}
	
	public static DebugView addDebugView(RelativeLayout l) {
		DebugView view = new DebugView(l.getContext());
		view.addToViewLayout(l);
		
		return view;
	}
	public static DebugView addViewToRightBottom(RelativeLayout l) {
		DebugView view = new DebugView(l.getContext());
		view.addToViewBottomLayout(l);
		
		return view;
	}
	private void addToViewBottomLayout(RelativeLayout l) {
		// TODO Auto-generated method stub
		l.addView(this);
		RelativeLayout.LayoutParams lp = (LayoutParams) getLayoutParams();
		lp.height = DensityUtil.dp2px(getContext(), 60);
		lp.width = DensityUtil.dp2px(getContext(), 60);;
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	}

	public void setDebugOnListener(DebugOnListener l) {
		this.listener = l;
	}
	
	private void sendMessageForCancel() {
		handler.removeMessages(1);
		handler.sendEmptyMessageDelayed(1, 500);
	}
	
	public interface DebugOnListener {
		void onDebugOn();
	}
}
