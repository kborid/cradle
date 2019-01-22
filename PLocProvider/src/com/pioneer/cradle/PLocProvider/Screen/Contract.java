package com.pioneer.cradle.PLocProvider.Screen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.pioneer.cradle.PLocProvider.ConntectedControl;
import com.pioneer.cradle.PLocProvider.R;
import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;

public class Contract extends Activity implements View.OnClickListener {
	
	private Button agree;
	private Button quit;
	
	private WebView content;
	
	private ViewGroup buttonLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.contract);
		findViews();
		
		init();
		
		setListeners();
		
		setTitle(R.string.contract);
		
	}

	private void setListeners() {
		agree.setOnClickListener(this);
		quit.setOnClickListener(this);
		
	}

	private void init() {
		boolean isFromLauncher = getIntent().getBooleanExtra("FromLauncher", false);
		if(!isFromLauncher) {
			buttonLayout.setVisibility(View.GONE);
		}
		
		
		content.getSettings().setSupportZoom(false);
		content.getSettings().setBuiltInZoomControls(false);
		content.getSettings().setDefaultTextEncodingName("utf-8");
		content.setBackgroundColor(Color.WHITE);

		content.loadUrl("file:///android_asset/declare.html"); 
	}

	private void findViews() {
		agree = (Button) findViewById(R.id.agree);
		quit = (Button) findViewById(R.id.quit);
		buttonLayout = (ViewGroup) findViewById(R.id.button_layout);
		content = (WebView) findViewById(R.id.webview);
	}

	@Override
	public void onClick(View v) {
		if(v == agree) {
			ConntectedControl.setSharedPreferenceToService(this, SharedPreferenceData.AGREE_CONTRACT, true);
			SharedPreferenceData.AGREE_CONTRACT.setValue(true);
			Intent i = new Intent(this, MainActivity.class);
			i.putExtra(SharedPreferenceData.AGREE_CONTRACT.name(), true);
			startActivity(i);
			finish();
		} else if(v == quit){
			SharedPreferenceData.AGREE_CONTRACT.setValue(false);
			finish();
			System.exit(0);
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if((keyCode == KeyEvent.KEYCODE_BACK) && (buttonLayout.getVisibility()==View.VISIBLE)) {
			finish();
			System.exit(0);
		}
		return super.onKeyDown(keyCode, event);
	}

}
