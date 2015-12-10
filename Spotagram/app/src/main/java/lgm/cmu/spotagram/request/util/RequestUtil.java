package lgm.cmu.spotagram.request.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lgm.cmu.spotagram.model.Comment;
import lgm.cmu.spotagram.model.Note;
import lgm.cmu.spotagram.utils.ConstantValue;

/**
 * Created by yulei on 2015/12/1.
 */
public class RequestUtil {
    public static List<Note> parseNoteList(String jsonStr) {
        List<Note> notes = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray array = jsonObject.getJSONArray(ConstantValue.KEY_NOTE_LIST);
            for (int i = 0; i < array.length(); i++) {
                JSONObject noteObject = array.getJSONObject(i);
                int noteId = noteObject.getInt(ConstantValue.JSON_NOTE_ID);
                int userId = noteObject.getInt(ConstantValue.JSON_USER_ID);
                String userName = noteObject.getString(ConstantValue.JSON_USER_NAME);
                float longitude = Float.parseFloat(noteObject.getString(ConstantValue.JSON_LOC_LONGITUDE));
                float latitude = Float.parseFloat(noteObject.getString(ConstantValue.JSON_LOC_LATITUDE));
                String content = noteObject.getString(ConstantValue.JSON_CONTENT);
                String info = noteObject.getString(ConstantValue.JSON_INFO);
                String userImageURL = noteObject.getString(ConstantValue.JSON_USER_IMAGE_URL);
                String noteImageURL = noteObject.getString(ConstantValue.JSON_NOTE_IMAGE_URL);

                String dateStr = noteObject.getString(ConstantValue.JSON_DATE);
                Date date = null;
                try {
                    date = format.parse(dateStr);
                } catch (ParseException e) {
                   // e.printStackTrace();
                }

                Note note = new Note(longitude, latitude, date, content, 0, userId, userName, info, userImageURL, noteImageURL);
                note.setId(noteId);
                notes.add(note);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notes;
    }

    public static List<Comment> parseCommentList(String jsonStr) {
        List<Comment> comments = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray array = jsonObject.getJSONArray(ConstantValue.KEY_COMMENT_LIST);
            for (int i = 0; i < array.length(); i++) {
                JSONObject commentObject = array.getJSONObject(i);
                int commentId = commentObject.getInt(ConstantValue.JSON_COMMENT_ID);
                String userName = commentObject.getString(ConstantValue.JSON_USER_NAME);
                int userId = commentObject.getInt(ConstantValue.JSON_USER_ID);
                String content = commentObject.getString(ConstantValue.JSON_CONTENT);

                String dateStr = commentObject.getString(ConstantValue.JSON_DATE);
                Date date = null;
                try {
                    date = format.parse(dateStr);
                } catch (ParseException e) {
                    // e.printStackTrace();
                }

                Comment comment = new Comment(date, content, userId, userName, -1);
                comment.setId(commentId);
                comments.add(comment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return comments;
    }

    public static int parseNoteReplyResult(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            int resultCode = jsonObject.getInt(ConstantValue.KEY_RESULT);
            if (resultCode != ConstantValue.RESULT_OK) {
                return -1;
            } else {
                return jsonObject.getInt(ConstantValue.KEY_COMMENT_ID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    };


    public static int parseNewNoteReplyResult(String jsonStr){
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            int resultCode = jsonObject.getInt(ConstantValue.KEY_RESULT);
            if (resultCode != ConstantValue.RESULT_OK) {
                return -1;
            } else {
                return jsonObject.getInt(ConstantValue.KEY_NOTE_ID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
