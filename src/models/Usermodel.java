/**
 * 
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Kevinlpd
 *
 */
public class Usermodel extends Model{
	
	/*
	 * Create user when the first start app.
	 * PARAMETER:
	 * * username: for login and reset password
	 * * password : for login
	 * * email: for reset password
	 * RETURN:
	 * * True if add user successful
	 * * False if add user fail
	 */
	public Boolean addUser(String username, String password, String email){
		PreparedStatement ps = null;
		Connection conn = getConnection();
		String sql = "INSERT INTO Users(username, password, email) VALUES(?, ?, ?);";
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, email);
			ps.executeUpdate();
			conn.close();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	/*
	 * Check login into app
	 * PARAMETER:
	 * * username is unique account name of user
	 * * password hashed before call this function
	 * RETURN:
	 * * True if username and password is correct
	 * * False if username or password incorrect
	 */
	public User login(String username, String password){
		PreparedStatement ps = null;
		Connection conn = getConnection();
		User u = new User();
		String sql = "SELECT * FROM Users WHERE username = ? AND password = ?;";
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				u.setId(rs.getInt("id"));
				u.setUsername(rs.getString("username"));
				u.setPassword(rs.getString("password"));
				u.setEmail(rs.getString("email"));
				u.setLogin(true);
			}
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return u;
	}
	/*
	 * Remember user's info on login form
	 * PARAMETER:
	 * * id: is user's id
	 */
	public void setRemember(int id){
		PreparedStatement ps = null;
		Connection conn = getConnection();
		String sql = "UPDATE Users SET isRemember = 1 WHERE id = ?";
		try{
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/*
	 * 
	 */
	public void removeRemember(int id){
		PreparedStatement ps = null;
		Connection conn = getConnection();
		String sql = "UPDATE Users SET isRemember = 0 WHERE id = ?";
		try{
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/*
	 * 
	 */
	public String getRemember(){
		Connection conn = getConnection();
		String sql = "SELECT username FROM Users WHERE isRemember = 1";
		try{
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getString("username");
			}
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return "";
	}
	/*
	 * Reset password
	 * PARAMETER
	 * * email: send new password to this email
	 * * newpass: is random string
	 * RETURN
	 * * True if update database is successfult
	 * * False if update fail
	 */
	public Boolean resetPassword(String email, String newpass){
		PreparedStatement ps = null;
		Connection conn = getConnection();
		String sql = "UPDATE Users SET password = ? WHERE email = ?";
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, newpass);
			ps.setString(2, email);
			ps.executeUpdate();
			conn.close();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
}
