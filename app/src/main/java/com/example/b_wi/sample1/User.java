package com.example.b_wi.sample1;

public class User {
    int Uid;
    String pwd;
    String phone;

    public User(int uid) {
        Uid = uid;
    }

    public User(){};

    public User(String pwd, String phone) {
        this.pwd = pwd;
        this.phone = phone;
    }

    public int getUid() {
        return Uid;
    }

    public void setUid(int uid) {
        Uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}