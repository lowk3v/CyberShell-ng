/**
 * 
 */
package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane.TabClosingPolicy;
import models.TargetModel;
import utilities.PopupUtils;

/**
 * @author Kevinlpd
 *
 */
public class ShellController extends Thread{
	private TargetModel target;
	private Tab tab_session;

	public ShellController(String id, Tab tab_session){
		this.target = new TargetModel().getTargetById(id);
		this.tab_session = tab_session;
	}
	
	@Override
	public void run() {
		sendGet();
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
		tab_session.getTabPane().getTabs().remove(tab_session);
	}
	
}
