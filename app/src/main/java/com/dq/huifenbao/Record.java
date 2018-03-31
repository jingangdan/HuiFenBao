package com.dq.huifenbao;

import java.util.List;

/**
 * Created by jingang on 2018/3/31.
 */

public class Record {

    /**
     * status : 1
     * data : [{"id":"17","uid":"1","money":"0.01","status":"1","addtime":"1522318717","paytime":"2018-03-29 18:18:50","paytype":"","ordersn":"LB2018032918183758166","payorder":"2018032921001004140578818220"},{"id":"18","uid":"1","money":"0.01","status":"1","addtime":"1522322738","paytime":"2018-03-29 19:25:50","paytype":"","ordersn":"LB2018032919253843076","payorder":"2018032921001004140579580965"}]
     */

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 17
         * uid : 1
         * money : 0.01
         * status : 1
         * addtime : 1522318717
         * paytime : 2018-03-29 18:18:50
         * paytype :
         * ordersn : LB2018032918183758166
         * payorder : 2018032921001004140578818220
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
