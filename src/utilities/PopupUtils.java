/**
 * 
 */
package utilities;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;

/**
 * @author kevinlpd
 *
 */
public class PopupUtils {
	
	private Alert alert;
	private boolean isClose = false;
	private AlertType type;
	
	public PopupUtils(AlertType type, String message) {
		this.type = type;
				
		Platform.runLater(()->{
			alert = new Alert(type);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.setTitle("Popup");
			alert.show();
		});
	}
}
