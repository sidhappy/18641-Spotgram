package lgm.cmu.spotagram.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
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
@WebServlet("/GetNotesServlet")
public class GetLocationNotesServlet extends HttpServlet {

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
		int radius = (int)Float.parseFloat(req
				.getParameter(ConstantValue.KEY_RADIUS_KM));
		int maxNotes = (int)Float.parseFloat(req
				.getParameter(ConstantValue.KEY_MAX_NOTE));
		int startNotes = (int)Float.parseFloat(req
				.getParameter(ConstantValue.KEY_START_NOTE));

		List<Model> models = DBFacade
				.findNoteByRange(loc_long, loc_lat, radius);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_OK);
		JSONArray jsonArray = new JSONArray();
		for (int i = startNotes; i < Math.min(models.size(), startNotes
				+ maxNotes); i++) {
			Note note = (Note) models.get(i);
			JSONObject noteObject = new JSONObject();
			noteObject.put(ConstantValue.JSON_NOTE_ID, note.getId());
			noteObject.put(ConstantValue.JSON_USER_NAME, note.getUsername());
			noteObject.put(ConstantValue.JSON_USER_ID, note.getUserid());
			noteObject.put(ConstantValue.JSON_LOC_LONGITUDE,
					note.getLongitude());
			noteObject.put(ConstantValue.JSON_LOC_LATITUDE, note.getLatitude());
			noteObject.put(ConstantValue.JSON_CONTENT, note.getContent());
			noteObject.put(ConstantValue.JSON_INFO, note.getInfo());
			noteObject.put(ConstantValue.JSON_DATE, note.getDate().toString());
			noteObject.put(ConstantValue.JSON_NOTE_IMAGE_URL, ConstantValue.IMAGE_URL_PATH + note.getImageURL());
			
			System.out.println(note.getUserid());
			User user = (User)DBFacade.findById(note.getUserid(), User.class);
			noteObject.put(ConstantValue.JSON_USER_IMAGE_URL,ConstantValue.IMAGE_URL_PATH +  user.getImageURL()); 

			jsonArray.add(noteObject);
		}

		jsonObject.put(ConstantValue.KEY_NOTE_LIST, jsonArray);
		
		//Writer writer = new StringWriter();
		jsonObject.writeJSONString(resp.getWriter());
		
		//System.out.print(jsonObject.toString());

		//PrintWriter out = resp.getWriter();
		//out.println(jsonObject.toString());
	}

}
