package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import fxml.ShellView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.ProjectModel;
import models.TargetModel;

/**
 * @author Kevinlpd
 *	[CONTROLLER] Handler features in home view
 */
public class ManagementController implements Initializable{
	private boolean isOpenWindowShell = false;
	@FXML
	private MenuItem menuitem_addProject = new MenuItem();
	@FXML
	private MenuItem menuitem_addTarget;

	private Stage stage;
	private TabPane tabpane;
	private AnchorPane shellmanager;
	
	@FXML
	private ListView<TitledPane> listview_contents;
	/*
	 * [EVENT] When user double click to label, image or hbox
	 * PARAMETER:
	 * * Mouse event
	 * HANDLING:
	 * * Open new windows, call view shell
	 */
	private EventHandler<MouseEvent> double_left_click = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			if (MouseEvent.MOUSE_CLICKED != null && event.getClickCount() == 2){
				// Initial tab session of current click
				String target_id = ((HBox)event.getSource()).getId();
				TargetModel target = new TargetModel().getTargetById(target_id);
				Tab tab_session = new ShellView().create_tab_session(target);
				// Open windows shell manager if don't exist
				if (! isOpenWindowShell){
					isOpenWindowShell = true;
					tabpane = new ShellView().create_tabpane_session(tab_session);
					tabpane.setTabMinWidth(100);
					shellmanager = new AnchorPane(tabpane);
					stage = new ShellView().create_stage(shellmanager);
				}else{
					tabpane.getTabs().add(tab_session);
				}
				stage.show();
			}
		}
	}; 
	/*
	 * [EVENT] When user move mouse enters a node
	 */
	private EventHandler<MouseEvent> onfocus = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			((Label)event.getSource()).setStyle("-fx-background-color: #C3C3C3");
		}
	}; 
	/*
	 * [EVENT] When user move mouse exits a node
	 */
	private EventHandler<MouseEvent> outfocus = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			((Label)event.getSource()).setStyle("-fx-background-color: #ffffff");
		}
	}; 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listProjects();
	}
	/*
	 * [VIEW] Add button - Open new windows create new project
	 */
	@FXML
	public void addProject(){
		try {
			Parent child = FXMLLoader.load(getClass().getResource("/fxml/NewProjectScene.fxml"));
			Stage stage = new Stage(); 
			Scene scene = new Scene(child);
			stage.setScene(scene); 
			stage.setTitle("New Project - CyberShell-ng 2.0");
			stage.show(); 
			stage.setOnHiding(e->{
				listProjects();
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * [VIEW] Add button - Open new windows create new target 
	 */
	@FXML
	public void addTarget(){
		try {
			Parent child = FXMLLoader.load(getClass().getResource("/fxml/NewTargetScene.fxml"));
			Stage stage = new Stage(); 
			Scene scene = new Scene(child);
			stage.setScene(scene); 
			stage.setTitle("New Target - CyberShell-ng 2.0");
			stage.show(); 
			stage.setOnHiding(e->{
				listProjects();
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * [VIEW] Load project's list from database	 * 
	 */
	public void listProjects(){
		ObservableList<TitledPane> project_name = FXCollections.observableArrayList();
		TitledPane titledpane_listproject;
		ArrayList<TargetModel> targetList;
		VBox vbox_for_target;
		HBox hbox_for_iconNtext;
		ImageView icon;
		Label link;
		
		// Call getlist() to get all list project in database and add node to listview
		for (ProjectModel project : new ProjectModel().getList()) {
			// Create titled pane drop down
			titledpane_listproject = new TitledPane();
			titledpane_listproject.setExpanded(false);
			titledpane_listproject.setPadding(new Insets(0,0,-6,0));
			titledpane_listproject.setText(project.getName());
			// Get list target 
			targetList = new TargetModel().getList(project.getId());
			vbox_for_target = new VBox();
			// Add item in list to titled pane
			for (TargetModel target : targetList) {	
				// Items
				icon = new ImageView("/resources/Open_new_window.png");				
				link = new Label(target.getName());
				link.setPadding(new Insets(0, 0, 5, 5));
				link.setPrefWidth(300);
				link.addEventHandler(MouseEvent.MOUSE_ENTERED, onfocus);
				link.addEventHandler(MouseEvent.MOUSE_EXITED, outfocus);
				// A line
				hbox_for_iconNtext = new HBox(icon, link);
				hbox_for_iconNtext.setId(target.getId() + "");
				hbox_for_iconNtext.addEventHandler(MouseEvent.MOUSE_CLICKED, double_left_click);
				// Item section
				vbox_for_target.getChildren().add(hbox_for_iconNtext);
				titledpane_listproject.setContent(vbox_for_target);
			}
			// Add item into list view
			project_name.add(titledpane_listproject);
	        listview_contents.setItems(project_name);
		}
	}
	
	
	/*
	 * [VIEW] Context Menu in list view
	 * ITEMS:
	 * * New project
	 * * New target
	 * * Refresh
	 * * Delete node
	 * * Rename node
	 * * Open
	 * * Edit
	 * * Copy To >
	 * * Move To >
	 * * Copy Text
	 * * Import From File
	 * * Export To File
	 * * Properties
	 */
}


