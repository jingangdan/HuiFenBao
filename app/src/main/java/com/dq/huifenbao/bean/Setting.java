package com.dq.huifenbao.bean;

/**
 * 设置
 * Created by jingang on 2018/5/18.
 */

public class Setting {

    /**
     * status : 1
     * data : {"open_register":"1","sms":"0"}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * open_register : 1
         * sms : 0
         */

        private String open_register;
        private String sms;

        public String getOpen_register() {
            return open_register;
        }

        public void setOpen_register(String open_register) {
            this.open_register = open_register;
        }

        public String getSms() {
            return sms;
        }

        public void setSms(String sms) {
            this.sms = sms;
        }
    }
}
