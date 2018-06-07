/**
 * 
 */
package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import models.TargetModel;
import utilities.PopupUtils;

/**
 * @author Kevinlpd
 *
 */
public class ShellController extends Thread{
	private TargetModel target;

	public ShellController(String id){
		this.target = new TargetModel().getTargetById(id);
	}
	
	@Override
	public void run() {
		sendGet();
	}
	

	public void sendPost() {
		
	}
	
	public void sendGet() {
		URL url;
		try {
			url = new URL(this.target.getUrl());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			System.out.print(con.getResponseCode());
			con.disconnect();
		} catch (MalformedURLException e) {
			System.out.print("URL is wrong");
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.print("Connect timed out");
		}
		
	}
	
}
