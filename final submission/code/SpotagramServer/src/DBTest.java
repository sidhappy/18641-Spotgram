import java.util.List;

import lgm.cmu.spotagram.db.DBFacade;
import lgm.cmu.spotagram.model.Comment;
import lgm.cmu.spotagram.model.Model;
import lgm.cmu.spotagram.model.Note;
import lgm.cmu.spotagram.model.User;

/**
 * DBTest.java 	Version <1.00>	ÏÂÎç1:58:25
 *
 * Copyright(C) 2015-2016  All rights reserved. 
 * Lei YU is a graduate student majoring in Electrical and Electronics Engineering, 
 * from the ECE department, Carnegie Mellon University, PA 15213, United States.
 *
 * Email: leiyu@andrew.cmu.edu
 */
public class DBTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//List<Model> users = DBFacade.findByFieldName(User.class, "userName", "vip");
		List<Model> models = DBFacade.findByFieldName(Comment.class, "userid", 1);
		
		System.out.println(models.size());
		
		List<Model> models2 = DBFacade.findByFieldName(Note.class, "userid", "10");
		
		
		System.out.println(models2.size());
		for (Model model : models2) {
			Note note = (Note)model;
			System.out.println(note.getDate().toString());
		}

	}

}
