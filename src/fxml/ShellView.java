/**
 * 
 */
package fxml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import controllers.ShellController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
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
	 * Create tab pane for each session shell
	 * PARAMETERS:
	 * * tab: for add into
	 * RETURN:
	 * * A object of tab pane with children are tabs
	 */
	public TabPane create_tabpane_session(){
		TabPane tabpane  = new TabPane();
		tabpane.setSide(Side.TOP);
		tabpane.setPrefWidth(1000);
		tabpane.setPrefHeight(650);
		tabpane.setTabMinWidth(100);
		return tabpane;
	}	
	/*
	 * Create tab session for each shell
	 * PARAMETER:
	 * * title: title of tab
	 * RETURN:
	 * * A object of tab
	 */
	public Tab create_tab_session(String target_id, TabPane tp){
		TargetModel target = new TargetModel().getTargetById(target_id);
		Tab tab = new Tab(target.getName());
		tab.setClosable(true);	
		tab.setId("session-" + target.getId());
		tab.setContent(tp);
		return tab;
	}
	/*
	 * Create sub-tab pane for each feature: explorer, database, ...
	 * PARAMETER:
	 * * tabs: list for add into
	 * RETURN:
	 * * A tab pane object 
	 */
	public TabPane create_tabpane(List<Tab> tabs){
		TabPane tabpane  = new TabPane();
		for (Tab tab : tabs) {
			 tabpane.getTabs().add(tab);			
		}
		tabpane.setSide(Side.LEFT);
		tabpane.setPrefWidth(1000);
		tabpane.setPrefHeight(650);
		tabpane.setTabMinWidth((650 / tabs.size()));
		return tabpane;
	}
	/*
	 * Create a sub-tab display in tab pane session
	 * PARAMETER:
	 * * name: is text display in tab
	 * RETURN:
	 * * A Tab object
	 */
	public Tab create_tab(String name, AnchorPane ap){
		Tab t = new Tab(name);
		t.setClosable(false);
		t.setContent(ap);
		return t;
	}
	/*
	 * 
	 */
	public AnchorPane create_anchorpane(List<Node> item_list) {
		AnchorPane ap = new AnchorPane();
		for (Node item  : item_list) {
			ap.getChildren().add(item);
		}		
		return ap;
	}
	/*
	 * 
	 */
	public TreeView<String> create_treeview() {
		TreeView<String> tv = new TreeView<>();
		tv.setPrefHeight(623);
		tv.setPrefWidth(252);
		return tv;
	}
	/*
	 * 
	 */
	public TableColumn create_table_column(String name) {
		TableColumn tc = new TableColumn(name);
		return tc;
	}
	/*
	 * 
	 */
	public TableView create_table_view(ObservableList<TableColumn> cols) {
		TableView tbv = new TableView();
		tbv.setPrefHeight(622);
		tbv.setPrefWidth(749);
		tbv.setLayoutX(252);
		
		for (TableColumn col : cols) {
			tbv.getColumns().add(col);
		}
		
		return tbv;
	}
	
	public ImageView create_loading() {
		ImageView img = new ImageView("/resources/Loading.gif");
		img.setLayoutY(292);
		img.setLayoutX(112);
		img.setFitWidth(40);
		img.setFitHeight(40);
		return img;
	}




}
