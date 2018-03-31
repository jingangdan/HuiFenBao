package com.dq.huifenbao;

/**
 * Created by asus on 2018/3/31.
 */

public class ErrorInfo {

    /**
     * status : 1
     * data : 用户不存在
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
