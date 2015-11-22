package lgm.cmu.spotagram.model;

/**
 * Created by MiaojunLi on 15/11/11.
 */
public class User {
    private String ID;
    private String userName;
    private String password;
    private String gender;
    private String email;
    private String other_info;

    public User(String userName,String password,String ID){
        this.userName=userName;
        this.ID=ID;
        this.password=password;
    }

    public boolean isValidPassword(String password){
        return this.password.equals(password);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOther_info() {
        return other_info;
    }

    public void setOther_info(String other_info) {
        this.other_info = other_info;
    }
}
