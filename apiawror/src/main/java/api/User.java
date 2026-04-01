package api;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

    private int id;
    private String user_name;
    private String real_name;
    private String real_surname;
    private String email;
    private String password;

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}