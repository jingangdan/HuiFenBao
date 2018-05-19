package com.dq.huifenbao.bean;

/**
 * Created by asus on 2018/5/18.
 */

public class UserInfo2 {

    /**
     * status : 0
     * data : 账号或者密码错误
     */

    private int status;
    private String data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
