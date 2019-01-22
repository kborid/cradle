package com.pioneer.cradle.PLocProvider.components;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;

public class ViewHelp extends View{

	public static final int REQUEST_CODE_VOICE_RECOGNIZER = 123456; 

	private ViewHelp(Context context) {
		super(context);
	}
	
	public static void startVoiceRecognizer(Activity a) {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				   RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		a.startActivityForResult(intent, REQUEST_CODE_VOICE_RECOGNIZER);
	}
	
    public static void setTouchDelegate(final View delegateeView, 
    		final int leftOffset, 
    		final int topOffset, 
    		final int rightOffset, 
    		final int bottomOffset) {
    	final View parent = (View) delegateeView.getParent(); 
    	parent.post(new Runnable() { 
    	       public void run() { 
    	        Rect r = new Rect(); 
    	        delegateeView.getHitRect(r); 
    	        r.top += topOffset; 
    	        r.left += leftOffset; 
    	        r.bottom += bottomOffset; 
    	        r.right += rightOffset;
    	        parent.setTouchDelegate(new TouchDelegate( r , delegateeView)); 
    	    } 
    	}); 
    }

//    public static void setTouchDelegate(final View delegateeView, 
//    		final View parent,
//    		final int leftOffset, 
//    		final int topOffset, 
//    		final int rightOffset, 
//    		final int bottomOffset) {
//    	parent.post(new Runnable() { 
//    	       public void run() { 
//    	        Rect r = new Rect(); 
//    	        delegateeView.getHitRect(r); 
//    	        r.top += topOffset; 
//    	        r.left += leftOffset; 
//    	        r.bottom += bottomOffset; 
//    	        r.right += rightOffset;
//    	        parent.setTouchDelegate(new TouchDelegate( r , delegateeView)); 
//    	    } 
//    	}); 
//    }

	public static StateListDrawable createDrawableListByImageID(Context context, int normalid, int pressid){
		return createDrawableListByImageID(context, normalid, -1, -1, -1, pressid);
	}

	public static StateListDrawable createDrawableListByImageID(Context context, int normalid, int disableid,int selectid,int focusid,int pressid){
		StateListDrawable iamgeDrawable = new StateListDrawable();	
		Resources resources = context.getResources();          
		Drawable selected = null;
		Drawable pressed = null;
		Drawable normal = null;
		Drawable focused = null;
		Drawable disable = null;
	
		if (selectid > 0) {
			selected = resources.getDrawable(selectid);
		}
		if (focusid > 0) {
			focused = resources.getDrawable(focusid);
		}
		if (pressid > 0) {
			pressed = resources.getDrawable(pressid);
		}
		if (normalid > 0) {
			normal = resources.getDrawable(normalid);
		}
		if (disableid > 0) {
			disable = resources.getDrawable(disableid);
		}
		if(pressed != null){
			iamgeDrawable.addState(View.PRESSED_ENABLED_STATE_SET, pressed);	
		}		
		if(focused != null){
			iamgeDrawable.addState(View.ENABLED_FOCUSED_STATE_SET, focused);
		}
		if(selected != null){
			iamgeDrawable.addState(View.SELECTED_STATE_SET, selected);	
		}
		if(focused != null){
			iamgeDrawable.addState(View.FOCUSED_STATE_SET, focused);	
		}		
		if(normal != null){
			iamgeDrawable.addState(View.ENABLED_STATE_SET, normal);
		}
		if(normal != null){
			iamgeDrawable.addState(View.EMPTY_STATE_SET, disable);	
		}
		return  iamgeDrawable;				
	}
	
	public static void setTextDeleteButton(View v, EditText et) {
		v.setOnClickListener(new DeleteEditViewListenr(et));
	}
	
	public static void scrollToBottom(final ViewGroup scroll) {

		Handler mHandler = new Handler();

		mHandler.post(new Runnable() {
			public void run() {
				if (scroll == null || scroll.getChildAt(0) == null) {
					return;
				}

				int offset = scroll.getChildAt(0).getMeasuredHeight() - scroll.getHeight();
				if (offset < 0) {
					offset = 0;
				}

				scroll.scrollTo(0, offset);
			}
		});
	}
	
	public static void showInputSoftKeyBoard(EditText input) {
	    //show soft key board
		input.setFocusable(true);
		input.requestFocus();
	    ((InputMethodManager)input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(input, 0);
	}
//	
//	public static void closeInputKeyBoard(EditText input) {
//        InputMethodManager imm = (InputMethodManager)input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
//        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
//	}
	
	/**
	 * please ensure don't have null object
	 * */
	public static void closeInputKeyBoard(EditText... inputs) {
        InputMethodManager imm = (InputMethodManager)inputs[0].getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
        for(EditText input : inputs) {
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }
	}
	
}

class DeleteEditViewListenr implements OnClickListener{
	private EditText et;
	
	DeleteEditViewListenr(EditText et) {
		this.et = et;
	}
	@Override
	public void onClick(View v) {
		et.setText("");
	}
	
}
