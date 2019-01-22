package com.pioneer.PLocProviderKit;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.location.Location;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;


class AlertWindow {
	TextView status;
	TextView lon;
	TextView lat;
	TextView speed;
	TextView time;
	
	View view;
	WindowManager windowManager;
	LayoutParams layoutParams;
	private boolean viewAdded = false;
	
	private AlertWindow(){};
	
	private static AlertWindow instance = new AlertWindow();
	
	public static AlertWindow getInstance() {
		return instance;
	}
	
	public void showAlert(Context c) {
		showAlert(c, 0, 0);
	}
	
	public void showAlert(Context c, int x, int y) {
		showAlert(c,x,y,120);
	}
	
	public void showAlert(Context c, int x, int y, int width) {
		if(viewAdded) {
			windowManager.updateViewLayout(view, layoutParams);
			return;
		}
		if(view == null || layoutParams==null) {
			
			createView(c.getApplicationContext());
			windowManager = (WindowManager) c.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
			layoutParams = new LayoutParams(dpTopx(c, width),
	                LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ERROR,
	                LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
			layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
			layoutParams.x = x;
			layoutParams.y = y;
			windowManager.addView(view, layoutParams);
			viewAdded = true;
			initView();
			
			view.setOnTouchListener(new OnTouchListener() {  
	            float[] temp = new float[] { 0f, 0f };  
	  
	            public boolean onTouch(View v, MotionEvent event) {  
	                layoutParams.gravity = Gravity.LEFT | Gravity.TOP;  
	                int eventaction = event.getAction();  
	                switch (eventaction) {  
	                case MotionEvent.ACTION_DOWN: 
	                    temp[0] = event.getX();  
	                    temp[1] = event.getY();  
	                    break;  
	  
	                case MotionEvent.ACTION_MOVE:  
	                    refreshView((int) (event.getRawX() - temp[0]), (int) (event  
	                            .getRawY() - temp[1]));  
	                    break;  
	  
	                }  
	                return true;  
	            }


	        }); 
		} else {
			windowManager.addView(view, layoutParams);
			viewAdded = true;
		}
	}
	private int statusBarHeight;
	
    public void refreshView(int x, int y) {  
        if(statusBarHeight == 0){  
            View rootView  = view.getRootView();  
            Rect r = new Rect();  
            rootView.getWindowVisibleDisplayFrame(r);  
            statusBarHeight = r.top;  
        }  
          
        layoutParams.x = x;  
        layoutParams.y = y - statusBarHeight;//STATUS_HEIGHT;  
        refresh();  
    }  
    
    private void refresh() {  
        if (viewAdded) {  
            windowManager.updateViewLayout(view, layoutParams);  
        } 
    } 

	
	private int dpTopx(Context c, int x) {
		return (int) (c.getResources().getDisplayMetrics().density*x);
	}
	
	public boolean isShowing(){
		return viewAdded;
	}
	
	private void initView() {
		int status = PLocProvider.getInstance().GetExtDeviceConnectionStatus();
		freshView(status);
		lon.setText("Lon: ");
		lat.setText("Lat: ");
		speed.setText("V:");
		time.setText("Time:");
	}
	
	private void createView(Context c) {
		LinearLayout l = new LinearLayout(c);
//		l.setBackgroundColor(0XAA000000);
		l.setOrientation(LinearLayout.VERTICAL);
		
		l.addView(status = new TextView(c), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		l.addView(lon = new TextView(c), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		l.addView(lat = new TextView(c), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		l.addView(speed = new TextView(c), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		l.addView(time = new TextView(c), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		status.setSingleLine();
		lon.setSingleLine();
		lat.setSingleLine();
		speed.setSingleLine();
		time.setSingleLine();
		
		status.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		lon.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		lat.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		speed.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		
		status.setShadowLayer(1, 2, 2, Color.BLUE);
		lon.setShadowLayer(1, 2, 2, Color.BLUE);
		lat.setShadowLayer(1, 2, 2, Color.BLUE);
		speed.setShadowLayer(1, 2, 2, Color.BLUE);
		time.setShadowLayer(1, 2, 2, Color.BLUE);
		
		view = l;
	}
	
	void freshView(int status, Location l) {
		if(!viewAdded) {
			return;
		}
		freshView(status);
		freshView(l);
	}
	
	void freshView(int status) {
		if(!viewAdded) {
			return;
		}
		if(status == PLocProviderKit.CONNECT_STATE_NOT_CONNECT_WITH_SERVICE) {
			this.status.setText("Service not connected");
			this.status.setTextColor(Color.RED);
		} else if(status == PLocProviderKit.CONNECT_STATE_NONE) {
			this.status.setText("Bluetooth none");
			this.status.setTextColor(Color.RED);
		} else if(status == PLocProviderKit.CONNECT_STATE_CONNECTING) {
			this.status.setText("Bluetooth conntecting");
			this.status.setTextColor(Color.YELLOW);
		} else if(status == PLocProviderKit.CONNECT_STATE_CONNECTED) {
			this.status.setText("Bluetooth conntected");
			this.status.setTextColor(Color.GREEN);
		}
	}
	
	void freshView(Location l) {
		if(!viewAdded) {
			return;
		}
		lon.setText("Lon: "+formartLon(l.getLongitude()));
		lat.setText("Lat: "+formartLon(l.getLatitude()));
		speed.setText("V:"+formartSpeed(l.getSpeed())+"m/s, B: "+(int)l.getBearing()+"");
		time.setText(getDisplayTime(l.getTime()));
	}
	
	String formartSpeed(float speed) {
		DecimalFormat df1=new DecimalFormat("###0.00"); 
		return df1.format(speed);
	}
	
	String formartLon(double lon) {
		DecimalFormat df1=new DecimalFormat("####.######"); 
		return df1.format(lon);
	}
	
	private String getDisplayTime(long time){
		SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return foo.format(new Date(time));
	}
	
	public void dismissAlert() {
		if (viewAdded) {
            windowManager.removeView(view);
            viewAdded = false;
        }
	}

}
