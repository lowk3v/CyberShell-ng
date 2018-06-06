/**
 * 
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Kevinlpd
 *
 */
public class TargetModel extends Model{
	private int id, project_id;
	private String name, password, db_connection, link, description;
	
	/*
	 * [INSERT] insert target in to Targets table
	 * PARAMETERS:
	 * * name: is alias of shell
	 * * link: url of shell (http://, https://)
	 * * password: password for authen and en/decrypt payload on shell
	 * * description: of target, default is null
	 * * project_id: project contain this shell
	 * * db_connection: connection string of SQL Server
	 * * dbserver: ip/host of sql like mysql, ...
	 * * dbuser: username for login to sql like mysql, ...
	 * * dbpass: password for login to sql like mysql, ...
	 * RETURN:
	 * * True if success
	 * * Otherwise, false
	 */
	public boolean addTarget(String name, String link, String password, String description, int project_id,
							String db_connection, String dbserver, String dbuser, String dbpass){
		if (! db_connection.isEmpty()){
			dbserver = dbuser = dbpass = "";
		}else{
			db_connection = "";
		}
		PreparedStatement ps = null;
		Connection conn = getConnection();
		String sql = "INSERT INTO Targets(name, link, password, db_connection, description, project_id, created_at, updated_at) "
					+ "VALUES (?, ?, ?, ?, ?, ?, current_timestamp, current_timestamp)";
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, link);
			ps.setString(3, password);
			ps.setString(4, db_connection);
			ps.setString(5, description);
			ps.setInt(6, project_id);
			ps.executeUpdate();
			conn.close();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	/*
	 * Get target's list in database
	 * PARAMETER
	 * * id: project's id
	 * RETURN
	 * * Arraylist of object targetmodel
	 */
	public ArrayList<TargetModel> getList(int id){
		PreparedStatement ps = null;
		Connection conn = getConnection();
		ArrayList<TargetModel> list = new ArrayList<>();
		String sql = "SELECT id, name FROM Targets WHERE project_id = ?";
		try{
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				TargetModel target = new TargetModel();
				target.setName(rs.getString("name"));
				target.setId(rs.getInt("id"));
				list.add(target);
			}
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public TargetModel getTargetById(String id){
		PreparedStatement ps = null;
		Connection conn = getConnection();
		TargetModel tm = new TargetModel();
		String sql = "SELECT * FROM Targets WHERE id = ?";
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			tm.setId(rs.getInt("id"));
			tm.setDb_connection(rs.getString("db_connection"));
			tm.setDescription(rs.getString("description"));
			tm.setLink(rs.getString("link"));
			tm.setName(rs.getString("name"));
			tm.setPassword(rs.getString("password"));
			tm.setProject_id(rs.getInt("project_id"));
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return tm;
	}
	
	/*
	 * Getters & Setters
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDb_connection() {
		return db_connection;
	}
	public void setDb_connection(String db_connection) {
		this.db_connection = db_connection;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
