package com.jeefw.model.sys;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/5.
 */
public class Information  extends  User implements Serializable{
    private static final long serialVersionUID = -6819450447997313562L;

    private String username;
    private String toname;
    private String con;
    private  String type;
    public Information() {
    }
    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
    public Information(String username, String toname, String con,String type) {
        this.username = username;
        this.toname = toname;
        this.con = con;
        this.type=type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    @Override
    public String toString() {
        return "Information{" +
                "username='" + username + '\'' +
                ", toname='" + toname + '\'' +
                ", con='" + con + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
