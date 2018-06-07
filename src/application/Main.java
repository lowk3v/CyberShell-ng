package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import utilities.MyScreen;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/HomeScene.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Login - CyberShell-ng 2.0");	
			primaryStage.setScene(scene);
			//primaryStage.setResizable(false);
			primaryStage.setX(new MyScreen().getPosition().get(0));
			primaryStage.setY(new MyScreen().getPosition().get(1));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
