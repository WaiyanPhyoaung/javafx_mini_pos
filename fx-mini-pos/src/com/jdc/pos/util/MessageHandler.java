package com.jdc.pos.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MessageHandler {

	private static Alert alert = new Alert(null);
	
	static {
		alert.setResizable(true);
	}
	
	public static void showAlert(String message) {
		
		alert.setAlertType(AlertType.INFORMATION);
		alert.setHeaderText("Message from POS Application");
		alert.setContentText(message);
		alert.setTitle("About");
		alert.show();
	}
	
	public static void showAlert(Exception e) {
		
		AlertType type = e instanceof PosException ? 
				AlertType.WARNING : AlertType.ERROR;
		
		String message = null;
		
		if(null == e.getMessage()) 
			message = "Please contact to Dev Team !";
		else 
			message = e.getMessage();
			
		alert.setAlertType(type);
		alert.setHeaderText("Error in Application");
		alert.setContentText(message);
		alert.setTitle("Error");
		alert.show();
	}
	
	public static void toFront() {
		Stage window = (Stage) alert.getDialogPane().getScene().getWindow();
		window.setAlwaysOnTop(true);
	}
}
