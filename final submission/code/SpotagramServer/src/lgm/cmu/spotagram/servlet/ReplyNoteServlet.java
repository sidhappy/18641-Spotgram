package lgm.cmu.spotagram.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import lgm.cmu.spotagram.db.DBFacade;
import lgm.cmu.spotagram.model.Comment;
import lgm.cmu.spotagram.model.Model;
import lgm.cmu.spotagram.model.Note;
import lgm.cmu.spotagram.model.User;
import lgm.cmu.spotagram.utils.ConstantValue;

/**
 * LogInServlet.java Version <1.00> ÏÂÎç2:32:22
 *
 * Copyright(C) 2015-2016 All rights reserved. Lei YU is a graduate student
 * majoring in Electrical and Electronics Engineering, from the ECE department,
 * Carnegie Mellon University, PA 15213, United States.
 *
 * Email: leiyu@andrew.cmu.edu
 */
@WebServlet("/ReplyServlet")
public class ReplyNoteServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int userid = Integer.parseInt(req.getParameter(ConstantValue.KEY_USER_ID));
		int noteid = Integer.parseInt(req.getParameter(ConstantValue.KEY_NOTE_ID));
		String content = req.getParameter(ConstantValue.KEY_CONTENT);
		String username = req.getParameter(ConstantValue.KEY_USERNAME);
		Timestamp date = new Timestamp(System.currentTimeMillis());
		
		JSONObject jsonObject = new JSONObject();
		
		Comment comment = new Comment(date, content, userid, username, noteid);
		if (DBFacade.save(comment)) {
			int note_id = comment.getId();
			jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_OK);
			jsonObject.put(ConstantValue.KEY_COMMENT_ID, note_id);
		} else {
			jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_ERR);
		}

		PrintWriter out = resp.getWriter();
		out.println(jsonObject.toString());
	}

}
