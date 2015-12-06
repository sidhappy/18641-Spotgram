package lgm.cmu.spotagram.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import lgm.cmu.spotagram.db.DBFacade;
import lgm.cmu.spotagram.model.Model;
import lgm.cmu.spotagram.model.User;
import lgm.cmu.spotagram.utils.ConstantValue;

/**
 * LogInServlet.java 	Version <1.00>	ÏÂÎç2:32:22
 *
 * Copyright(C) 2015-2016  All rights reserved. 
 * Lei YU is a graduate student majoring in Electrical and Electronics Engineering, 
 * from the ECE department, Carnegie Mellon University, PA 15213, United States.
 *
 * Email: leiyu@andrew.cmu.edu
 */
@WebServlet("/RegisterServlet")
public class RegistrationServlet extends HttpServlet {
	
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
		String user = req.getParameter(ConstantValue.KEY_USERNAME);
		String email = req.getParameter(ConstantValue.KEY_EMAIL);
		String pwd = req.getParameter(ConstantValue.KEY_PWD);
		boolean gender = Boolean.parseBoolean(req.getParameter(ConstantValue.KEY_GENDER));
		String info = req.getParameter(req.getParameter(ConstantValue.KEY_INFO));
		
		JSONObject jsonObject = new JSONObject();
		
		// check whether the username and email exist in the database
		List<Model> users = null, emails = null;
		if (user != null) {
			users = DBFacade.findByFieldName(User.class, "userName", user);
		}
		
		if (email != null){
			emails = DBFacade.findByFieldName(User.class, "email", email);
		}
		
		if (users != null && users.size() != 0) {
			jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_USER_ERR);
		} else if (emails != null && emails.size() != 0) {
			jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_EMAIL_ERR);
		} else {
			User modelUser = new User(user, pwd, gender, "", info, email, "");
			if (DBFacade.save(modelUser)) {
				int user_id = modelUser.getId();
				jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_OK);
				jsonObject.put(ConstantValue.KEY_USER_ID, user_id);
			} else {
				jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_ERR);
			}
		}
		
		jsonObject.writeJSONString(resp.getWriter());
	}

}
