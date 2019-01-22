package com.pioneer.cradle.PLocProvider.Screen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.pioneer.cradle.PLocProvider.R;

public class MainUILayer extends Activity implements View.OnClickListener{
	
	private ImageView navi;
	private ImageView music;
	private ImageView setting;
	private ImageView phone;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_ui_layer);
		findViews();
		
	}
	
	private void findViews() {
		navi = (ImageView) findViewById(R.id.navi);
		music = (ImageView) findViewById(R.id.music);
		setting = (ImageView) findViewById(R.id.setting);
		phone = (ImageView) findViewById(R.id.phone);
		
		navi.setOnClickListener(this);
		music.setOnClickListener(this);
		setting.setOnClickListener(this);
		phone.setOnClickListener(this);
		
	}
	
	
	@Override
	public final void onClick(View v) {
		try {
			if(v == navi) {
				clickNavi();
			} else if(v == music) {
				clickMusic();
			} else if(v == phone) {
				clickPhone();
			} else if(v == setting) {
				clickSetting();
			} else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void clickSetting() {
	}

	protected void clickPhone() {
	}

	protected void clickMusic() {
	}

	protected void clickNavi() {
	}



}
