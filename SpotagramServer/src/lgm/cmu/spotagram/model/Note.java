package lgm.cmu.spotagram.model;

import java.sql.Date;
import java.sql.Timestamp;

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

@Table(name="note")
public class Note extends Model {
	
	@Column(name="longitude", type =DataType.FLOAT)
	private float longitude;
	
	@Column(name="latitude", type =DataType.FLOAT)
	private float latitude;
	
	@Column(name="date", type =DataType.DATETIME)
	private Timestamp date;
	
	@Column(name="content", type =DataType.TEXT)
	private String content;
	
	@Column(name="type", type =DataType.INTEGER)
	private int type;
	
	@Column(name="userid", type =DataType.INTEGER)
	private int userid;

	@Column(name="username", type =DataType.TEXT)
	private String username;
	
	@Column(name="info", type =DataType.TEXT)
	private String info;
	
	@Column(name="image_url", type =DataType.TEXT)
	private String imageURL;
	
	public Note() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public Note(float longitude, float latitude, Timestamp date,
			String content, int type, int userid, String userName, String info, String imageURL) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.date = date;
		this.content = content;
		this.type = type;
		this.userid = userid;
		this.info = info;
		this.username = userName;
		this.imageURL = imageURL;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
}
