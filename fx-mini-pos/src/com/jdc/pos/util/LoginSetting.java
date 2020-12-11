package com.jdc.pos.util;

public class LoginSetting {
	
	public static void login(String loginId, String password) {
		
		// login empty
		if(CheckString.empty(loginId)) {
			throw new PosException("LoginId should not be empty !");
		}
		
		// password empty
		if(CheckString.empty(password)) {
			throw new PosException("Password should not be empty !");
		}
		
		// login wrong
		if(!loginId.equals(PosSetting.get("pos.user.name"))) {
			throw new PosException("Please check loginId !");
		}
		
		// password wrong
		if(!password.equals(PosSetting.get("pos.user.password"))) {
			throw new PosException("Please check password !");
		}
	}

}
