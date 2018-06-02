/**
 * 
 */
package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Kevinlpd
 *
 */
public class ProjectModel extends Model{
	private int id = 0;
	private String name = null;
	private Date created_at = null, updated_at = null;
	
	public boolean addProject(String name){
		PreparedStatement ps = null;
		Connection conn = getConnection();
		String sql = "INSERT INTO Projects(name, created_at, updated_at) VALUES (?, current_timestamp, current_timestamp)";
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.executeUpdate();
			conn.close();
			return true;
		}catch(SQLException e){
			//e.printStackTrace();
			return false;
		}
	}

	public ArrayList<ProjectModel> getList(){
		PreparedStatement ps = null;
		Connection conn = getConnection();
		ArrayList<ProjectModel> projects = new ArrayList<>();
		String sql = "SELECT id, name FROM Projects";
		try{
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				ProjectModel project = new ProjectModel();
				project.setId(rs.getInt("id"));
				project.setName(rs.getString("name"));
				projects.add(project);
			}
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return projects;
	}

	public int getProjectIDbyName(String name){
		PreparedStatement ps = null;
		Connection conn = getConnection();
		String sql = "SELECT id FROM Projects WHERE name = ?";
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int id = rs.getInt("id");
			conn.close();
			return id;
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
	
	/*
	 * Getter & Setter
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
}
