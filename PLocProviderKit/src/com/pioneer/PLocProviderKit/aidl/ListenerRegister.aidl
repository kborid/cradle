package com.pioneer.PLocProviderKit.aidl;


import com.pioneer.PLocProviderKit.aidl.pOnCallBackListener;

interface ListenerRegister {
    /**
     * Called when the service has a new value for you.
     * @value current state of BT in SPP Service
     */
    
    boolean pRegisterListener(in String packageName, pOnCallBackListener ll);
    
    boolean pUnregisterListener(in String packageName, pOnCallBackListener ll);	
    
    int pGetExtDeviceConnectionStatus();
    
    Location pGetLatestLocation();
    
    BluetoothDevice getConnectedDevice();
    
    Bundle order(in Bundle order);
}