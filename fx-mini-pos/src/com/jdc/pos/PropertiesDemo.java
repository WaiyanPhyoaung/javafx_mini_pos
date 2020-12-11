package com.jdc.pos;

import java.util.Properties;

public class PropertiesDemo {
	
	public static void main(String[] args) {
		Properties prop = System.getProperties();
		prop.list(System.out);
		
		String name = System.getProperty("os.name");
		System.out.printf("%n%nOS Name: %s", name);
	}

}
