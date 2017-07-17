package com.example.codecsan;

import java.util.List;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;

public class MyLocationListener implements BDLocationListener {

	@Override
	public void onConnectHotSpotMessage(String arg0, int arg1) {
		
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		Data.latitude = (location.getLatitude()+"").trim();
	    Data.longitude = (location.getLongitude()+"").trim();
		
		 //?????λ???
//        StringBuffer sb = new StringBuffer(256);
// 
//        sb.append("time : ");
//        sb.append(location.getTime());    //?????λ???
// 
//        sb.append("\nerror code : ");
//        sb.append(location.getLocType());    //???????????
// 
//        sb.append("\nlatitude : ");
//        sb.append(location.getLatitude());    //???γ?????
// 
//        sb.append("\nlontitude : ");
//        sb.append(location.getLongitude());    //??????????
// 
//        sb.append("\nradius : ");
//        sb.append(location.getRadius());    //?????λ?????
// 
//        if (location.getLocType() == BDLocation.TypeGpsLocation){
// 
//            // GPS??λ???
//            sb.append("\nspeed : ");
//            sb.append(location.getSpeed());    // ??λ???????С?
// 
//            sb.append("\nsatellite : ");
//            sb.append(location.getSatelliteNumber());    //?????????
// 
//            sb.append("\nheight : ");
//            sb.append(location.getAltitude());    //??????θ?????????λ??
// 
//            sb.append("\ndirection : ");
//            sb.append(location.getDirection());    //??????????????λ??
// 
//            sb.append("\naddr : ");
//            sb.append(location.getAddrStr());    //?????????
// 
//            sb.append("\ndescribe : ");
//            sb.append("gps??λ???");
// 
//        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
// 
//            // ???綨λ???
//            sb.append("\naddr : ");
//            sb.append(location.getAddrStr());    //?????????
// 
//            sb.append("\noperationers : ");
//            sb.append(location.getOperators());    //???????????
// 
//            sb.append("\ndescribe : ");
//            sb.append("???綨λ???");
// 
//        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
// 
//            // ?????λ???
//            sb.append("\ndescribe : ");
//            sb.append("?????λ??????????λ????????Ч??");
// 
//        } else if (location.getLocType() == BDLocation.TypeServerError) {
// 
//            sb.append("\ndescribe : ");
//            sb.append("????????綨λ???????????IMEI?????嶨λ???loc-bugs@baidu.com??????????????");
// 
//        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
// 
//            sb.append("\ndescribe : ");
//            sb.append("???粻??????λ??????????????????");
// 
//        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
// 
//            sb.append("\ndescribe : ");
//            sb.append("????????Ч??λ????????λ?????????????????????????????????????????????????????????????");
// 
//        }
// 
//        sb.append("\nlocationdescribe : ");
//        sb.append(location.getLocationDescribe());    //λ?????廯???
// 
//        List<Poi> list = location.getPoiList();    // POI????
//        if (list != null) {
//            sb.append("\npoilist size = : ");
//            sb.append(list.size());
//            for (Poi p : list) {
//                sb.append("\npoi= : ");
//                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//            }
//        }
// 
//        Log.i("BaiduLocationApiDem", sb.toString());

	}

}
