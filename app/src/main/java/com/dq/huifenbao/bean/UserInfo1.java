package com.dq.huifenbao.bean;

/**
 * Created by jingang on 2018/3/31.
 */

public class UserInfo1 {

    /**
     * status : 1
     * data : {"id":"1","idcard":"37132119870912393X","name":"刘波","mobile":"18669974364","is_del":"0","regtime":"0","is_reg":"1","review":"0"}
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
         * name : 刘波
         * mobile : 18669974364
         * is_del : 0
         * regtime : 0
         * is_reg : 1
         * review : 0
         */

        private String id;
        private String idcard;
        private String name;
        private String mobile;
        private String is_del;
        private String regtime;
        private String is_reg;
        private String review;

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

        public String getIs_del() {
            return is_del;
        }

        public void setIs_del(String is_del) {
            this.is_del = is_del;
        }

        public String getRegtime() {
            return regtime;
        }

        public void setRegtime(String regtime) {
            this.regtime = regtime;
        }

        public String getIs_reg() {
            return is_reg;
        }

        public void setIs_reg(String is_reg) {
            this.is_reg = is_reg;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }
    }
}
