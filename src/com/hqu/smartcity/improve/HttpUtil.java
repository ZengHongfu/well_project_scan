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
 * ��������
 * @author hjh
 *
 */
public class HttpUtil {
	
	/**
	 * post����
	 * @param postContent ���ϴ�������
	 * @param url �ϴ���Ŀ�ĵ�ַ
	 * @return strResult ���صĽ��
	 */
	public static String post(String postContent, String url){
		String strResult = "";
		HttpURLConnection conn = null;
	    InputStream is = null;
		try {	
				Log.i("aaaa", "post:---"+postContent);
				postContent = URLEncoder.encode(postContent, "UTF-8");
				Log.i("aaaa", "post2:---"+postContent);
	    		byte[] data = postContent.getBytes();//���������
	            
	            conn = (HttpURLConnection) new URL(url).openConnection();
	            conn.setConnectTimeout(3000); // ���ó�ʱʱ��
	            conn.setReadTimeout(3000);
	            conn.setDoInput(true);//�����������Ա�ӷ�������ȡ����
	            conn.setDoOutput(true);//����������Ա���������ύ����
	            conn.setRequestMethod("POST"); //���û�ȡ��Ϣ��ʽ
	            conn.setRequestProperty("Charset", "UTF-8");//���ý������ݱ����ʽ
	            conn.setUseCaches(false);//ʹ��Post��ʽ����ʹ�û���
	            //������������������ı�����
	            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//	            conn.setRequestProperty("content-type", "text/html");  
	            //����������ĳ���
	            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
	            //�����������������д������
	            OutputStream outputStream = conn.getOutputStream();
	            outputStream.write(data);
	            Log.i("aaaa",conn.getResponseCode()+"----------hello");
	            if (conn.getResponseCode() == 200) {//����ɹ�
	                is = conn.getInputStream();
	                strResult = parseInfo(is);//0��ȷ��1����-1�쳣
	                Log.i("aaaa",strResult+"--------------------------------------------------");
	                if(strResult==null||"".equals(strResult)){
	                	strResult="-1";
	                }
	                if(!strResult.equals("0")&&!strResult.equals("1")&&!strResult.equals("-1")){
	                	strResult="-1";
	                }
	                return strResult;
	            }else if(conn.getResponseCode() == 404){//�Ҳ�����ҳ
	            	is = conn.getInputStream();
	            	strResult = "2";
	                return strResult;
	            }else if(conn.getResponseCode() == 408){//����ʱ
	            	is = conn.getInputStream();
	            	strResult = "3";
	                return strResult;
	            }else {//����
	            	is = conn.getInputStream();
	            	strResult = "4";
	                return strResult;
	            }

	        }catch (Exception e) {
	            e.printStackTrace();
	            Log.i("aaaa", "error------------------------------------------");
	            strResult ="-2";//�Ƿ������ַ
	        } finally {
	            // �����˳�ʱ�������ӹرձ���
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
	 * get����
	 * @param url �����Ŀ�ĵ�ַ
	 * @return strResult ������
	 */
	public static String get(String url){
		
		String strResult = "";
		HttpURLConnection conn = null;
        InputStream is = null;
        try {

            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(3000); // ���ó�ʱʱ��
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // ���û�ȡ��Ϣ��ʽ
            conn.setRequestProperty("Charset", "UTF-8"); // ���ý������ݱ����ʽ

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                strResult = parseInfo(is);
                return strResult;
            }

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // �����˳�ʱ�������ӹرձ���
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
	
	// ��������ת��Ϊ String�� 
    private static String parseInfo(InputStream inStream) throws Exception {
        byte[] data = read(inStream);
        // ת��Ϊ�ַ���
        return new String(data, "UTF-8");
    }

    // ��������ת��Ϊbyte�� 
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
     * �ж����������Ƿ�����
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
     * �ϴ�����
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
		
		//��MapתΪjson�ַ���
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
