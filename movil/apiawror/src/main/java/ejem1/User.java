package ejem1;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
    private int id_user;
    private String user_name;
    private String real_name;
    private String real_surname;
    private String password;
    private String email;

    public User(int id_user, String user_name, String real_name, String real_surname, String password, String email) {
    
        this.id_user = id_user;
        this.real_name = real_name;
        this.real_surname = real_surname;
        this.password = password;
        this.email = email;
    }
    public User() {
       
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getReal_surname() {
        return real_surname;
    }

    public void setReal_surname(String real_surname) {
        this.real_surname = real_surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
