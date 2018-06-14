/**
 * 
 */
package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import fxml.ShellView;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane;
import models.TargetModel;
import utilities.PopupUtils;

/**
 * @author Kevinlpd
 *
 */
public class ShellController extends Thread{
	private TargetModel target;
	private ShellView view;

	
	public ShellController(String id, TabPane Shellmanager){		
		this.target = new TargetModel().getTargetById(id);
		this.view = new ShellView(target, Shellmanager);
	}
	
	@Override
	public void run() {

		//sendGet();
		List disks = Arrays.asList("C", "D", "E", "F");
		this.view.getLoading().setVisible(false);
		this.view.add_item_to_tree_view(this.view.getRoot(), disks);
		
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
			this.view.getTab_session().getTabPane().getTabs().remove(this.view.getTab_session());
		});
	}
	
}
