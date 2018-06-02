package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import models.User;
import utilities.Logging;
import utilities.Validation;
import javax.xml.bind.DatatypeConverter;

public class LoginController implements Initializable{

	@FXML
	private TextField textfield_username, textfield_password;
	@FXML 
	private Button button_login;
	@FXML
	private CheckBox checkbox_remember;
	@FXML 
	private Label label_forgetpassword, label_notify;
	
	Logger log = new Logging().getLogger();
	User user = new User();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Check rememeber is exist
		String u = user.getRemember();
		if (u.length() > 0){
			textfield_username.setText(u);
			checkbox_remember.setSelected(true);
		}
		
		//
		textfield_username.setOnKeyPressed(pressEnter);
		textfield_password.setOnKeyPressed(pressEnter);
		checkbox_remember.setOnKeyPressed(pressEnter);
		button_login.setOnKeyPressed(pressEnter);
	}
	
	@FXML
	public void login(){		
		Validation validation = new Validation();
		String username = textfield_username.getText();
		String	password = null;
		try {
			password = DatatypeConverter.printHexBinary( 
			           	MessageDigest.getInstance("MD5")
			           	.digest(textfield_password.getText().getBytes("UTF-8")));
		} catch (NoSuchAlgorithmException e) {
			log.info(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log.info(e.getMessage());
		}
		
		// Validating
		if (! validation.check(username, "alpha")){
			label_notify.setText("Username or Password incorrect");
			log.warning("[" + username + "] login failed");
			return;
		}
		// Check login
		if (user.login(username, password)){
			label_notify.setText("Login successful");
			// Check checked remember
			if (checkbox_remember.isSelected()){
				user.setRemember(user.getId());
			}else{
				user.removeRemember(user.getId());
			}
		}else{
			label_notify.setText("Username or password incorrect");
		}
		
		
	}
	
	private EventHandler<KeyEvent> pressEnter = new EventHandler<KeyEvent>() {
		@Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode() == KeyCode.ENTER){
            	login();
            }
        }
	};
}
