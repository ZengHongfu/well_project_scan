package com.hqu.smartcity.improve;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class JsonUtils {

	/**
	 * jsonStrתMap
	 * @param jsonStr
	 * @return Map
	 */
	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		
		JSONObject jsonObject;  
        try {  
            jsonObject = new JSONObject(jsonStr);                
            Iterator<String> keyIter= jsonObject.keys();  
            String key;  
            Object value ;  
            Map<String, Object> valueMap = new HashMap<String, Object>();  
            while (keyIter.hasNext()) {  
                key = keyIter.next();  
                value = jsonObject.get(key);  
                valueMap.put(key, value);  
            }  
            return valueMap;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  

	}
	
	
	public static List<Map<String, String>> getResultMap(String data,int type) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		try {
			JSONObject jsonObject = new JSONObject(data);
			JSONArray jsonArray = jsonObject.getJSONArray("data");
		for (int i = 0; i < jsonArray.length(); i++) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
		if(type==1){//�豸�б�
			for(int ii=0;ii<jsonObject1.length();ii++){
				Iterator<String> iterator = jsonObject1.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if(key.equals("deviceid") && ii==0){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("devicecode") && ii==1){//�ʲ����
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("devicealias") && ii==2){//�ʲ�����
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("devicetype") && ii==3){//�ʲ�����
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("mtype") && ii==4){//ģ������
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("addr") && ii==5){//��ϸ��ַ
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("msuid") && ii==6){//�ն�id
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}					
					else if(key.equals("warnflag") && ii==7){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("utype") && ii==8){//����������
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}
					
					}
			}
		}else if(type==2){//�����б�
			for(int ii=0;ii<jsonObject1.length();ii++){
				Iterator<String> iterator = jsonObject1.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if(key.equals("orderid") && ii==0){
						String value = jsonObject1.getString(key);
						map.put(key, value);
//					}else if(key.equals("deviceid") && ii==1){
//						String value = jsonObject1.getString(key);
//						map.put(key, value);
//					}
					}else if(key.equals("devicecode") && ii==2){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("devicealias") && ii==3){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("status") && ii==4){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("uptime") && ii==5){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}
					
					}
			}
		}else if(type==3){//�澯�б�
			for(int ii=0;ii<jsonObject1.length();ii++){
				Iterator<String> iterator = jsonObject1.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if(key.equals("warnid") && ii==0){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("uid") && ii==1){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("mtype") && ii==2){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("warntype") && ii==3){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("warnvalue") && ii==4){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("judgevalue") && ii==5){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("uptime") && ii==6){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}
					
					}
			}
		}else if(type==4){//��վ�б�
			for(int ii=0;ii<jsonObject1.length();ii++){
				Iterator<String> iterator = jsonObject1.keys();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if(key.equals("uid") && ii==0){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("apalias") && ii==1){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}else if(key.equals("status") && ii==2){
						String value = jsonObject1.getString(key);
						map.put(key, value);
					}
					
					}
			}
		}
		list.add(map);
		}
		
		
		} catch (JSONException e) {
		e.printStackTrace();
		}
		return list;
		}

	

	public static Map<String, Object> getMapByUrl(String url) {
		try {
			// ͨ��HTTP��ȡJSON����
			InputStream in = new URL(url).openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return parseJSON2Map(sb.toString());
		} catch (Exception e) {
			
		}
		return null;
	}

	/**
	 * json��ʽ���ַ��������е��ֶ�Ϊnull���ڽ���ת��json���ٽ��н�����
	 * Ϊnull���ֶεõ��Ĳ�����java���null������JSONNull��������Ҫ����null�жϡ�
	 * Ҫ����toString��toString�õ�����"null"�ַ������ٸ�"null"���бȽϼ��ɡ�
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, Object> parseTypeJsonNull2Null(Map<String, Object> map) {
		Set set = map.entrySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry mapentry = (Map.Entry) iterator.next();
			if (mapentry.getValue().toString().equals("null")) {
				mapentry.setValue(null);
				System.out.println(mapentry.getKey() + "/" + mapentry.getValue());
			}
		}
		return map;
	}

	/**
	 * ��mapתΪjson
	 * 
	 * @param map
	 * @param sb
	 * @return
	 */
	public static StringBuilder parseMap2Json(Map<?, ?> map, StringBuilder sb) {
		if (sb == null)
			sb = new StringBuilder();
		sb.append("{");
		Iterator<?> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iter.next();
			String key = entry.getKey() != null ? entry.getKey().toString() : "";
			sb.append("\"" + stringToJson(key) + "\":");
			Object o = entry.getValue();
			if (o instanceof List<?>) {
				List<?> l = (List<?>) o;
				parseList2Json(l, sb);
			} else if (o instanceof Map<?, ?>) {
				Map<?, ?> m = (Map<?, ?>) o;
				parseMap2Json(m, sb);
			} else {
				String val = entry.getValue() != null ? entry.getValue().toString() : "";
				sb.append("\"" + stringToJson(val) + "\"");
				// sb.append(stringToJson(val));
			}
			if (iter.hasNext())
				sb.append(",");
		}
		sb.append("}");
		return sb;
	}

	/**
	 * ��listתΪjson
	 * 
	 * @param lists
	 * @param sb
	 * @return
	 */
	public static StringBuilder parseList2Json(List<?> lists, StringBuilder sb) {
		if (sb == null)
			sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < lists.size(); i++) {
			Object o = lists.get(i);
			if (o instanceof Map<?, ?>) {
				Map<?, ?> map = (Map<?, ?>) o;
				parseMap2Json(map, sb);
			} else if (o instanceof List<?>) {
				List<?> l = (List<?>) o;
				parseList2Json(l, sb);
			} else {
				sb.append("\"" + o + "\"");
			}
			if (i != lists.size() - 1)
				sb.append(",");
		}
		sb.append("]");
		return sb;
	}


	/**
	 * ���ַ���תΪjson����
	 * 
	 * @param str
	 *            �����ַ���
	 * @return json�ַ���
	 */
	public static String stringToJson(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			case '\"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '/':
				sb.append("\\/");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
