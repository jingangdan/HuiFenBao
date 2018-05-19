package com.dq.huifenbao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dq.huifenbao.R;
import com.dq.huifenbao.bean.UserInfo2;
import com.dq.huifenbao.openssl.Base64Utils;
import com.dq.huifenbao.openssl.RSAUtils;
import com.dq.huifenbao.utils.GsonUtil;
import com.dq.huifenbao.utils.HttpPath;
import com.dq.huifenbao.utils.MySharedPreferences;
import com.dq.huifenbao.utils.ToastUtils;

import org.xutils.BuildConfig;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.PrivateKey;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 * Created by jingang on 2018/5/17.
 */

public class ResActivity extends Activity {
    @Bind(R.id.actvResIdcard)
    AutoCompleteTextView actvResIdcard;
    @Bind(R.id.etResName)
    EditText etResName;
    @Bind(R.id.etResPhone)
    EditText etResPhone;
    @Bind(R.id.butResVerify)
    Button butResVerify;
    @Bind(R.id.etResVerify)
    EditText etResVerify;
    @Bind(R.id.etResPwd)
    EditText etResPwd;
    @Bind(R.id.etResPwd2)
    EditText etResPwd2;
    @Bind(R.id.butRes)
    Button butRes;
    @Bind(R.id.linResVerify)
    LinearLayout linResVerify;
    @Bind(R.id.ivResBack)
    ImageView ivResBack;

    private boolean isClick;

    private RequestParams params = null;

    //控制按钮样式是否改变
    private boolean tag = true;
    //每次验证请求需要间隔60S
    private int i = 60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        ButterKnife.bind(this);
        x.Ext.init(this.getApplication());
        x.Ext.setDebug(BuildConfig.DEBUG);

        setVerify();

    }

    @OnClick({R.id.ivResBack, R.id.butResVerify, R.id.butRes})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivResBack:
                ResActivity.this.finish();
                break;
            case R.id.butResVerify:
                if (!TextUtils.isEmpty(etResPhone.getText().toString().trim())) {
                    getSms();
                }
                break;
            case R.id.butRes:
                if (TextUtils.isEmpty(actvResIdcard.getText().toString().trim())) {
                    ToastUtils.getInstance(this).showMessage("请输入身份证号");
                    return;
                }
                if (TextUtils.isEmpty(etResName.getText().toString().trim())) {
                    ToastUtils.getInstance(this).showMessage("请输入姓名");
                    return;
                }
                if (TextUtils.isEmpty(etResPhone.getText().toString().trim())) {
                    ToastUtils.getInstance(this).showMessage("请输入手机号");
                    return;
                }
                if (!TextUtils.isEmpty(MySharedPreferences.getPreference(ResActivity.this, "isVerify"))) {
                    if (TextUtils.isEmpty(etResVerify.getText().toString().trim())) {
                        ToastUtils.getInstance(this).showMessage("请输入验证码");
                        return;
                    }
                }
                if (TextUtils.isEmpty(etResPwd.getText().toString().trim())) {
                    ToastUtils.getInstance(this).showMessage("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(etResPwd2.getText().toString().trim())) {
                    ToastUtils.getInstance(this).showMessage("请输入确认密码");
                    return;
                }
                if (!etResPwd.getText().toString().equals(etResPwd2.getText().toString())) {
                    ToastUtils.getInstance(this).showMessage("两次密码输入不一致");
                    return;
                }
                if (!isClick) {
                    isClick = true;
                    // ToastUtils.getInstance(this).showMessage("走起");

                    res();
                }
                break;
        }
    }

    public void setVerify() {
        if (TextUtils.isEmpty(MySharedPreferences.getPreference(ResActivity.this, "isVerify"))) {
            linResVerify.setVisibility(View.GONE);
            butResVerify.setVisibility(View.GONE);
        } else {
            linResVerify.setVisibility(View.VISIBLE);
            butResVerify.setVisibility(View.VISIBLE);
        }
    }

    public void res() {
        String PATH_RSA = "idcard=" + actvResIdcard.getText().toString().trim() + "&name=" + etResName.getText().toString()
                + "&mobile=" + etResPhone.getText().toString() + "&verify=" + etResVerify.getText().toString()
                + "&pwd=" + etResPwd.getText().toString() + "&timestamp=" + (System.currentTimeMillis() / 100);
        System.out.println("注册 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUitlsRes(Base64Utils.encode(encryptByte).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void xUitlsRes(String sign) {
        try {
            System.out.println(HttpPath.RES + "sign=" + URLEncoder.encode(sign, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params = new RequestParams(HttpPath.RES);
        params.addBodyParameter("sign", sign);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                isClick = false;
                System.out.println("注册 = " + result);
                UserInfo2 u2 = GsonUtil.gsonIntance().gsonToBean(result, UserInfo2.class);
                if (u2.getStatus() == 1) {
                    ToastUtils.getInstance(ResActivity.this).showMessage(u2.getData());
                    ResActivity.this.finish();
                }
                if (u2.getStatus() == 0) {
                    ToastUtils.getInstance(ResActivity.this).showMessage(u2.getData());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                isClick = false;

            }

            @Override
            public void onCancelled(CancelledException cex) {
                isClick = false;

            }

            @Override
            public void onFinished() {
                isClick = false;

            }
        });
    }

    public void getSms() {
        String PATH_RSA = "mobile=" + etResPhone.getText().toString().trim() + "&type=1" + "&timestamp=" + (System.currentTimeMillis() / 100);
        System.out.println("发送短信 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUtilsSms(Base64Utils.encode(encryptByte).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void xUtilsSms(String sign) {
        try {
            System.out.println(HttpPath.SMS + "sign=" + URLEncoder.encode(sign, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        params = new RequestParams(HttpPath.SMS);
        params.addBodyParameter("sign", sign);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("发送短信= " + result);
                UserInfo2 u2 = GsonUtil.gsonIntance().gsonToBean(result, UserInfo2.class);
                if (u2.getStatus() == 1) {
                    //发送成功
                    ToastUtils.getInstance(ResActivity.this).showMessage(u2.getData());
                    changeBtnGetCode();
                }
                if (u2.getStatus() == 0) {
                    //发送失败
                    ToastUtils.getInstance(ResActivity.this).showMessage(u2.getData());
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

    /**
     * 改变按钮样式
     */
    private void changeBtnGetCode() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                if (tag) {
                    while (i > 0) {
                        i--;
                        //如果活动为空
                        if (this == null) {
                            break;
                        }

                        ResActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                butResVerify.setText("已发送(" + i + ")");
                                butResVerify.setClickable(false);
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tag = false;
                }
                i = 60;
                tag = true;

                if (this != null) {
                    ResActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            butResVerify.setText("发送短信");
                            butResVerify.setClickable(true);
                        }
                    });
                }
            }
        };
        thread.start();
    }
}
