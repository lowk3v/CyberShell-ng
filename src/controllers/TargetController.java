/**
 * 
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.ProjectModel;
import models.TargetModel;

/**
 * @author Kevinlpd
 *
 */
public class TargetController implements Initializable{
	
	@FXML
	private TextField textfield_targetName, textfield_url, textfield_password, textfield_ConnectionStr, textfield_ServerName, textfield_DBUser, textfield_DBPass;
	@FXML
	private TextArea textarea_description;
	@FXML
	private Label label_notify;
	@FXML
	private SplitMenuButton menubutton_project, menubutton_os, menubutton_lang;
	@FXML
	private AnchorPane anchorpane_root;
	@FXML
	private Button button_submit;
	

	private int project_id = 0;
	private String os = null, program_lang = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Load project list from database
		RadioMenuItem name;
		ToggleGroup toggle_project = new ToggleGroup();
		for (ProjectModel i : new ProjectModel().getList()) {
			name = new RadioMenuItem();
			name.setText(i.getName());
			name.setToggleGroup(toggle_project);
			menubutton_project.getItems().addAll(name);
		}
		// Press ENTER to call method addTarget
		anchorpane_root.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
	        public void handle(KeyEvent keyEvent) {
	            if (keyEvent.getCode() == KeyCode.ENTER){
	            	button_submit.fire();
	            }
	        }
		});
	}
	/*
	 *  [ACTION] When user pressed submit, it will get data and call addTarget model to insert into Targets table
	 */
	@FXML
	private void addTarget(){
		// Get operation system
    	menubutton_os.getItems().forEach((item)->{
    		if (item.getStyleClass().contains("selected")){
    			os = item.getText();
    		}
    	});
    	// Get programming languages
    	menubutton_lang.getItems().forEach((lang)->{
    		if (lang.getStyleClass().contains("selected")){
    			program_lang = lang.getText();
    		}
    	});
    	// Get project selected
    	menubutton_project.getItems().forEach((y)->{
    		if (y.getStyleClass().contains("selected")){
    			project_id = new ProjectModel().getProjectIDbyName(y.getText());
    		}
    	});
    	// Check insert into database 
    	Boolean isSuccess = new TargetModel().addTarget(
				textfield_targetName.getText().trim(),	// name
				textfield_url.getText().trim(),			// link
				textfield_password.getText().trim(),	// password
				textarea_description.getText().trim(),	// description
				project_id,								// project id
				textfield_ConnectionStr.getText().trim(),// connection str
				textfield_ServerName.getText().trim(),	// DB server
				textfield_DBUser.getText().trim(), 		// DB user
				textfield_DBPass.getText().trim(),		// DB pass
				os,										// Operating system 
				program_lang							// programming languages
			);
    	// Check textfield is valid
    	if (valid_textfield() && isSuccess && project_id != 0){
    		 Stage stage = (Stage) button_submit.getScene().getWindow();
    		 stage.close();
    	}else{
    		label_notify.setText("Somethings error");
    	}
	}
	/*
	 * Check textfield is not null
	 */
	private boolean valid_textfield(){
		// Text field is not null
		if (textfield_targetName.getText().trim().isEmpty()     // Name
			|| textfield_url.getText().trim().isEmpty()		// url
			|| textfield_password.getText().trim().isEmpty()	// pass
			){
			return false;
		}	
		return true;
	}


}
