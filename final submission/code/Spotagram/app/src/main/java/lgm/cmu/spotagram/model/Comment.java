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


public class Comment extends Model {
	
	private Date date;
	private String content;
	private int userid;
	private String username;
	private int noteid;
	
	public Comment() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public Comment(Date date, String content, int userid, String username,
			int noteid) {
		super();
		this.date = date;
		this.content = content;
		this.userid = userid;
		this.noteid = noteid;
		this.username = username;
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

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getNoteid() {
		return noteid;
	}

	public void setNoteid(int noteid) {
		this.noteid = noteid;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
