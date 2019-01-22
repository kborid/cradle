package com.pioneer.cradle.PLocProvider.Screen;

import com.pioneer.PLocProviderKit.RemoteServiceControl;
import com.pioneer.cradle.PLocProvider.R;
import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class CradleSetting extends PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener{
	
	private CheckBoxPreference gpsLogEnable;
	private CheckBoxPreference snsLog;
	private CheckBoxPreference nmeaLog;
	
	private Preference cleanLearn;
	private Preference dymset;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.debug_setting);
		addPreferencesFromResource(R.xml.cradle_setting);
		gpsLogEnable = (CheckBoxPreference) findPreference("gps_log_enable");
		snsLog = (CheckBoxPreference) findPreference("sns_log");
		nmeaLog = (CheckBoxPreference) findPreference("nmea_log");
//		cleanLearn = findPreference("clear_learn");
		dymset = findPreference("set_Dymset");
		
		gpsLogEnable.setChecked(SharedPreferenceData.GPS_LOG_RECORD.getBoolean());
		
		gpsLogEnable.setOnPreferenceChangeListener(this);
		snsLog.setOnPreferenceChangeListener(this);
		nmeaLog.setOnPreferenceChangeListener(this);
		
//		cleanLearn.setOnPreferenceClickListener(this);
		dymset.setOnPreferenceClickListener(this);
	}


	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(preference == gpsLogEnable) {
			RemoteServiceControl.getInstance().setGpsLogFileEnable((Boolean)newValue);
		} else if(preference == snsLog){
			RemoteServiceControl.getInstance().setSNSGpsLogFileEnable((Boolean)newValue);
		} else if(preference == nmeaLog) {
			RemoteServiceControl.getInstance().setNMEALogFileEnable((Boolean)newValue);
		}
		return true;
	}


	@Override
	public boolean onPreferenceClick(Preference preference) {
		if(cleanLearn == preference) {
			RemoteServiceControl.getInstance().clearLearn();
			Toast.makeText(this, "clean leanrn commad sended", Toast.LENGTH_LONG).show();
		} else if(dymset == preference) {
			RemoteServiceControl.getInstance().setDmyset();
			Toast.makeText(this, "dymset commad sended", Toast.LENGTH_LONG).show();
		}
		return true;
	}
	

}
