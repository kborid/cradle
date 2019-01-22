package com.pioneer.cradle.PLocProvider.Screen;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public abstract class LongClickPreferenceActivity extends PreferenceActivity implements OnItemLongClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getListView().setOnItemLongClickListener(this);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		Preference preference = (Preference) parent.getAdapter().getItem(position);
		return onPreferenceTreeLongClick(getPreferenceScreen(), preference);
	}
	
	/**
	 * the item want support long click, then do something and return true; 
	 * otherwise return false.
	 * 
	 * if you return false, that will execute both long click 
	 * and click event when you long click. and otherwise you return true,
	 * that will execute long click only when you long click.
	 * 
	 * */
	public abstract boolean onPreferenceTreeLongClick(PreferenceScreen preferenceScreen, Preference preference);

}
