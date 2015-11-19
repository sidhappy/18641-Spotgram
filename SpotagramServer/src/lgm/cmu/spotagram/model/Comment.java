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

@Table(name="comment")
public class Comment extends Model {
	
	@Column(name="date", type =DataType.DATETIME)
	private Timestamp date;
	
	@Column(name="content", type =DataType.TEXT)
	private String content;
	
	
	@Column(name="userid", type =DataType.INTEGER)
	private int userid;

	@Column(name="username", type =DataType.TEXT)
	private String username;
	
	@Column(name="noteid", type =DataType.INTEGER)
	private int noteid;
	
	public Comment() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public Comment(Timestamp date, String content, int userid, String username,
			int noteid) {
		super();
		this.date = date;
		this.content = content;
		this.userid = userid;
		this.noteid = noteid;
		this.username = username;
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
