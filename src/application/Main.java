package application;
	
import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.ScreenUtils;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/HomeScene.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("CyberShell-ng 2.0");	
			primaryStage.setScene(scene);
			//primaryStage.setResizable(false);
			primaryStage.setX(new ScreenUtils().getPosition().get(0));
			primaryStage.setY(new ScreenUtils().getPosition().get(1));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
