package com.hqu.smartcity.improve;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.codecsan.Data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 网络请求
 * @author hjh
 *
 */
public class HttpUtil {
	
	/**
	 * post请求
	 * @param postContent 待上传的内容
	 * @param url 上传的目的地址
	 * @return strResult 返回的结果
	 */
	public static String post(String postContent, String url){
		String strResult = "";
		HttpURLConnection conn = null;
	    InputStream is = null;
		try {	
				Log.i("aaaa", "post:---"+postContent);
				postContent = URLEncoder.encode(postContent, "UTF-8");
				Log.i("aaaa", "post2:---"+postContent);
	    		byte[] data = postContent.getBytes();//获得请求体
	            
	            conn = (HttpURLConnection) new URL(url).openConnection();
	            conn.setConnectTimeout(3000); // 设置超时时间
	            conn.setReadTimeout(3000);
	            conn.setDoInput(true);//打开输入流，以便从服务器获取数据
	            conn.setDoOutput(true);//打开输出流，以便向服务器提交数据
	            conn.setRequestMethod("POST"); //设置获取信息方式
	            conn.setRequestProperty("Charset", "UTF-8");//设置接收数据编码格式
	            conn.setUseCaches(false);//使用Post方式不能使用缓存
	            //设置请求体的类型是文本类型
	            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//	            conn.setRequestProperty("content-type", "text/html");  
	            //设置请求体的长度
	            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
	            //获得输出流，向服务器写入数据
	            OutputStream outputStream = conn.getOutputStream();
	            outputStream.write(data);
	            Log.i("aaaa",conn.getResponseCode()+"----------hello");
	            if (conn.getResponseCode() == 200) {//请求成功
	                is = conn.getInputStream();
	                strResult = parseInfo(is);//0正确，1错误，-1异常
	                Log.i("aaaa",strResult+"--------------------------------------------------");
	                if(strResult==null||"".equals(strResult)){
	                	strResult="-1";
	                }
	                if(!strResult.equals("0")&&!strResult.equals("1")&&!strResult.equals("-1")){
	                	strResult="-1";
	                }
	                return strResult;
	            }else if(conn.getResponseCode() == 404){//找不到网页
	            	is = conn.getInputStream();
	            	strResult = "2";
	                return strResult;
	            }else if(conn.getResponseCode() == 408){//请求超时
	            	is = conn.getInputStream();
	            	strResult = "3";
	                return strResult;
	            }else {//其他
	            	is = conn.getInputStream();
	            	strResult = "4";
	                return strResult;
	            }

	        }catch (Exception e) {
	            e.printStackTrace();
	            Log.i("aaaa", "error------------------------------------------");
	            strResult ="-2";//非法请求地址
	        } finally {
	            // 意外退出时进行连接关闭保护
	            if (conn != null) {
	                conn.disconnect();
	            }
	            if (is != null) {
	                try {
	                    is.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }

	        }
	        return strResult;
	}
	
	/**
	 * get请求
	 * @param url 请求的目的地址
	 * @return strResult 请求结果
	 */
	public static String get(String url){
		
		String strResult = "";
		HttpURLConnection conn = null;
        InputStream is = null;
        try {

            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(3000); // 设置超时时间
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                strResult = parseInfo(is);
                return strResult;
            }

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
		return strResult;	
	}
	
	// 将输入流转化为 String型 
    private static String parseInfo(InputStream inStream) throws Exception {
        byte[] data = read(inStream);
        // 转化为字符串
        return new String(data, "UTF-8");
    }

    // 将输入流转化为byte型 
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }
    
    /**
     * 判断网络连接是否正常
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {  
	      if (context != null) {  
	          ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                  .getSystemService(Context.CONNECTIVITY_SERVICE);  
	          NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	          if (mNetworkInfo != null) {  
	              return mNetworkInfo.isAvailable();  
	          }  
	      }  
	     return false;  
    }
    
    /**
     * 上传数据
     * @return
     */
    public static String postData(){
    	Map<String, Object> paramMap = new LinkedHashMap<String, Object>();
		try{
			paramMap.put("userId", Data.userId);
			paramMap.put("longitude", Data.longitude);
			paramMap.put("latitude", Data.latitude);
			paramMap.put("message", Data.message);
			paramMap.put("remark", URLEncoder.encode(Data.extra, "UTF-8"));
		}
		catch(Exception e){
			e.printStackTrace();
			return "-1";
		}
		
		//将Map转为json字符串
		StringBuilder json = null;
		json = JsonUtils.parseMap2Json(paramMap,json);
		String jsonStr = json.toString();
		
		String postContent = jsonStr;
		
		String result = null;
		String path = Data.website;
            
        result = HttpUtil.post(postContent, path);
        
		return result;
	}
    
}
