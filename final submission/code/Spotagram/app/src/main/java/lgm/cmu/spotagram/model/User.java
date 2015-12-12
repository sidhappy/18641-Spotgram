package lgm.cmu.spotagram.model;


/**
 * User.java 	Version <1.00>	����1:05:31
 *
 * Copyright(C) 2015-2016  All rights reserved. 
 * Lei YU is a graduate student majoring in Electrical and Electronics Engineering, 
 * from the ECE department, Carnegie Mellon University, PA 15213, United States.
 *
 * Email: leiyu@andrew.cmu.edu
 */

public class User extends Model {
	
	private String userName;
	
	private String password;
	
	private boolean gender;
	
	private String avatar;
	
	private String info;
	
	private String email;
	
	private String imageURL;
	
	public User() {
		// TODO Auto-generated constructor stub
		super();
	}

	public User(String userName, String password, boolean gender,
			String avatar, String info, String email, String imageURL) {
		super();
		this.userName = userName;
		this.password = password;
		this.gender = gender;
		this.avatar = avatar;
		this.info = info;
		this.email = email;
		this.imageURL = imageURL;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean getGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	

}
