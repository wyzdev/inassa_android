package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Noel Emmanuel Roodly on 8/31/2019.
 */
public class KeyRequestUser {
    @SerializedName("username") @Expose private String username = "wyzdev@nassagroup.com";
    @SerializedName("password") @Expose private String password = "W1Yz$54@8jha$1";
    @SerializedName("key") @Expose private String key;
    private Date date;
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getUsername() {
        return username;
    }

    public void setUsername() {
//        this.username = "jotest@test.com";
        this.username = "wyzdev@nassagroup.com";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword() {
//        this.password = "P@$$w0rd";
        this.password = "W1Yz$54@8jha$1";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
