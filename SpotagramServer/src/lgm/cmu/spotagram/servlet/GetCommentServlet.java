package lgm.cmu.spotagram.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet("/GetCommentsServlet")
public class GetCommentServlet extends HttpServlet {

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
		int note_id = Integer.parseInt(req
				.getParameter(ConstantValue.KEY_NOTE_ID));

		List<Model> models = DBFacade.findByFieldName(Comment.class, "noteid", note_id);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_OK);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < models.size(); i++) {
			Comment comment = (Comment) models.get(i);
			JSONObject noteObject = new JSONObject();
			noteObject.put(ConstantValue.JSON_COMMENT_ID, comment.getId());
			noteObject.put(ConstantValue.JSON_USER_NAME, comment.getUsername());
			noteObject.put(ConstantValue.JSON_USER_ID, comment.getUserid());
			noteObject.put(ConstantValue.JSON_CONTENT, comment.getContent());
			noteObject.put(ConstantValue.JSON_DATE, comment.getDate());

			jsonArray.add(noteObject);
		}

		jsonObject.put(ConstantValue.KEY_COMMENT_LIST, jsonArray.toString());

		PrintWriter out = resp.getWriter();
		out.println(jsonObject.toString());
	}

}
