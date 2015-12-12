package lgm.cmu.spotagram.servlet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.IOUtils;
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
@WebServlet("/UploadProfileServlet")
@MultipartConfig
public class UploadProfileServlet extends HttpServlet {

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
		 
		final InputStream imageInputStream = extractImageInputStream(req);
		final int userId = extractUserId(req);
		final String fileName = extractFileName(req);
		
		final JSONObject jsonObject = new JSONObject();
		final User user = (User)DBFacade.findById(userId, User.class);
		
		if (userId == -1 || imageInputStream == null || user == null) {
			jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_ERR);
		} else {
			final String imageFileName = userId + "_profile_" + fileName;
			final File imageFile = new File(ConstantValue.IMAGE_DISK_PATH + imageFileName);
			
			// create the new file
			if (!imageFile.exists()) {
				System.out.println("try to create the file");
				imageFile.createNewFile();
			}
			
			// copy the file to server
			OutputStream imageOutputStream = new FileOutputStream(imageFile);
			IOUtils.copy(imageInputStream, imageOutputStream);
			imageInputStream.close();
			imageOutputStream.close();
			
			// update the database
			user.setImageURL(imageFileName);
			
			if (DBFacade.update(user)) {
				jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_OK);
			} else {
				jsonObject.put(ConstantValue.KEY_RESULT, ConstantValue.RESULT_ERR);
			}
			
		}

		jsonObject.writeJSONString(resp.getWriter());
	}
	
	private String extractFileName(HttpServletRequest req) {
		// TODO Auto-generated method stub
		Part idPart;
		String fileName = null;
		try {
			idPart = req.getPart(ConstantValue.KEY_FILE_NAME);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(idPart.getInputStream()));
			fileName =  bufferedReader.readLine();
			bufferedReader.close();
		} catch (IllegalStateException | IOException | ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileName;
	}

	private int extractUserId(HttpServletRequest req) {
		// TODO Auto-generated method stub
		Part idPart;
		int userId = -1;
		try {
			idPart = req.getPart(ConstantValue.KEY_USER_ID);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(idPart.getInputStream()));
			userId = Integer.parseInt(bufferedReader.readLine());
			bufferedReader.close();
		} catch (IllegalStateException | IOException | ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO: handle exception
			userId = -1;
		}
		
		return userId;
	}

	private InputStream extractImageInputStream(HttpServletRequest req) {
		Part imagePart = null;
		try {
			imagePart = req.getPart(ConstantValue.KEY_PROFILE);
			return imagePart.getInputStream();
		} catch (IllegalStateException | IOException | ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

}
