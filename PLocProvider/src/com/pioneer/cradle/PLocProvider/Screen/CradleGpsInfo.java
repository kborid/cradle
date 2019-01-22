package com.pioneer.cradle.PLocProvider.Screen;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class CradleGpsInfo extends TabActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        final TabHost tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("Location Info")
                .setContent(new Intent(this, LocationInfo.class)));

        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("Satellite Info")
                .setContent(new Intent(this, SatelliteInfo.class)));

	}

}
