package com.jdc.pos.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordEncryptor {

	public static String encrypt(String password) {
		try {
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encode = digest.digest(password.getBytes());
			return Base64.getEncoder().encodeToString(encode);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String pass = encrypt("root");
		System.out.println(pass);
	}
}
