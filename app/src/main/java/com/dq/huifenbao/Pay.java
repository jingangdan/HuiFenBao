package com.dq.huifenbao;

/**
 * Created by asus on 2018/3/31.
 */

public class Pay {

    /**
     * status : 1
     * data : alipay_sdk=alipay-sdk-php-20161101&app_id=2018031502377719&biz_content=%7B%22body%22%3A%22%5Cu60e0%5Cu5206%5Cu5b9d%5Cu8fd8%5Cu6b3e%22%2C%22subject%22%3A%22%5Cu60e0%5Cu5206%5Cu5b9d%5Cu8fd8%5Cu6b3e%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22out_trade_no%22%3A%22LB2018033115250559305%22%2C%22total_amount%22%3A%225.00%22%2C%22timeout_express%22%3A%2230m%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2Fhuifenbao.dequanhuibao.com%2FPay%2FPay%2Faliwap.html&sign_type=RSA2×tamp=2018-03-31+16%3A13%3A05&version=1.0&sign=NiA8dW6nGhiXS8P4SEEGvXOHJrfp7H3PeyqXg2jYnZgJYj%2B%2FRl2BUwr%2B48KIQJ22N3Yk8aQhINv4R%2Bg97ErBsyUpyA7Dc10Y4%2B5d2kFbNLJMRPpXUHjngqH7WOzZLbfF7TLnxBlzd1RNEvJEZSfM80PJxK%2FYs%2BmIm%2BOp7DiVjAufbcTywQPKvvmNqDmU8DBgZDlN%2FR56cmBWEDeGajFekydcamv2uzEPKUZ4ivJNlAeMM1zpzDKcog8ashngnE9e758q9qV5eIAktNl%2Fj3euZKA0FXTjCrIb8iAChgAPNV4FA5uzHa%2BYxS%2BEfWSEbgvuX5slLXNnnm1j6wVPg62MNw%3D%3D
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
