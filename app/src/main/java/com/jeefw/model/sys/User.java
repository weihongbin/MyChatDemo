package com.jeefw.model.sys;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/5.
 */
public class User  implements Serializable {
    private static final long serialVersionUID = -6819450447997313562L;
    private String userId;
    private String userName;
    private String passWord;
    private String type;//login: register
    private boolean flag;
    public User() {
    }

    public User(String userId, String userName, String passWord,String type) {
        this.userId = userId;
        this.userName = userName;
        this.passWord = passWord;
        this.type=type;
    }

    public boolean isFlag() {
        return flag;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                '}';
    }
}
