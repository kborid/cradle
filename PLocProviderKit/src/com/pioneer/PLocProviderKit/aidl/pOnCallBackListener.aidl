package com.pioneer.PLocProviderKit.aidl;


oneway interface pOnCallBackListener {

	void onReceiveRemoteCtrl(int info);
	
    void onReceiveLocationInfo(in String nmea);
    
    void onLocationChanged(in Location l);
    
	void onExtDeviceConnectStateChanged(int connectState);
	
	void onSatelliteChanged(String satellites);
}