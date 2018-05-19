package com.dq.huifenbao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dq.huifenbao.R;
import com.dq.huifenbao.bean.Setting;
import com.dq.huifenbao.bean.UserInfo1;
import com.dq.huifenbao.bean.UserInfo2;
import com.dq.huifenbao.openssl.Base64Utils;
import com.dq.huifenbao.openssl.RSAUtils;
import com.dq.huifenbao.utils.GsonUtil;
import com.dq.huifenbao.utils.HttpPath;
import com.dq.huifenbao.utils.MySharedPreferences;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.security.PrivateKey;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录
 * Created by jingang on 2018/5/17.
 */

public class LoginActivity extends Activity {
    @Bind(R.id.actvLoginIdcard)
    AutoCompleteTextView actvLoginIdcard;
    @Bind(R.id.etLoginPwd)
    EditText etLoginPwd;
    @Bind(R.id.butLogin)
    Button butLogin;
    @Bind(R.id.tvLoginRes)
    TextView tvLoginRes;
    @Bind(R.id.tvLoginForget)
    TextView tvLoginForget;

    private LoginActivity TAG = LoginActivity.this;

    private RequestParams params = null;
    private String[] array;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        x.Ext.init(this.getApplication());
        x.Ext.setDebug(org.xutils.BuildConfig.DEBUG);
        if (TextUtils.isEmpty(MySharedPreferences.getPreference(TAG, "isLogin"))) {
            get();
        } else {
            startActivity(new Intent(TAG, MainActivity.class));
            TAG.finish();
        }

        setAutoCompleteTextView();

    }

    @OnClick({R.id.butLogin, R.id.tvLoginRes, R.id.tvLoginForget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLoginRes:
                startActivity(new Intent(this, ResActivity.class));
                break;
            case R.id.tvLoginForget:
                break;
            case R.id.butLogin:
                if (!TextUtils.isEmpty(actvLoginIdcard.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(etLoginPwd.getText().toString().trim())) {
                        login();
                    } else {
                        Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入身份号", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void setAutoCompleteTextView() {
        array = new String[3];
        array[0] = MySharedPreferences.getPhone1(this) + "";
        array[1] = MySharedPreferences.getPhone2(this) + "";
        array[2] = MySharedPreferences.getPhone3(this) + "";

        //创建 AutoCompleteTextView 适配器 (输入提示)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                array);
        //初始化autoCompleteTextView
        actvLoginIdcard.setAdapter(adapter);
        //设置输入多少字符后提示，默认值为2，在此设为１
        actvLoginIdcard.setThreshold(1);
    }

    //判断登录的账号有没有保存，没有就保存起来，替换一个最久没登录的
    public static void phones(Context context, String[] array, String phone) {
        //当 phone 是新登录的账号的时候
        boolean trfa = (!phone.equals(array[0]) && !phone.equals(array[1]) && !phone.equals(array[2]));
        if (trfa) {
            //循环一下，将最后一个替换成新的
            MySharedPreferences.setPhone3(array[1], context);
            MySharedPreferences.setPhone2(array[0], context);
            MySharedPreferences.setPhone1(phone, context);
        }
    }


    public void login() {
        String PATH_RSA = "idcard=" + actvLoginIdcard.getText().toString().trim() + "&pwd=" + etLoginPwd.getText().toString() + "&timestamp=" + (System.currentTimeMillis() / 100);
        System.out.println("登录 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUtilsLogin(Base64Utils.encode(encryptByte).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String login_result;

    public void xUtilsLogin(String sign) {
        System.out.println("登录 = " + HttpPath.LOGIN + "sign=" + sign);
        params = new RequestParams(HttpPath.LOGIN);
        params.addBodyParameter("sign", sign);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                login_result = result;
                System.out.println("登录 = " + result);
                UserInfo1 u1 = GsonUtil.gsonIntance().gsonToBean(result, UserInfo1.class);
                if (u1.getStatus() == 1) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                    phones(TAG, array, u1.getData().getIdcard());
                    MySharedPreferences.savePreference(LoginActivity.this, "isLogin", "1");
                    MySharedPreferences.savePreference(TAG, "idcard", u1.getData().getIdcard());
                    MySharedPreferences.savePreference(TAG, "name", u1.getData().getName());
                    MySharedPreferences.savePreference(TAG, "mobile", u1.getData().getMobile());

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                }
                if (u1.getStatus() == 0) {
                    Toast.makeText(LoginActivity.this, "" + u1.getData(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!TextUtils.isEmpty(login_result)) {
                    UserInfo2 u2 = GsonUtil.gsonIntance().gsonToBean(login_result, UserInfo2.class);
                    if (u2.getStatus() == 0) {
                        Toast.makeText(LoginActivity.this, u2.getData(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void get() {
        String PATH_RSA = "timestamp=" + (System.currentTimeMillis() / 100);
        System.out.println("设置 = " + PATH_RSA);
        try {
            PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
            byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
            xUtilsGet(Base64Utils.encode(encryptByte).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void xUtilsGet(String sign) {
        System.out.println("设置 = " + HttpPath.GET + "sign=" + sign);
        params = new RequestParams(HttpPath.GET);
        params.addBodyParameter("sign", sign);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("设置 = " + result);
                Setting setting = GsonUtil.gsonIntance().gsonToBean(result, Setting.class);
                if (setting.getStatus() == 1) {
                    setting.getData().getOpen_register();
                    if (setting.getData().getOpen_register().equals("1")) {
                        //注册开启
                        MySharedPreferences.savePreference(TAG, "isRes", "1");
                        tvLoginRes.setVisibility(View.VISIBLE);

                    } else if (setting.getData().getOpen_register().equals("0")) {
                        //注册关闭
                        MySharedPreferences.savePreference(TAG, "isRes", "");
                        tvLoginRes.setVisibility(View.INVISIBLE);
                    }

                    if (setting.getData().getSms().equals("1")) {
                        //验证码开启
                        MySharedPreferences.savePreference(TAG, "isVerify", "1");
                        //tvLoginForget.setVisibility(View.VISIBLE);
                    } else if (setting.getData().getSms().equals("0")) {
                        //验证码关闭
                        MySharedPreferences.savePreference(TAG, "isVerify", "");
                        // tvLoginForget.setVisibility(View.INVISIBLE);
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

}
