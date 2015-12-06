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
@WebServlet("/ChangePwdServlet")
public class ChangePwdServlet extends HttpServlet {

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
		int userId = Integer.parseInt(req
				.getParameter(ConstantValue.KEY_USER_ID));
		String oldPwd = req.getParameter(ConstantValue.KEY_PWD);
		String newPwd = req.getParameter(ConstantValue.KEY_NEW_PWD);
		
		JSONObject jsonObject = new JSONObject();
		User user = (User)DBFacade.findById(userId, User.class);
		if (user == null || !user.getPassword().equals(oldPwd) || newPwd == null) {
			jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_ERR);
		} else {
			user.setPassword(newPwd);
			if (DBFacade.update(user)) {
				jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_OK);
			} else {
				jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_ERR);
			}
		}
		

		jsonObject.writeJSONString(resp.getWriter());
	}

}
