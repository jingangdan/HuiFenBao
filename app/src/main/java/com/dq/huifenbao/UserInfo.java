package com.dq.huifenbao;

/**
 * Created by jingang on 2018/3/31.
 */

public class UserInfo {
    /**
     * status : 1
     * data : {"id":"1","idcard":"37132119870912393X","name":"刘波1","mobile":"18669974365"}
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
         * id : 1
         * idcard : 37132119870912393X
         * name : 刘波1
         * mobile : 18669974365
         */

        private String id;
        private String idcard;
        private String name;
        private String mobile;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
