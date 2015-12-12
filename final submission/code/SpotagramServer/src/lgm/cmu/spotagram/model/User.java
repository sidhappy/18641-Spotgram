package lgm.cmu.spotagram.model;

import lgm.cmu.spotagram.db.Column;
import lgm.cmu.spotagram.db.Table;
import lgm.cmu.spotagram.db.Column.DataType;

/**
 * User.java 	Version <1.00>	ÏÂÎç1:05:31
 *
 * Copyright(C) 2015-2016  All rights reserved. 
 * Lei YU is a graduate student majoring in Electrical and Electronics Engineering, 
 * from the ECE department, Carnegie Mellon University, PA 15213, United States.
 *
 * Email: leiyu@andrew.cmu.edu
 */

@Table(name="user")
public class User extends Model {
	
	@Column(name="username", type =DataType.TEXT)
	private String userName;
	
	@Column(name="password", type =DataType.TEXT)
	private String password;
	
	@Column(name="gender", type =DataType.BOOLEAN)
	private boolean gender;
	
	@Column(name="avatar", type =DataType.TEXT)
	private String avatar;
	
	@Column(name="info", type =DataType.TEXT)
	private String info;
	
	@Column(name="email", type =DataType.TEXT)
	private String email;
	
	@Column(name="image_url", type =DataType.TEXT)
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
