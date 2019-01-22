package com.pioneer.cradle.PLocProvider.Screen;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.location.Location;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pioneer.PLocProviderKit.PLocProviderKit;
import com.pioneer.PLocProviderKit.interfaces.LocationListener;
import com.pioneer.PLocProviderKit.interfaces.RequiredListener;
import com.pioneer.cradle.PLocProvider.R;

public class LocationInfo extends Activity {
	
	private CheckBox cradleStatus;
	private TextView latitude;
	private TextView longitude;
	private TextView altitude;
	private TextView speed;
	private TextView bearing;
	private TextView dimension;
	private TextView time;
	private TextView learnStatus;
	
	private PLocProviderKit kit = new PLocProviderKit();
	
	RequiredListener requiredListener;
	LocationListener locationListener;
	
	private static final String[] LearningStr = {"0 no-DR, no-GPS", "1 no-DR, GPS", "2 no-DR, difference-GPS","3","4", "5 DR, no-GPS", "6 DR, GPS"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.location_info);
		
		setContentView(R.layout.loaction_info);
		
		findViews();
		
		initViews();
		
		
		kit.registerGpsStatusListener(requiredListener = new RequiredListener() {
			
			@Override
			public void onExtDeviceConnectStateChanged(int connectState) {
				if(connectState == PLocProviderKit.CONNECT_STATE_NONE) {
					cradleStatus.setChecked(false);
					cradleStatus.setText("Connect none");
				} else if(connectState == PLocProviderKit.CONNECT_STATE_CONNECTED) {
					cradleStatus.setChecked(true);
					BluetoothDevice device = kit.getConnectedDevice();
					if(device != null) {
						cradleStatus.setText(device.getName()+"("+device.getAddress()+")");
					}
				} else if(connectState == PLocProviderKit.CONNECT_STATE_CONNECTING) {
					cradleStatus.setChecked(false);
					cradleStatus.setText(R.string.connecting);
				}
			}
		});
		
		kit.registerLocationListener(locationListener = new LocationListener() {
			
//			private long time = System.currentTimeMillis();
			@Override
			public void onReceiveLocationInfo(Location l) {
				setLocationInfo(l);
			}

			@Override
			public void onReceiveLocationInfo(String nmea) {
//				long sys = System.currentTimeMillis();
//				long diff = sys - time;
//				time = sys;
//				
//				System.out.println("1111111111111111 diff = "+diff+", nmea = "+nmea);
				
//				latitude.setText(nmea);
				String statements[] = nmea.split("\n");
				
				for(String statement : statements) {
					if(statement.startsWith("$GPGGA")) {
						decodeDRStatus(statement);
					}
				}
			}

			private void decodeDRStatus(String statement) {
				try {
					statement = statement.substring(0, statement.indexOf("*"));
					String[] fields = statement.split(",");

					int status = Integer.parseInt(fields[6]);
					
					String learnstatus = LearningStr[status];
					learnStatus.setText(learnstatus);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
	}
	
	private void initViews() {
		BluetoothDevice device = kit.getConnectedDevice();
		
		if(device != null) {
			cradleStatus.setChecked(true);
			cradleStatus.setText(device.getName()+"("+device.getAddress()+")");
		} else {
			cradleStatus.setChecked(false);
			cradleStatus.setText("Connect none");
			
		}
		
		
	}

	private void setLocationInfo(Location location) {
		latitude.setText(Double.toString(location.getLatitude())+"бу");
		longitude.setText(Double.toString(location.getLongitude())+"бу");
		altitude.setText(Double.toString(location.getAltitude())+"m");
		speed.setText(Float.toString(location.getSpeed())+"m/s");
		bearing.setText(Float.toString(location.getBearing())+"бу");
		dimension.setText(Float.toString(location.getAccuracy())+"m");
		time.setText(getDisplayTime(location.getTime()));
	}
	
	private String getDisplayTime(long time){
		SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return foo.format(new Date(time));
	}

	private void findViews() {
		cradleStatus = (CheckBox) findViewById(R.id.cradle_status_checkbox);
		latitude = (TextView) findViewById(R.id.lat);
		longitude = (TextView) findViewById(R.id.lon);
		altitude = (TextView) findViewById(R.id.alt);
		speed = (TextView) findViewById(R.id.speed);
		bearing = (TextView) findViewById(R.id.angel);
		dimension = (TextView) findViewById(R.id.dimension);
		time = (TextView) findViewById(R.id.time);
		
		learnStatus = (TextView) findViewById(R.id.learn_status);
	}

	@Override
	protected void onDestroy() {
		kit.unregisterGpsStatusListener(requiredListener);
		kit.unregisterLocationListener(locationListener);
		super.onDestroy();
	}

}
