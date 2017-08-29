package com.example.shang.dailynews_master.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static com.example.shang.dailynews_master.config.ContentValues.CITY;

/**
 * Created by shang on 2017/7/26.
 */

public class LocationHelper {

    private static Location location = null;
    public static void getLocation(final Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        // 标准
        Criteria criteria = new Criteria();
        criteria.setBearingRequired(false); // 方位
        criteria.setAltitudeRequired(false); // 高度
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 耗电量
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 精确度
        criteria.setCostAllowed(true); // 耗费流量
        String bestProvider = manager.getBestProvider(criteria, false); // 不止返回一个

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = manager.getLastKnownLocation(bestProvider);
        if (location == null){
            Toast.makeText(context.getApplicationContext(),"位置获取失败",Toast.LENGTH_SHORT).show();
        }else {
            getCity(context);
        }

        // 一分钟刷新一次，改变的距离大于一公里时
        manager.requestLocationUpdates(bestProvider, 60000, 1000, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getCity(context);
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
        });

    }

    private static void getCity(Context context) {
        String city;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        // 地理编码  将经纬度转换为城市名
        Geocoder gc = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gc.getFromLocation(latitude,longitude,1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size()>0){
                Address address = addresses.get(0);
                sb.append(address.getLocality());
                city = sb.toString();
                SpUtils.putString(context,CITY,city);
            }else {
                Toast.makeText(context.getApplicationContext(),"经纬度转换城市失败",Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
