package com.hqu.smartcity.improve;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class CommonUtil {
	
		/**
		 * StringתInt
		 * 
		 * @param s
		 * @return
		 */
		public static int String2Int(String s) {
			try {
				return Integer.parseInt(s);
			} catch (Exception e) {
				return 0;
			}
		}

		/**
		 * StringתLong
		 * 
		 * @param s
		 * @return
		 */
		public static Long String2Long(String s) {
			try {
				return Long.parseLong(s);
			} catch (Exception e) {
				return Long.parseLong("0");
			}
		}
		/**
		 * StringתDouble
		 * 
		 * @param s
		 * @return
		 */
		public static Double String2Double(String s) {
			try {
				return Double.parseDouble(s);
			} catch (Exception e) {
				return Double.parseDouble("0.00");
			}
		}
		/**
		 * StringתFloat
		 * 
		 * @param s
		 * @return
		 */
		public static Float String2Float(String s) {
			try {
				return Float.parseFloat(s);
			} catch (Exception e) {
				return Float.parseFloat("0.00");
			}
		}
		/**
		 * ʮ������ObjectתLong
		 * 
		 * @param s
		 * @return
		 */
		public static Long HexObject2Long(Object s) {
			try {
				return Long.parseLong(s.toString(),16);
			} catch (Exception e) {
				return Long.parseLong("0");
			}
		}
		
		/**
		 * ʮ������ObjectתInt
		 * 
		 * @param s
		 * @return
		 */
		public static Integer HexObject2Int(Object s) {
			try {
				return Integer.parseInt(s.toString(),16);
			} catch (Exception e) {
				return Integer.parseInt("0");
			}
		}
		/**
		 * ʮ����ת��λ��byte�ַ���
		 * @param s   ��ת����ʮ����ֵ
		 * @param length  ������ĳ���
		 * @return
		 */
		public static String Int2ByteString(int s,int length) {
			try {
				return String.format("%0"+length+"d", Long.parseLong(Integer.toBinaryString(s)));
			} catch (Exception e) {
				return String.format("%0"+length+"d", Long.parseLong(Integer.toBinaryString(s)));
			}
		}
		/**
		 * unixʱ����Ϊdatetime����
		 * 
		 * @param s
		 * @return
		 */
		public static Date ObjectUnix2DateStr(Object s) {
			try {
				return new java.util.Date(1000*Long.parseLong(s.toString()));
			} catch (Exception e) {
				return new Date();
			}
		}

		/**
		 * json�Ű��޸�
		 * 
		 * @param s
		 * @return
		 */
		public static String String2Rep(String s) {
			try {
				return s.replace(",", ",\n").replace("[", "\n[\n")
						.replace("{", "\n{\n").replace("}", "\n}\n")
						.replace("]", "\n]\n");
			} catch (Exception e) {
				return s;
			}
		}

		/**
		 * Json����Map
		 * 
		 * @param jsonString
		 * @return
		 * @throws JSONException
		 */
		public static Map<String, Object> jsonToMap(String jsonString){
			Map<String, Object> resultMap  = new HashMap<String,Object>();
			try {
				// JSONObject������"{"��ͷ
				jsonString = jsonString.replace("=,", "=\"\",");
				JSONObject jsonObject = new JSONObject(jsonString);
				resultMap = new HashMap<String, Object>();
				Iterator<String> iter = jsonObject.keys();
				String key = null;
				Object value = null;
				while (iter.hasNext()) {
					key = iter.next();
					value = jsonObject.get(key);
					resultMap.put(key, value);
				}
			} catch (Exception e) {
				resultMap = null;
			}
			return resultMap;
		}
		
		/**8
		* Convert byte[] to hex string.�������ǿ��Խ�byteת����int��Ȼ������Integer.toHexString(int)��ת����16�����ַ�����   
		 * @param src byte[] data   
		 * @return hex string   
		 */      
		public static String bytesToHexString(byte[] src){   
		    StringBuilder stringBuilder = new StringBuilder("");   
		    if (src == null || src.length <= 0) {   
		        return null;   
		    }   
		    for (int i = 0; i < src.length; i++) {   
		        int v = src[i] & 0xFF;   
		        String hv = Integer.toHexString(v);   
		        if (hv.length() < 2) {   
		            stringBuilder.append(0);   
		        }   
		        stringBuilder.append(hv);   
		    }   
		    return stringBuilder.toString();   
		}   
	  
		public static String StringtoHexString(String s) {
			String str = "";
			for (int i = 0; i < s.length(); i++) {
				int ch = (int) s.charAt(i);
				String s4 = Integer.toHexString(ch & 0xFF);
				if (s4.length() == 1) {
					s4 = '0' + s4;
				}
				str = str + s4;
			}
			return str;// 0x��ʾʮ������
		}
		/**  
		 * Convert hex string to byte[]  
		 * @param hexString the hex string  
		 * @return byte[]  
		 */  
		public static byte[] HexString2Bytes(String src) {
			int size = src.length();
			byte[] ret = new byte[size / 2];
			byte[] tmp = src.getBytes();
			for (int i = 0; i < size / 2; i++) {
				ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
			}
			return ret;
		}
		private static byte uniteBytes(byte src0, byte src1) {
			char _b0 = (char) Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
			_b0 = (char) (_b0 << 4);
			char _b1 = (char) Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
			byte ret = (byte) (_b0 ^ _b1);
			return ret;
		}
		
		/**
		  * ��ȡ������汾��Ϣ
		  * @date:2016-9-19
		  * @author:jiangwy
		  * @param agent
		  * @return
		  */
		public static String getBrowserName(String agent) {
			  if(agent.indexOf("msie 7")>0){
			   return "ie7";
			  }else if(agent.indexOf("msie 8")>0){
			   return "ie8";
			  }else if(agent.indexOf("msie 9")>0){
			   return "ie9";
			  }else if(agent.indexOf("msie 10")>0){
			   return "ie10";
			  }else if(agent.indexOf("msie")>0){
			   return "ie";
			  }else if(agent.indexOf("opera")>0){
			   return "opera";
			  }else if(agent.indexOf("opera")>0){
			   return "opera";
			  }else if(agent.indexOf("firefox")>0){
			   return "firefox";
			  }else if(agent.indexOf("webkit")>0){
			   return "webkit";
			  }else if(agent.indexOf("gecko")>0 && agent.indexOf("rv:11")>0){
			   return "ie11";
			  }else{
			   return "Others";
			  }
		}
		
		/**
		 * ƴ��List��Map��ĳ���ض�����Ӧ������ֵ
		 * @param list
		 * @param param
		 * @return
		 */
		public static String JointListParam(List<Map<String,Object>> list , String Key, String jointParam){
			for(int i = 0; i< list.size();i++){
				if(list.get(i).get(Key) != null && list.get(i).get(Key) != "" ){
					if(jointParam == null || jointParam.length() <= 0){
						jointParam = list.get(i).get(Key).toString();   
				       }else{
				    	   jointParam = jointParam + "," + list.get(i).get(Key).toString();
				       }
				}
		    }	
			return jointParam;
		}
		
}	
