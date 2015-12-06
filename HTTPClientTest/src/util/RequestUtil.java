package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import model.Note;


/**
 * Created by yulei on 2015/12/1.
 */
public class RequestUtil {
    public static List<Note> parseNoteList(String jsonStr) {
//    	jsonStr.replace("\\\"", "\"");
//    	System.out.println(jsonStr);
//    	JSONParser parser = new JSONParser();
//        List<Note> notes = new ArrayList<>();
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//        try {
//            JSONObject jsonObject = (JSONObject)parser.parse(jsonStr);
//            JSONArray array = (JSONArray)parser.parse((String)jsonObject.get(ConstantValue.KEY_NOTE_LIST));
//            for (int i = 0; i < array.size(); i++) {
//                JSONObject noteObject = (JSONObject)array.get(i);
//                int noteId = (int)noteObject.get(ConstantValue.JSON_NOTE_ID);
//                int userId = (int)noteObject.get(ConstantValue.JSON_USER_ID);
//                String userName = (String)noteObject.get(ConstantValue.JSON_USER_NAME);
//                float longitude = Float.parseFloat((String)noteObject.get(ConstantValue.JSON_LOC_LONGITUDE));
//                float latitude = Float.parseFloat((String)noteObject.get(ConstantValue.JSON_LOC_LATITUDE));
//                String content = (String)noteObject.get(ConstantValue.JSON_CONTENT);
//                String info = (String)noteObject.get(ConstantValue.JSON_INFO);
//                String url = (String)noteObject.get(ConstantValue.JSON_IMAGE_URL);
//
//                String dateStr = (String)noteObject.get(ConstantValue.JSON_DATE);
//                Date date = null;
//                try {
//                    date = format.parse(dateStr);
//                } catch (ParseException e) {
//                   // e.printStackTrace();
//                }
//
//                Note note = new Note(longitude, latitude, null, content, 0, userId, userName, info);
//                note.setId(noteId);
//                notes.add(note);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return null;
    }
}
