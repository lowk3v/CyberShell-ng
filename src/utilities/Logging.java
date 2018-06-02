/**
 * 
 */
package utilities;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Kevinlpd
 *
 */
public class Logging {
	private Logger logger;
	
	public Logging(){
		this.logger = Logger.getLogger("ShellLog");
	    SimpleFormatter formatter = new SimpleFormatter();
	    FileHandler fh;  

	    try { 
	        fh = new FileHandler("src/logs/error.log"); 
	        fh.setFormatter(formatter); 
	        logger.addHandler(fh);  
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }   
	}
	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
