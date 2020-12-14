package com.jdc.pos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	
	private final static String URL="jdbc:mysql://localhost:3306/jdc_mini_pos";
	private final static String USR="root";
	private final static String PASS="waiyanphyoaung";
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USR, PASS);
	}

}
