package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import models.ProjectModel;

public class ProjectController implements Initializable{
	@FXML
	private Label label_projectName, label_notify;
	@FXML
	private TextField textfield_projectName;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Press enter then insert project's name into database and close windows
		textfield_projectName.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
	        public void handle(KeyEvent keyEvent) {
	            if (keyEvent.getCode() == KeyCode.ENTER){
	            	String n = textfield_projectName.getText();
	            	if (new ProjectModel().addProject(n)){
	            		Stage stage = (Stage)label_projectName.getScene().getWindow();
	           		 	stage.close();
	            	}else{
	            		label_notify.setText("Somethings error: Name is dupplicated");
	            	}
	            }
	        }
		});
	}	
	
}
