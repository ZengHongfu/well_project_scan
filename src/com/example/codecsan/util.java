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
	 * ��ȡ��ǰ��γ��
	 * @param context
	 */
	public static void getLoc(Context context) {
	    // λ��
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
	     criteria.setAccuracy(Criteria.ACCURACY_FINE);// �߾���
	     criteria.setAltitudeRequired(false);// ��Ҫ�󺣰�
	     criteria.setBearingRequired(false);// ��Ҫ��λ
	     criteria.setCostAllowed(true);// �����л���
	     criteria.setPowerRequirement(Criteria.POWER_LOW);// �͹���
	     // �ӿ��õ�λ���ṩ���У�ƥ�����ϱ�׼������ṩ��
	     provider = locationManager.getBestProvider(criteria, true);
	     // ������һ�α仯��λ��
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
	     // ����λ�ñ仯��2��һ�Σ�����10������
	     locationManager.requestLocationUpdates(provider, 1000, 1,locationListener);
	}
	
	/**
     * ��ȡ�ֻ�IMEI��
     */
    public static void getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        Data.userId=imei;
    }
}
