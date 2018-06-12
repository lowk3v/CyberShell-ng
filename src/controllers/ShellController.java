/**
 * 
 */
package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import fxml.ShellView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import models.TargetModel;
import utilities.PopupUtils;

/**
 * @author Kevinlpd
 *
 */
public class ShellController extends Thread{
	private TargetModel target;
	private ShellView view;
	private TableView table_view;
	private ImageView loading;
	private TreeView<String> tree_view;
	private Tab tab_session;

	/*
	 * [Shellmanager -> tab_session] -> [sub_tab_pane -> tab_explorer -> anchor pane] -> [ tree_view
	 * 																					-> table_view -> cl_name ...
	 * 																					-> progress_bar ]
	 */
	public ShellController(String id, TabPane Shellmanager){		
		this.target = new TargetModel().getTargetById(id);
		this.view = new ShellView();
		// Load view
		
		// Tree view
		this.tree_view = view.create_treeview();
		
		// Table view
		TableColumn cl_name = view.create_table_column("Name");
		TableColumn cl_date = view.create_table_column("Date Modified");
		TableColumn cl_type = view.create_table_column("Type");
		TableColumn cl_size = view.create_table_column("Size");
		this.table_view = view.create_table_view(FXCollections.observableArrayList(cl_name, cl_date, cl_type, cl_size));
		
		// Icon loading
		this.loading = view.create_loading();
		
		// Tab
		AnchorPane anchorpane = view.create_anchorpane(Arrays.asList(tree_view, this.loading, table_view));
		Tab tab_explorer = view.create_tab("Explorer", anchorpane);
		TabPane sub_tab_pane = view.create_tabpane(Arrays.asList(tab_explorer));
		

		
		// session
		this.tab_session = view.create_tab_session(id, sub_tab_pane);
		Shellmanager.getTabs().add(this.tab_session);
		
	}
	
	@Override
	public void run() {
		sendGet();
		System.out.print(123123);
		
	}
	

	public void sendPost() {
		
	}
	/*
	 * Send HTTP request using method GET
	 * RETURN: 
	 * * HttpURLConnnection
	 */
	public HttpURLConnection sendGet() {
		URL url;
		HttpURLConnection con = null;
		try {
			url = new URL(this.target.getUrl());
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(30000);
			con.setReadTimeout(10000);
			con.connect();		
			/*
			 * 
			 */
			con.disconnect();
		} catch (MalformedURLException e) {
			new PopupUtils(AlertType.ERROR, "Url is wrong");
			this.exit();
		} catch (IOException e) {
			new PopupUtils(AlertType.ERROR, "Request time out");
			this.exit();
		}
		return con;
		
	}
	/*
	 * Happeneds to be the current thread, This will temination it
	 */
	public void exit() {
		Thread.currentThread().interrupt();
		Platform.runLater(()->{
			this.tab_session.getTabPane().getTabs().remove(tab_session);
		});
	}
	
}
