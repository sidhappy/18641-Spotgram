package lgm.cmu.spotagram.Model;

/**
 * Created by MiaojunLi on 15/11/11.
 */
public class Post {
    private String postID;
    private String content;
    private String time;
    private String date;
    private String location;
    private String post_user;
    private int commentNum;

    public Post(String postID, String content, String time, String date, String location, String post_user) {
        this.postID = postID;
        this.content = content;
        this.time = time;
        this.date = date;
        this.location = location;
        this.post_user = post_user;
        this.commentNum =0;
    }

    public void comment(){
        commentNum++;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPost_user() {
        return post_user;
    }

    public void setPost_user(String post_user) {
        this.post_user = post_user;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }
}
