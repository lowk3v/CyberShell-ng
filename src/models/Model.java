/**
 * 
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Kevinlpd
 *
 */
public abstract class Model {	
	
	public Connection getConnection(){
		try {
			return DriverManager.getConnection("jdbc:sqlite:cybershell.sqlite");
        } catch (SQLException e) { 
        	return null;
        }
	}
}
