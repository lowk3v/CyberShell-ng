/**
 * 
 */
package fxml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import controllers.ShellController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import models.TargetModel;
import utilities.MyScreen;

/**
 * @author Kevinlpd
 *
 */
public class ShellView {
	
	/*
	 * [INITIALIZE] Create tab session for each shell
	 * PARAMETER:
	 * * title: title of tab
	 * RETURN:
	 * * A object of tab
	 */
	public Tab create_tab_session(String target_id){
		TargetModel target = new TargetModel().getTargetById(target_id);
		// Create tab
		Tab tab_explorer = this.create_tab("Explorer");
		Tab tab_database = this.create_tab("Database");
		TabPane tabpane_section = this.create_tabpane(Arrays.asList(tab_explorer, tab_database));		
		// Session tab
		Tab tab = new Tab(target.getName());
		tab.setClosable(true);	
		tab.setId("session-" + target.getId());
		tab.setContent(tabpane_section);
		// Call controller as threading
		ShellController shell_ctrl = new ShellController(target_id, tab);
		shell_ctrl.start();
		return tab;
	}
	/*
	 * Create new windows manage shell
	 * PARAMETER:
	 * * root: is children of stage
	 * RETURN:
	 * * Stage instance with style
	 */
	public Stage create_stage(AnchorPane root){
		Stage stage = new Stage();
		stage.setTitle("SHELL - Cybershell-ng v2");
		stage.setScene(new Scene(root, 1000, 650));
		stage.setX(new MyScreen().getPosition().get(0) + 310);
		stage.setY(new MyScreen().getPosition().get(1));
		return stage;
	}
	/*
	 * Create tabpane for each session shell
	 * PARAMETERS:
	 * * tab: for add into
	 * RETURN:
	 * * A object of tabpane with children are tabs
	 */
	public TabPane create_tabpane_session(Tab tab){
		TabPane tabpane  = new TabPane(tab);
		tabpane.setSide(Side.TOP);
		tabpane.setPrefWidth(1000);
		tabpane.setPrefHeight(650);
		tabpane.setTabMinWidth(100);
		return tabpane;
	}
	/*
	 * Create tabpane for each feature: explorer, database, ...
	 * PARAMETER:
	 * * tabs: list for add into
	 * RETURN:
	 * * A tabpane object 
	 */
	public TabPane create_tabpane(List<Tab> tabs){
		TabPane tabpane  = new TabPane();
		for (Tab tab : tabs) {
			 tabpane.getTabs().add(tab);			
		}
		tabpane.setSide(Side.LEFT);
		tabpane.setPrefWidth(1000);
		tabpane.setPrefHeight(650);
		tabpane.setTabMinWidth((650 / tabs.size()) - 35);
		return tabpane;
	}
	/*
	 * Create a tab display in tabpane
	 * PARAMETER:
	 * * name: is text display in tab
	 * RETURN:
	 * * A Tab object
	 */
	public Tab create_tab(String name){
		Tab t = new Tab(name);
		return t;
	}
	/*
	 * 
	 */
	public void create_anchorpane(Node node) {
		AnchorPane ap = new AnchorPane(node);
	}
	
	public void create_treeview() {
	}
}
