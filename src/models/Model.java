/**
 * 
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	/* Get all column
	 * USAGE:
	 * * table = 'targets'
	 * * where = [
	 * 			['id', '>'. '1'],
	 * 			['name', '<>', 'test']
	 * 		]
	 */
	public List<Map<String, Object>> select_all(String table, List<List<String>> where) {
		Map<String, Object> row;
		List<Map<String, Object>> result = new ArrayList<>();
		Connection con = this.getConnection();
		
		// create query string
		String sql = "SELECT * FROM " + table + " WHERE 1=1 AND ";
		for (List<String> cond : where) {
			sql += String.join(" ", cond) + " AND ";
		}
		sql += "1=1";
				
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = stmt.getMetaData();
			while (rs.next()) {
				row = new HashMap<>();
				for (int i=1; i<=meta.getColumnCount(); i++){
					switch(meta.getColumnTypeName(i)) {
						case "INTEGER":
							row.put(meta.getColumnName(i), rs.getInt(i));
							break;
						case "VARCHAR":
						case "NVARCHAR":
						case "TEXT":
						case "STRING":
							row.put(meta.getColumnName(i), rs.getString(i));
							break;
					}
				}
				result.add(row);
			}			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Map<String, Object> select_one(String table, List<List<String>> where) {
		Map<String, Object> result = new HashMap<>();
		Connection con = this.getConnection();
		
		// create query string
		String sql = "SELECT * FROM " + table + " WHERE 1=1 AND ";
		for (List<String> cond : where) {
			sql += String.join(" ", cond) + " AND ";
		}
		sql += "1=1";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = stmt.getMetaData();
			
			for (int i=1; i<=meta.getColumnCount(); i++){
				switch(meta.getColumnTypeName(i)) {
					case "INTEGER":
						result.put(meta.getColumnName(i), rs.getInt(i));
						break;
					case "VARCHAR":
					case "NVARCHAR":
					case "TEXT":
					case "STRING":
						result.put(meta.getColumnName(i), rs.getString(i));
						break;
				}
			}					
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
