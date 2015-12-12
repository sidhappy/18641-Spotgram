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
@WebServlet("/PostNoteServlet")
public class PostNoteServlet extends HttpServlet {

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
		float loc_long = Float.parseFloat(req
				.getParameter(ConstantValue.KEY_LOC_LONGITUDE));
		float loc_lat = Float.parseFloat(req
				.getParameter(ConstantValue.KEY_LOC_LATITUDE));
		int userid = Integer.parseInt(req.getParameter(ConstantValue.KEY_USER_ID));
		String userName = req.getParameter(ConstantValue.KEY_USERNAME);
		String content = req.getParameter(ConstantValue.KEY_CONTENT);
		int type = Integer.parseInt(req.getParameter(ConstantValue.KEY_TYPE));
		Timestamp date = new Timestamp(System.currentTimeMillis());
		
		JSONObject jsonObject = new JSONObject();
		
		Note note = new Note(loc_long, loc_lat, date, content, type, userid, userName, "", "");
		if (DBFacade.save(note)) {
			int note_id = note.getId();
			jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_OK);
			jsonObject.put(ConstantValue.KEY_NOTE_ID, note_id);
		} else {
			jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_ERR);
		}

		jsonObject.writeJSONString(resp.getWriter());
	}

}
