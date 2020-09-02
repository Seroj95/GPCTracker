package com.gptracera.gpctracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocListener implements LocationListener {
    private LocListenerinterface locListenerinterface;
    @Override
    public void onLocationChanged(Location location) {
locListenerinterface.OnLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void setLocListenerinterface(LocListenerinterface locListenerinterface) {
        this.locListenerinterface = locListenerinterface;
    }
}
