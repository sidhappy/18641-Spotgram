package lgm.cmu.spotagram.model;

import java.util.Date;

/**
 * User.java 	Version <1.00>	����1:05:31
 *
 * Copyright(C) 2015-2016  All rights reserved. 
 * Lei YU is a graduate student majoring in Electrical and Electronics Engineering, 
 * from the ECE department, Carnegie Mellon University, PA 15213, United States.
 *
 * Email: leiyu@andrew.cmu.edu
 */

public class Note extends Model {
	
	private float longitude;
	private float latitude;
	private Date date;
	private String content;
	private int type;
	private int userid;
	private String username;
	private String userURL;
	private String noteImageURL;
	private String info;
	
	public Note() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public Note(float longitude, float latitude, Date date,
			String content, int type, int userid, String userName, String info, String usrURL, String noteURL) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.date = date;
		this.content = content;
		this.type = type;
		this.userid = userid;
		this.info = info;
		this.username = userName;
		this.userURL = usrURL;
        this.noteImageURL = noteURL;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
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

	public String getUserURL() {
		return userURL;
	}

	public void setUserURL(String url) {
		this.userURL = url;
	}

    public String getNoteImageURL() {
        return noteImageURL;
    }

    public void setNoteImageURL(String url) {
        this.noteImageURL = url;
    }
}
