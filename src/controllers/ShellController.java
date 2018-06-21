/**
 * 
 */
package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import fxml.ShellView;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import models.BinaryModel;
import models.TargetModel;
import utilities.CryptoUtils;
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
		this.isLive();
		String ls = new BinaryModel().get_binary("list_dir");
		
//		List disks = Arrays.asList("C", "D", "E", "F");
//		this.view.getLoading().setVisible(false);
//		this.view.add_item_to_tree_view(this.view.getRoot(), disks);
	}
	
	//http://www.baeldung.com/httpclient-post-http-request
	private String sendPost(String payload, String arg) {
		try {
			HttpResponse response = 
					Request.Post(this.target.getUrl())
							.bodyForm(Form.form()
								.add("_", payload)
								.add("__", arg)
								.build())
							.execute().returnResponse();
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
			 
			    
	}
	/*
	 * Send HTTP request using method GET
	 * RETURN: 
	 * * HttpURLConnnection
	 */
	private HttpURLConnection sendGet() {
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
			return con;
		} catch (MalformedURLException e) {
			new PopupUtils(AlertType.ERROR, "Url is wrong");
			this.exit();
			return con;
		} catch (IOException e) {
			new PopupUtils(AlertType.ERROR, "Request time out");
			this.exit();
			return con;
		}		
	}
	/*
	 * Happeneds to be the current thread, This will temination it
	 */
	private void exit() {
		Thread.currentThread().interrupt();
		Platform.runLater(()->{
			this.view.getTab_session().getTabPane().getTabs().remove(this.view.getTab_session());
		});
	}
	/*
	 * Check shell is live
	 * If false, alert notification and exit this tab
	 */
	private void isLive() {
		try {
			if (this.sendGet().getResponseCode() != 200) {
				new PopupUtils(AlertType.ERROR, sendGet().getResponseCode() + "");
				this.exit();
			}else {				
				Platform.runLater(()->{
					HBox hb = (HBox)this.view.getTab_session().getGraphic();
					hb.getChildren().remove(0);
					hb.getChildren().add(0, this.view.get_icon_living());
				});
			}
		} catch (IOException e) {}
	}
}
