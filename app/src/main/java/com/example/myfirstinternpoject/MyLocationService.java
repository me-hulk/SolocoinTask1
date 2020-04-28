package com.example.myfirstinternpoject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myfirstinternpoject.SetLocation.latitudeKey;
import static com.example.myfirstinternpoject.SetLocation.longitudeKey;


public class MyLocationService extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.example.myfirstinternpoject.UPDATE_LOCATION";
    public MyLocationService() {
    }
    public int reward;
    public double distance;
    public double latitude_c;
    public double longitude_c;
    public double latitude_a;
    public double longitude_a;
    public String r;
    SharedPreferences prefs;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null)
        {
            final String action = intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action))
            {
                LocationResult result = LocationResult.extractResult(intent);
                if(result!=null)
                {
                    SharedPreferences prefs = getSharedPreferences("SaveData", MODE_PRIVATE);
                    Location location = result.getLastLocation();
                    final int R = 6371; // Radius of the earth
                    latitude_c =location.getLatitude();
                    longitude_c = location.getLongitude();
                    assert prefs != null;
                    if(prefs.contains(latitudeKey))
                        latitude_a = Double.longBitsToDouble(prefs.getLong(latitudeKey, 0));
                    if(prefs.contains(SetLocation.longitudeKey))
                        longitude_a = Double.longBitsToDouble(prefs.getLong(longitudeKey, 0));
                    double latDistance = Math.toRadians(latitude_a - latitude_c);
                    double lonDistance = Math.toRadians(longitude_a - longitude_c);
                    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                            + Math.cos(Math.toRadians(latitude_c)) * Math.cos(Math.toRadians(latitude_a))
                            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                    distance=R*c;
                    if(distance < 0.1)
                        reward = reward+10;
                    else
                        reward = reward+10;

                    r = Integer.toString(reward);
                    try{
                        MainActivity.getInstance().updateTextView(r);
                    }catch (Exception ex)
                    {
                        Toast.makeText(context,r,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }

    private SharedPreferences getSharedPreferences(String saveData, int modePrivate) {
        if(prefs.contains(latitudeKey))
            latitude_a = Double.longBitsToDouble(prefs.getLong(latitudeKey, 0));
        if(prefs.contains(SetLocation.longitudeKey))
            longitude_a = Double.longBitsToDouble(prefs.getLong(longitudeKey, 0));
        return null;
    }
}
