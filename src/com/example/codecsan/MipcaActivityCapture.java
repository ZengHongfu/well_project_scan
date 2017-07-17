package com.example.codecsan;

import java.io.IOException;
import java.util.Vector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hqu.smartcity.improve.HttpUtil;
import com.hqu.well_project.ClearEditText;
import com.hqu.well_project_scan.R;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
/**
 * Initial the camera
 * @author Ryan.Tang
 */
@SuppressLint("Override")
public class MipcaActivityCapture extends Activity implements Callback,ActivityCompat.OnRequestPermissionsResultCallback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private static final int CAMERA_OK = 0;
	private boolean vibrate;
	
	
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		
	     /*------------------百度地图定位相关-----------------*/
	     //声明LocationClient类
		mLocationClient = new LocationClient(getApplicationContext());     
		//注册监听函数
		 mLocationClient.registerLocationListener( myListener );    
		 initLocation();
		
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		
		Button mButtonBack = (Button) findViewById(R.id.button_exit);
		mButtonBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MipcaActivityCapture.this.finish();
				
			}
		});
		
		Button mButtonSet = (Button) findViewById(R.id.button_set);
		mButtonSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSetWebsiteDialog();
			}
		});

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		
		CameraManager.init(getApplication());
		
		if(!gPSIsOPen(getApplicationContext())){
			enSure("GPS定位未打开，是否去开启？",2);
		}else{
			Data.website=getWebsite(getApplicationContext(), "website");
			if(Data.website!=null&&!Data.website.equals("")){
			}else{
				showSetWebsiteDialog();
			}

			requestPermissions();//请求权限
		}
		
		
	   //动态注册广播 ，监听网络变化 	  
       IntentFilter mFilter = new IntentFilter();  
       mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);  
       registerReceiver(myNetReceiver, mFilter);  
       
       
	}
	
	/**
	 * 初始化定位参数
	 */
	private void initLocation(){
	    LocationClientOption option = new LocationClientOption();
	    option.setLocationMode(LocationMode.Hight_Accuracy);
	    //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
	 
	    option.setCoorType("bd09ll");
	    //可选，默认gcj02，设置返回的定位结果坐标系
	 
	    int span=1000;
	    option.setScanSpan(span);
	    //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
	 
	    option.setIsNeedAddress(true);
	    //可选，设置是否需要地址信息，默认不需要
	 
	    option.setOpenGps(true);
	    //可选，默认false,设置是否使用gps
	 
	    option.setLocationNotify(true);
	    //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
	 
	    option.setIsNeedLocationDescribe(true);
	    //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
	 
	    option.setIsNeedLocationPoiList(true);
	    //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
	 
	    option.setIgnoreKillProcess(false);
	    //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
	 
	    option.SetIgnoreCacheException(false);
	    //可选，默认false，设置是否收集CRASH信息，默认收集
	 
	    option.setEnableSimulateGps(false);
	    //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
	 
	    mLocationClient.setLocOption(option);
	}
	
	/**
	 * 权限请求
	 */
	private boolean  ownAllPermissions=false;
	private  final int REQUEST_PERMISSIONS = 1;
	private void requestPermissions() {
	    if (Build.VERSION.SDK_INT >= 23) {
	        int callPhonePermission = ContextCompat.checkSelfPermission
	        		(this, Manifest.permission.CAMERA);
	        int callPhonePermission2 = ContextCompat.checkSelfPermission
	        		(this, Manifest.permission.ACCESS_COARSE_LOCATION);
	        int callPhonePermission3 = ContextCompat.checkSelfPermission
	        		(this, Manifest.permission.READ_PHONE_STATE);
	        StringBuilder sb=new StringBuilder();
	        if(callPhonePermission != PackageManager.PERMISSION_GRANTED){
	        	sb.append(Manifest.permission.CAMERA+" ");
	        }
	        if(callPhonePermission2 != PackageManager.PERMISSION_GRANTED){
	        	sb.append(Manifest.permission.ACCESS_COARSE_LOCATION+" ");
	        }
	        if(callPhonePermission3 != PackageManager.PERMISSION_GRANTED){
	        	sb.append(Manifest.permission.READ_PHONE_STATE+" ");
	        }
	        
	        if(sb.toString().trim()!=null&&!sb.toString().trim().equals("")){
	        	String[] requsstPermissions = sb.toString().trim().split(" "); 
	        	ActivityCompat.requestPermissions(this, requsstPermissions, REQUEST_PERMISSIONS);
	        	if(setDialog!=null){
	        		setDialog.dismiss();
	        	}
	        }else{
	        	ownAllPermissions=true;
	        	mLocationClient.start();
	        }
	    }else{
        	ownAllPermissions=true;
        	mLocationClient.start();
        }
	}	
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
	       switch (requestCode){
	           case REQUEST_PERMISSIONS:
	               if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
	            	   //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
	                   Toast.makeText(MipcaActivityCapture.this,"未获取到相机权限！",Toast.LENGTH_SHORT).show();
	               }else{
	            	   if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
	                	   if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED){
	                		   ownAllPermissions=true;
	                		   mLocationClient.start();
	                	   }
	                   }
	               }
	               
	               Data.website=getWebsite(getApplicationContext(), "website");
		       		if(Data.website!=null&&!Data.website.equals("")){
		       		}else{
		       			showSetWebsiteDialog();
		       		}
	               break;
	           default:
	               break;
	       }
	}
	
	
	/**
	 * 处理扫描结果
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText().trim();
		if (resultString.equals("")) {
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			continuePreview();//继续扫描
		}else {
			requestPermissions();//再次检查权限
    		setExtraDialog(resultString);	
		}
	}
	
	
	/*--------------------------------------------------------------------------------------*/
	/*---------------------------以下为扫描结果处理、上传相关代码---------------------------------------*/
	
   
	
	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	/**
	 * 扫描后弹出对话框
	 * @param resultString
	 */
    Dialog dialog;
	public void setExtraDialog(final String resultString){
		dialog = new AlertDialog.Builder(MipcaActivityCapture.this).create();
		//new一个空的edittext，解决输入法无法弹出的问题
		((AlertDialog) dialog).setView(new EditText(this));
		
		dialog.setCancelable(true);// 可以用“返回键”取消  
		dialog.setCanceledOnTouchOutside(false);//
		dialog.show();
		
		//设置dialog宽度
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes(); 
		params.width = MipcaActivityCapture.this.getWindowManager().getDefaultDisplay().getWidth() - 100 ;
		dialog.getWindow().setAttributes(params);
		View view = LayoutInflater.from(MipcaActivityCapture.this).inflate(R.layout.z_housenumber_set_dialog, null);
		dialog.setContentView(view);
		
		Button btnOk = (Button) view.findViewById(R.id.btn_housenumber_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_housenumber_cancel);
		final ClearEditText extraEt = (ClearEditText) view.findViewById(R.id.extra_et);
		
		//确定
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		

				//保存扫描信息
				Data.message=resultString;
				//保存用户输入的备注信息
				Data.extra=extraEt.getText().toString().trim();
				
				Data.website=getWebsite(getApplicationContext(), "website");
	       		if(Data.website!=null&&!Data.website.equals("")){
	       			dialog.dismiss();
	       			enSure("确认上传信息？",1);	
	       		}else{
					showSetWebsiteDialog();
				}
				
			}	
		});
		//取消
		btnCancel.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				continuePreview();//继续扫描
			}
		});
    }	
	/**
	 * 显示填写设备信息弹窗
	 */
	private void showDialog(){
		dialog.show();
	}

    /**
     * 显示设置网址对话框
     */
	Dialog setDialog;
	private void showSetWebsiteDialog(){
		setDialog = new AlertDialog.Builder(MipcaActivityCapture.this).create();
		//new一个空的edittext，解决输入法无法弹出的问题
		((AlertDialog) setDialog).setView(new EditText(this));
		
		setDialog.setCancelable(true);// 可以用“返回键”取消  
		setDialog.setCanceledOnTouchOutside(false);//
		setDialog.show();
		
		//设置setDialog宽度
		WindowManager.LayoutParams params = setDialog.getWindow().getAttributes(); 
		params.width = MipcaActivityCapture.this.getWindowManager().getDefaultDisplay().getWidth() - 100 ;
		setDialog.getWindow().setAttributes(params);
		View view = LayoutInflater.from(MipcaActivityCapture.this).inflate(R.layout.set_website, null);
		setDialog.setContentView(view);
		
		Button btnOk = (Button) view.findViewById(R.id.btn_website_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_website_cancel);
		final ClearEditText websiteEt = (ClearEditText) view.findViewById(R.id.website_et);
		
		Data.website=getWebsite(getApplicationContext(), "website");
		if(Data.website!=null&&!Data.website.equals("")){
			websiteEt.setText(Data.website);
		}
		
		//确定
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		
				String website=websiteEt.getText().toString().trim();
				if(website!=null&&!website.equals("")){
					saveWebsite(getApplicationContext(), website);
					Data.website=getWebsite(getApplicationContext(), "website");
					setDialog.dismiss();
					showToast("保存成功！");
				}else{
					showToast("网址不能为空！");
				}
				
				
			 
			}	
		});
		//取消
		btnCancel.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				setDialog.dismiss();
			}
		});
	}
	
	
	
	/**
	 * 从本地获取网址信息
	 */
	public String getWebsite(Context context, String key) {
	    SharedPreferences sp = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
	    String result = sp.getString(key, "");
	    return result;
	}
	/**
	 * 保存网址信息到本地
	 */
	public void saveWebsite(Context context,String website) {
	    SharedPreferences sp = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
	    Editor editor = sp.edit();
	    editor.putString("website", website);
	    editor.commit();
	}
	/**
	 * 操作确认弹窗
	 * @param msg
	 */
	private void enSure(String msg,final int type){
		new AlertDialog.Builder(this).setTitle("系统提示")//设置对话框标题   
	     .setMessage(msg)//设置显示的内容  
	     .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮            
	         @Override
	         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  	
	        	 switch (type){
	        	 case 1:
	        		 if(!HttpUtil.isNetworkConnected(getApplicationContext())){
		        		 showDialog();
		        		 showToast("网络异常，请稍后重试！"); 
		        		 return;
		        	 }
		        	 
		        	 if(!ownAllPermissions){
		        		 showDialog();
		        		 requestPermissions();
		        		 showToast("未获取相关权限，无法上传数据！");
		        		 return;
		        	 }
//		        	 util.getLoc(getApplicationContext());
		        	 util.getIMEI(getApplicationContext());
		        	 updata();	//上传设备数据   
		        	 
	        		 break;
	        	 case 2:
	        		 openGPSSettings();
	        		 break;
	        	default:
	        			 break;
	        	 }
	         }  
	     }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮    	  
	         @Override  
	         public void onClick(DialogInterface dialog, int which) {//响应事件  
	        	 switch (type){
	        	 case 1:
	        		 showDialog();
//		        	 continuePreview();//继续扫描
		        	 
	        		 break;
	        	 case 2:
	        		 MipcaActivityCapture.this.finish();
	        		 break;
	        	default:
	        			 break;
	        	 }
	         }  
	     }).show();//在按键响应事件中显示此对话框  
	}
	
	//上传数据
	Handler handler_update;
	@SuppressLint("HandlerLeak")
	public  void updata(){
		 handler_update = new Handler(){

			@Override
			public void handleMessage(Message msg) {	        					
				String result =  (String) msg.obj;
				Log.i("aaaa",result);
				if("0".equals(result)){
					showToast("上传成功！");
				}else if("-1".equals(result)){
					showToast("服务器错误，请稍后重试！");
				}else if("1".equals(result)){
					showToast("数据有误，请重试！");
				}else if("3".equals(result)){
					showToast("请求超时，请稍后重试！");
				}else{
					showToast("上传失败，请检查上传网址是否配置正确！");
				}
				
				continuePreview();//继续扫描
			}
 		 };
 		 new Thread(new Runnable() {
			@Override
			public void run() {
//				Log.i("aaaa",Data.userId+"\n"+Data.message+"\n"+Data.extra+"\n"+Data.latitude+"\n"+Data.longitude);
				String result=HttpUtil.postData();
//				Log.i("aaaa", "result:"+result);
				
				Message message=new Message();
				message.obj=result;
				handler_update.sendMessage(message);
			}
		}).start();
 		 						
	}
	
	/**
	 * 判断当前位置服务是否开启
	 * @param context
	 * @return
	 */ static final boolean gPSIsOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gps) {
            return true;
        }
        return false;
    }
	public static final boolean locationIsOPen(final Context context) {
		LocationManager locationManager
		= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}
		return false;
	}
	
	/**
	 * GPS设置
	 */
	int GPS_REQUEST_CODE=1;
	private void openGPSSettings() {
	    LocationManager locationManager = (LocationManager) this
	            .getSystemService(Context.LOCATION_SERVICE);
	    if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
	        //GPS正常开启，可以写入需要开启GPS才能执行的方法 
	        //也可在onActivityResult()方法写
	    } else {
	        //调转GPS设置界面
	        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	        //此为设置完成后返回到获取界面
	        startActivityForResult(intent, GPS_REQUEST_CODE);
	    }
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == GPS_REQUEST_CODE) {
	        // ............自己需要执行的方法
	    	MipcaActivityCapture.this.recreate();//重新create()
	    }
	}
	
	/**
	 * 显示Toast消息
	 * @param msg
	 */
	Toast mToast;
	private void showToast(String msg){
		if(mToast == null){
			mToast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		}else{
			mToast.setText(msg);
		}
		mToast.show();
	}

		
	private ConnectivityManager mConnectivityManager; 		  	
	private NetworkInfo netInfo;   	
	//监听网络状态变化的广播接收器  
	private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {  
		@Override  
		public void onReceive(Context context, Intent intent) {  
		    
			String action = intent.getAction();  
	        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {  
	             
	            mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);  
	            netInfo = mConnectivityManager.getActiveNetworkInfo();    
	            if(netInfo != null && netInfo.isAvailable()) {  
	  
	                 /////////////网络连接  
	                String name = netInfo.getTypeName();  
	                  
	                if(netInfo.getType()==ConnectivityManager.TYPE_WIFI ||
	                   netInfo.getType()==ConnectivityManager.TYPE_MOBILE){  
	               
							//此时网络已恢复，若本地保存有未上传的信息，则开启重传
				
	                }else if(netInfo.getType()==ConnectivityManager.TYPE_ETHERNET){  
	                /////有线网络  
	                	//showToast("有线网络");
	                }                      
	            } else {  
	            	////////网络断开  
	            	showToast("网络已断开！");
	            }  
	        }    
		}   
	};   
	
			
	
			
			
	/*--------------------------------------------------------------------------------*/
	/*---------------------------------以下非业务逻辑--------------------------------------*/
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	
	@Override
	protected void onResume() {
		super.onResume();
		
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		if(myNetReceiver!=null){  
			unregisterReceiver(myNetReceiver);  
	    }  
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	/**
	 * 连续扫描
	 */
	public void continuePreview() {  
        if (handler != null) {  
            handler.restartPreviewAndDecode();  
        }  
    }  
	/**
	 * 暂停扫描，好像没效果，暂时不管
	 */
//	public void stopScan() {  
//		if (handler != null) {
//			handler.quitSynchronously();
//			handler = null;
//		}
//		if(CameraManager.get()!=null){
//			CameraManager.get().closeDriver();
//		}
//		
//	}  
	
	
}