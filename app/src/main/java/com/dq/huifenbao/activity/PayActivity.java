package com.dq.huifenbao.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dq.huifenbao.R;
import com.dq.huifenbao.bean.GetOrder;
import com.dq.huifenbao.bean.Pay;
import com.dq.huifenbao.openssl.Base64Utils;
import com.dq.huifenbao.openssl.RSAUtils;
import com.dq.huifenbao.utils.GsonUtil;
import com.dq.huifenbao.zhifubao.AuthResult;
import com.dq.huifenbao.zhifubao.PayResult;

import org.xutils.*;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.net.URLEncoder;
import java.security.PrivateKey;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus on 2018/3/31.
 */

public class PayActivity extends Activity {
    @Bind(com.dq.huifenbao.R.id.tvOrdersn)
    TextView tvOrdersn;
    @Bind(R.id.tvPayMoney)
    TextView tvPayMoney;
    @Bind(R.id.ll_pay)
    LinearLayout llPay;
    @Bind(R.id.ivPayBack)
    ImageView ivPayBack;
    private String id;
    private String PATH = "";
    private String PATH_RSA = "";
    private RequestParams params = null;


    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("WrongConstant")
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        //Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

                        System.out.println("111111 = " + resultInfo);
                        startActivity(new Intent(PayActivity.this, PayReturnActivity.class).putExtra("zhifubao",resultInfo));

                        PayActivity.this.finish();

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);

        x.Ext.init(this.getApplication());
        x.Ext.setDebug(org.xutils.BuildConfig.DEBUG);

       // id = getIntent().getStringExtra("id");
        PATH_RSA = "id="+getIntent().getStringExtra("id");
        if (!TextUtils.isEmpty(PATH_RSA)) {

            try {
                PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
                byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
                getOrder(URLEncoder.encode(Base64Utils.encode(encryptByte).toString(), "UTF-8"));

                // getUser(URLEncoder.encode(Base64Utils.encode(encryptByte).toString(), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getOrder(String sign) {
        PATH = "http://huifenbao.dequanhuibao.com/Api/Index/getorder?sign=" + sign;
        params = new RequestParams(PATH);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("pay_getorder", result);
                GetOrder getOrder = GsonUtil.gsonIntance().gsonToBean(result, GetOrder.class);
                if (getOrder.getStatus() == 1) {
                    tvOrdersn.setText(getOrder.getData().getOrdersn());
                    tvPayMoney.setText(getOrder.getData().getMoney());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void setPay(String sign) {
        PATH = "http://huifenbao.dequanhuibao.com/Api/Index/pay?sign=" + sign;
        params = new RequestParams(PATH);
        Log.e("pay_pay",PATH);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("pay_pay", result);
                Pay pay = GsonUtil.gsonIntance().gsonToBean(result, Pay.class);
                if (pay.getStatus() == 1) {
                    if (!TextUtils.isEmpty(pay.getData())) {
                        setZhuFuBao(pay.getData());
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @OnClick(R.id.ll_pay)
    public void onViewClicked() {
        if (!TextUtils.isEmpty(PATH_RSA)) {
            try {
                PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
                byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
                setPay(URLEncoder.encode(Base64Utils.encode(encryptByte).toString(), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setZhuFuBao(String info) {
        final String orderInfo = info;
        System.out.println("orderInfo  = " + info);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @OnClick(R.id.ivPayBack)
    public void onClick() {
        PayActivity.this.finish();
    }
}
