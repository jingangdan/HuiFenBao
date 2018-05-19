package com.dq.huifenbao.bean;

/**
 * Created by asus on 2018/3/31.
 */

public class GetOrder {

    /**
     * status : 1
     * data : {"id":"37","uid":"6","money":"5.00","status":"0","addtime":"1522483696","paytime":"","paytype":"","ordersn":"LB2018033116081618949","payorder":""}
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
         * id : 37
         * uid : 6
         * money : 5.00
         * status : 0
         * addtime : 1522483696
         * paytime :
         * paytype :
         * ordersn : LB2018033116081618949
         * payorder :
         */

        private String id;
        private String uid;
        private String money;
        private String status;
        private String addtime;
        private String paytime;
        private String paytype;
        private String ordersn;
        private String payorder;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getPaytime() {
            return paytime;
        }

        public void setPaytime(String paytime) {
            this.paytime = paytime;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public String getOrdersn() {
            return ordersn;
        }

        public void setOrdersn(String ordersn) {
            this.ordersn = ordersn;
        }

        public String getPayorder() {
            return payorder;
        }

        public void setPayorder(String payorder) {
            this.payorder = payorder;
        }
    }
}
