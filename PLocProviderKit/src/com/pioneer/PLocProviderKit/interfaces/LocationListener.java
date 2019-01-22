package com.pioneer.PLocProviderKit.interfaces;

import android.location.Location;

public interface LocationListener {
   public void onReceiveLocationInfo(String nmea);
    
   public void onReceiveLocationInfo(Location l);
}
