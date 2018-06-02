/**
 * 
 */
package models;

import models.Usermodel;

/**
 * @author Kevinlpd
 *
 */
public class User{
	private int id = 0;
	private boolean isLogin = false;
	private String username = null, 
					password = null, 
					email = null;

	/*
	 * Inittial ORM Usermodel object
	 * RETURN:
	 * * Usermodel object
	 */
	private static Usermodel model(){
		return new Usermodel();
	}
	/*
	 * Create user when the first start app.
	 * PARAMETER:
	 * * username: for login and reset password
	 * * password : was hashed
	 * * email: for reset password
	 * RETURN:
	 * * True if add user successful
	 * * False if add user fail
	 */
	public boolean addUser(String username, String password, String email){
		return User.model().addUser(username, password, email);
	}
	/*
	 * Action check login into app
	 * PARAMETER:
	 * * username is unique account name of user
	 * * password was hashed before call this function
	 * RETURN:
	 * * True if username and password is correct
	 * * False if username or password incorrect
	 */
	public boolean login(String username, String password){
		User u = User.model().login(username, password);
		this.id = u.getId();
		this.username = u.getUsername();
		this.password = u.getPassword();
		this.email = u.getEmail();
		this.isLogin = u.isLogin;
		return u.isLogin;
	}
	/*
	 * Remember user's info on login form
	 * PARAMETER:
	 * * id: is user's id
	 */
	public boolean setRemember(int id){
		User.model().setRemember(id);
		return true;
	}
	/*
	 * 
	 */
	public boolean removeRemember(int id){
		User.model().removeRemember(id);
		return true;
	}
	/*
	 * 
	 */
	public String getRemember(){
		return User.model().getRemember();
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
	public boolean resetPassword(String email, String newpass){
		return User.model().resetPassword(email, newpass);
	}
	/*
	 * Getter and setter
	*/
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
}
