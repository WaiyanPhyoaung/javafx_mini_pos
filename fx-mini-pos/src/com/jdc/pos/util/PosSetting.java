package com.jdc.pos.util;

import java.io.FileInputStream;
import java.util.Properties;

public class PosSetting {
	
	private static Properties prop;
	
	static {
		
		try {
			prop = new Properties();
			prop.load(new FileInputStream("pos.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String get(String key) {
		return prop.getProperty(key);
	}
}
