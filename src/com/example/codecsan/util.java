package com.example.codecsan;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class util {
	/**
	 * 获取当前经纬度
	 * @param context
	 */
	public static void getLoc(Context context) {
	    // 位置
	     LocationManager locationManager;
	     LocationListener locationListener;
	     Location location;
	     String contextService = Context.LOCATION_SERVICE;
	     String provider;
	     locationManager = (LocationManager) context.getSystemService(contextService);
	     if(locationManager==null){
	    	 return;
	     }
	     Criteria criteria = new Criteria(); 
	     criteria.setAccuracy(Criteria.ACCURACY_FINE);// 高精度
	     criteria.setAltitudeRequired(false);// 不要求海拔
	     criteria.setBearingRequired(false);// 不要求方位
	     criteria.setCostAllowed(true);// 允许有花费
	     criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
	     // 从可用的位置提供器中，匹配以上标准的最佳提供器
	     provider = locationManager.getBestProvider(criteria, true);
	     // 获得最后一次变化的位置
	     location = locationManager.getLastKnownLocation(provider); 
	     if(location!=null){
	    	 Data.latitude = (location.getLatitude()+"").trim();
		     Data.longitude = (location.getLongitude()+"").trim();
	     }
	   
	     locationListener = new LocationListener() {
	    
		     public void onProviderEnabled(String provider) {
		    	 // TODO Auto-generated method stub
		     }
		     public void onProviderDisabled(String provider) {
		    	 // TODO Auto-generated method stub
		     }
		     public void onLocationChanged(Location location) {
			     Data.latitude = (location.getLatitude()+"").trim();
			     Data.longitude = (location.getLongitude()+"").trim();
		     }
			 @Override
			 public void onStatusChanged(String provider, int status, Bundle extras) {
				 // TODO Auto-generated method stub
				
			 }
	     };
	     // 监听位置变化，2秒一次，距离10米以上
	     locationManager.requestLocationUpdates(provider, 1000, 1,locationListener);
	}
	
	/**
     * 获取手机IMEI号
     */
    public static void getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        Data.userId=imei;
    }
}
