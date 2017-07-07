package cn.com.bonc.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ConfigHandle {

	private String filePath = null;

	public ConfigHandle(String filePath) {
		this.filePath = filePath;
	}

	@SuppressWarnings("resource")
	public Conf getConfigs() {
		Conf conf = new Conf();
		HashMap<String, String> map = new HashMap<String, String>();
		try {

			FileInputStream fis = new FileInputStream(this.filePath);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")||line.trim().length()==0)
					continue;
				
				String cols[] = StringUtils.split(line, "=");
				if(cols.length==2)
				map.put(cols[0], cols[1]);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
		configHandle(conf,map);
		return conf;
	}

	public boolean configHandle(Conf conf, HashMap map) {

		Field[] fields = conf.getClass().getFields();

		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName().trim();
			String type = fields[i].getGenericType().toString() ;
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				
				String key = ((String) entry.getKey()).trim();
				String val = ((String) entry.getValue()).trim();
				
				
				if (key.equals(fieldName)) {
					System.out.println(fieldName +"   "+ val);
					setAttribute(conf,fieldName,val,type);				}

			}
		}
		return true;
	}

	public void setAttribute(Conf conf, String attr, String value, String type) {
		try {
			 
			Method getMethod = conf.getClass().getMethod(
					"get" + attr.substring(0, 1).toUpperCase()				
					+ attr.substring(1));			
			Method setMethod = conf.getClass().getMethod(
					"set" + attr.substring(0, 1).toUpperCase()				
					+ attr.substring(1),getMethod.getReturnType());
			
			
			if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名

				setMethod.invoke(conf, value);
			}
			if (type.equals("class java.lang.Integer")) {
				setMethod.invoke(conf, Integer.valueOf(value));
			}
			if (type.equals("class java.lang.Short")) {
				setMethod.invoke(conf, Short.valueOf(value));
			}
			if (type.equals("class java.lang.Double")) {
				setMethod.invoke(conf, Double.valueOf(value));
			}
			if (type.equals("class java.lang.Boolean")) {
				setMethod.invoke(conf, Boolean.valueOf(value));
			}

		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
