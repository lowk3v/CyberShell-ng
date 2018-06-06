/**
 * 
 */
package fxml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import controllers.ShellController;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.TargetModel;

/**
 * @author Kevinlpd
 *
 */
public class ShellView {
	/*
	 * 
	 */
	public Stage create_stage(AnchorPane root){
		Stage stage = new Stage();
		stage.setTitle("SHELL - Cybershell-ng v2");
		stage.setScene(new Scene(root, 1000, 650));
		return stage;
	}
	/*
	 * [VIEW] Create tab with style
	 * PARAMETER:
	 * * title: title of tab
	 * RETURN:
	 * * A object of tab
	 */
	public Tab create_tab_session(TargetModel target){
		// Create tab
		Tab tab_explorer = this.create_tab("Explorer");
		Tab tab_database = this.create_tab("Database");
		TabPane tabpane_section = this.create_tabpane(Arrays.asList(tab_explorer, tab_database));		
		// Session tab
		Tab tab = new Tab(target.getName());
		tab.setClosable(true);	
		tab.setId("session-" + target.getId());
		tab.setContent(tabpane_section);
		return tab;
	}
	/*
	 * [VIEW] Create tabpane with style
	 * PARAMETERS:
	 * * tab: tab list for add into
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
	 * 
	 */
	public TabPane create_tabpane(List<Tab> tabs){
		TabPane tabpane  = new TabPane();
		for (Tab tab : tabs) {
			 tabpane.getTabs().add(tab);			
		}
		tabpane.setSide(Side.LEFT);
		tabpane.setPrefWidth(1000);
		tabpane.setPrefHeight(650);
		return tabpane;
	}
	/*
	 * 
	 */
	public Tab create_tab(String name){
		Tab t = new Tab(name);
		return t;
	}
}
