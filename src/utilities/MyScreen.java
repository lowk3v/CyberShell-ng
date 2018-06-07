/**
 * 
 */
package utilities;

import java.util.Arrays;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;

/**
 * @author kevinlpd
 *
 */
public class MyScreen {
	
	public List<Double> getPosition() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		return Arrays.asList(
				primaryScreenBounds.getMinX() + 10,
				primaryScreenBounds.getMinY() + 100);
		
	}
}
