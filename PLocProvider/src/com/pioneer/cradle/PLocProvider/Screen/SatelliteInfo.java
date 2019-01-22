package com.pioneer.cradle.PLocProvider.Screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pioneer.PLocProviderKit.PLocProviderKit;
import com.pioneer.PLocProviderKit.interfaces.SatelliteListener;
import com.pioneer.PLocProviderKit.util.SatelliteData;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class SatelliteInfo extends ListActivity {
	
	private static final int MAX_LENGTH = 16;
	
	private SimpleAdapter adapter;
	private PLocProviderKit kit = new PLocProviderKit();
	private ArrayList<HashMap<String, String>> list;
	
	private SatelliteListener listenr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		adapter = new SimpleAdapter(this, getData(), android.R.layout.simple_list_item_1, new String[]{"text"}, new int[]{android.R.id.text1});
		
		setListAdapter(adapter);
		
		kit.registerSatelliteListener(listenr = new SatelliteListener() {
			
			@Override
			public void onRecive(SatelliteData[] s) {
				// TODO Auto-generated method stub
				notifyChanged(s);
			}
		});
	}
	
	private List<? extends Map<String, ?>> getData(){
		list = new ArrayList<HashMap<String, String>>();
		for(int i=0; i<MAX_LENGTH; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			list.add(map);
		}
		return list;
	}
	
	private void notifyChanged(SatelliteData[] s){
		if(s != null && s.length > MAX_LENGTH) {
			return ;
		}
		
		int length = 0;
		if(s != null && s.length != 0) {
			length = s.length;
		}
		
		for(int i=0; i<length; i++) {
			HashMap<String, String> map = list.get(i);
			map.put("text", s[i].toString());
		}
		
		for(int i=length; i<MAX_LENGTH; i++) {
			HashMap<String, String> map = list.get(i);
			map.put("text", "");
		}
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onDestroy() {
		kit.unregisterSatelliteListener(listenr);
		super.onDestroy();
	}
}
