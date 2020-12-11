package com.jdc.pos.views;

import java.net.URL;
import java.util.ResourceBundle;

import com.jdc.pos.util.LoginSetting;
import com.jdc.pos.util.PasswordEncryptor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class Login implements Initializable {

    @FXML
    private Label message;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;

    @FXML
    void cancel() {
    	message.getScene().getWindow().hide();
    }

    @FXML
    void login() {
    	try {
    		
    		LoginSetting.login(login.getText(), 
    				PasswordEncryptor.encrypt(password.getText()));
    		MainFrame.show();
    		cancel();
    		
		} catch (Exception e) {
			e.printStackTrace();
			message.setText(e.getMessage());
		}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		message.setText("");
		password.setOnKeyPressed( a -> {
			if(a.getCode().equals(KeyCode.ENTER)) {
				login();
			}
		});
	}

}
