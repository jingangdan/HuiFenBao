package com.dq.huifenbao.utils;

/**
 * Created by asus on 2018/5/17.
 */

public class HttpPath {
    public static final String URL = "http://huifenbao.dequanhuibao.com/";

    public static final String LOGIN = URL + "Api/User/login?";//登录（idcard，pwd，timestamp）

    public static final String GET = URL + "Api/Sys/get?";//获取系统设置（timestamp）

    public static final String RES = URL + "Api/User/register?";//用户注册（idcard，name,mobile,verify,pwd,timestamp）

    public static final String SMS = URL + "Api/User/sendsms?";//发送短信（mobile，type,timestamp）
}
