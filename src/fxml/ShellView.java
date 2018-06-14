/**
 * 
 */
package fxml;

import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.TargetModel;
import utilities.MyScreen;

/**
 * @author Kevinlpd
 *
 */
public class ShellView {
	private Tab tab_session;
	private TreeView tree_view;
	private ImageView loading;
	
	public ShellView() {}
	
	/*
	 * [Shellmanager -> tab_session] -> [sub_tab_pane -> tab_explorer -> anchor pane] -> 
	 * 		[	-> Current path
	 *			-> title
	 *			-> tree_view
	 * 			-> table_view -> cl_name ...
	 * 			-> progress_bar 
	 * 		]
	 */
	public ShellView(TargetModel target, TabPane Shellmanager) {
		// Current path
		TextField current_path = this.create_current_path();
		
		// Title
		Button title = this.create_title();
		
		// Tree view
		this.tree_view = this.create_treeview("linux");	
		
		// Table view
		TableColumn cl_name = this.create_table_column("Name", 200.0, 500.0, 389.0);
		TableColumn cl_date = this.create_table_column("Date Modified", 10.0, 150.0, 150.0);
		TableColumn cl_type = this.create_table_column("Type", 10.0, 90.0, 150.0);
		TableColumn cl_size = this.create_table_column("Size", 10.0, 90.0, 150.0);
		TableView table_view = this.create_table_view(FXCollections.observableArrayList(cl_name, cl_date, cl_type, cl_size));
		
		// Icon loading
		loading = this.create_loading();
		
		// Tab
		AnchorPane anchorpane = this.create_anchorpane(Arrays.asList(loading, current_path, this.tree_view, table_view, title));
		Tab tab_explorer = this.create_tab("Explorer", anchorpane);
		TabPane sub_tab_pane = this.create_tabpane(Arrays.asList(tab_explorer));
				
		// session
		this.tab_session = this.create_tab_session(target.getId()+"", sub_tab_pane);
		Shellmanager.getTabs().add(this.tab_session);
				
	}
	// ========================= ACTIONS ================================ //
	
	public void add_item_to_tree_view(TreeItem parrent, List<String> item_list) {
		for (String item : item_list) {
			this.tree_view.getTreeItem(
				this.tree_view.getTreeItemLevel(parrent)
			).getChildren().add(new TreeItem<String>(item));
		}
	}
	
	
	// ======================== GETTERS & SETTERS ======================= //
	
	public Tab getTab_session() {
		return tab_session;
	}
	public ImageView getLoading() {
		return loading;
	}
	public TreeItem<String> getRoot(){
		return this.tree_view.getRoot();
	}
	
	// ================================ NEW WINDOWS ===================== //

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
	 * Contain by tabs. It contains tree view, table view, ...
	 */
	public AnchorPane create_anchorpane(List<Node> item_list) {
		AnchorPane ap = new AnchorPane();
		for (Node item  : item_list) {
			ap.getChildren().add(item);
		}		
		return ap;
	}
	
	// ======================== TREE VIEW =============================== //
		
	/*
	 * Create view for display all disks, folder tree
	 */
	public TreeView<String> create_treeview(String OS) {
		TreeItem<String> root;
		if (OS.equals("windows")) {
			root = new TreeItem<String>("This PC");
		}else { // Linux
			root = new TreeItem<String>("/");
		}
		root.setExpanded(true);
				
		TreeView tv = new TreeView(root);
		tv.setPrefHeight(623);
		tv.setPrefWidth(252);
		tv.setLayoutY(32);
		tv.setPadding(new Insets(25, 0, 0, 0));
		return tv;
	}
	/*
	 * Display title "Disks"
	 */
	public Button create_title() {
		Button bt = new Button("Disks");
		bt.setStyle("-fx-font-weight: bold");
		bt.setPrefWidth(252);
		bt.setPrefHeight(23);
		bt.setLayoutY(32);
		return bt;
	}
		
	// ======================== TABLE VIEW ============================== //
	
	/*
	 * Create column for table be call in create_table_view();
	 * PARAMETER:
	 * * name: display column name
	 */
	public TableColumn create_table_column(String name, Double min_width, Double max_width, Double pref_width) {
		TableColumn tc = new TableColumn(name);
		tc.setPrefWidth(pref_width);
		tc.setMaxWidth(max_width);
		tc.setMinWidth(min_width);
		return tc;
	}
	/*
	 * Create table for view all files and folders ...
	 * PARAMETERS:
	 * * cols: list of column name
	 */
	public TableView create_table_view(ObservableList<TableColumn> cols) {
		TableView tbv = new TableView();
		tbv.setPrefHeight(622);
		tbv.setPrefWidth(749);
		tbv.setLayoutX(252);
		tbv.setLayoutY(32);
		
		for (TableColumn col : cols) {
			tbv.getColumns().add(col);
		}
		
		return tbv;
	}
		
	// ======================== ICON LOADING ============================ //
	
	/*
	 * Create dynamic image loading
	 */
	public ImageView create_loading() {
		ImageView img = new ImageView("/resources/Loading.gif");
		img.setLayoutY(292);
		img.setLayoutX(112);
		img.setFitWidth(40);
		img.setFitHeight(40);
		return img;
	}
	
	// ======================== TEXT FIELD PATH ========================= //
	
	/*
	 * Field current path in system
	 */
	public TextField create_current_path() {
		TextField tf = new TextField();
		tf.setPromptText("Current Path");
		tf.setPrefWidth(550);
		tf.setLayoutX(211);
		tf.setLayoutY(2);
		return tf;
	}

	// ======================================== //
	

}
