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

import com.sun.corba.se.impl.orbutil.closure.Constant;

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
@WebServlet("/LoginServlet")
public class LogInServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doGet(req, resp);
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doPost(req, resp);
		String user = req.getParameter(ConstantValue.KEY_USERNAME);
		String email = req.getParameter(ConstantValue.KEY_EMAIL);
		String pwd = req.getParameter(ConstantValue.KEY_PWD);
		
		JSONObject jsonObject = new JSONObject();
		
		if (user == null && email == null) {
			jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_ERR);
		} else {
			User modelUser = null;
			List<Model> models = null;
			if (user != null) {
				models = DBFacade.findByFieldName(User.class, "userName", user);	
			} else if (email != null) {
				models = DBFacade.findByFieldName(User.class, "email", email);
			}
	
			if (models != null && models.size() >= 1) {
				modelUser = (User)models.get(0);
			}
			
			if(modelUser == null) {
				jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_USER_ERR);
			} else if (modelUser.getPassword().equals(pwd)) {
				jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_OK);
				jsonObject.put(ConstantValue.KEY_USER_ID, modelUser.getId());
				jsonObject.put(ConstantValue.KEY_USERNAME, modelUser.getUserName());
			} else {
				jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_PWD_ERR);
			}
					
		}
		
		
		jsonObject.writeJSONString(resp.getWriter());
	}

}
