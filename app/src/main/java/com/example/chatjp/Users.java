package com.example.chatjp;

public class Users {
    String profilepic,mail,username,password,userid,lastmessage,status;

    public Users(String id, String namee, String emaill, String password, String cPassword, String imageuri, String status){}
    public Users(String userid, String username, String emaill, String password, String profilepic, String status)
    {
        this.userid = userid;
        this.username = username;
        this.mail = emaill;
        this.password = password;
        this.profilepic = profilepic;
        this.status = status;
    }


    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

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
}
