package com.example.chatjp;

public class Users {

    private String mail;
    private String username;
    private String password;
    private String userid;
    private String lastmessage;
    private String status;
    private String imageuri;

    // No-argument constructor
    public Users() {
    }

    // Constructor with arguments
    public Users(String userid, String username, String mail, String password, String imageuri, String status) {
        this.userid = userid;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.status = status;
        this.imageuri = imageuri;
    }

    // Getters and setters




    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public boolean getProfilepic() {
        return false;
    }
}
