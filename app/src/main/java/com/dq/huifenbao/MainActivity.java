package com.dq.huifenbao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dq.huifenbao.openssl.Base64Utils;
import com.dq.huifenbao.openssl.RSAUtils;

import org.xutils.*;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.ll_mian)
    LinearLayout llMian;
    @Bind(R.id.etIdcard)
    EditText etIdcard;
    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.etPhone)
    EditText etPhone;
    @Bind(R.id.etMoney)
    EditText etMoney;
    @Bind(R.id.butOk)
    Button butOk;
    @Bind(R.id.butSearch)
    Button butSearch;
    @Bind(R.id.actvIdcard)
    AutoCompleteTextView actvIdcard;

    private String idcard, name, mobile, money;

    private String PATH = "";
    private RequestParams params = null;
    private String string_result;

    private String[] array;

    private String PATH_RSA = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        x.Ext.init(this.getApplication());
        x.Ext.setDebug(org.xutils.BuildConfig.DEBUG);

        setAutoCompleteTextView();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setAutoCompleteTextView();
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
        actvIdcard.setAdapter(adapter);
        //设置输入多少字符后提示，默认值为2，在此设为１
        actvIdcard.setThreshold(1);
    }

    @OnClick(R.id.ll_mian)
    public void onClick() {
        // 隐藏虚拟键盘
        InputMethodManager inputmanger = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(this.getWindow().peekDecorView().getWindowToken(), 0);

        //idcard = etIdcard.getText().toString().trim();
       // idcard = actvIdcard.getText().toString().trim();
        PATH_RSA = "idcard="+actvIdcard.getText().toString().trim();

        if (!TextUtils.isEmpty(PATH_RSA)) {
            try {
                PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
                byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
                getUser(URLEncoder.encode(Base64Utils.encode(encryptByte).toString(), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.butOk, R.id.butSearch})
    public void onViewClicked(View view) {
        // idcard = etIdcard.getText().toString().trim();
        idcard = actvIdcard.getText().toString().trim();
        name = etName.getText().toString().trim();
        mobile = etPhone.getText().toString().trim();
        money = etMoney.getText().toString().trim();

        PATH_RSA = "idcard="+idcard+"&name="+name+"&mobile="+mobile+"&money="+money;

        switch (view.getId()) {
            case R.id.butOk:
                if (!TextUtils.isEmpty(idcard)) {
                    if (!TextUtils.isEmpty(name)) {
                        if (!TextUtils.isEmpty(mobile)) {
                            if (!TextUtils.isEmpty(money)) {
                                try {
                                    PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
                                    byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
                                    addOrder(URLEncoder.encode(Base64Utils.encode(encryptByte).toString(), "UTF-8"));

                                   // getUser(URLEncoder.encode(Base64Utils.encode(encryptByte).toString(), "UTF-8"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                Toast.makeText(this, "请输入还款金额", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入身份证号", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.butSearch:
                Intent intent = new Intent(this, SeeRecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void getUser(String afterencrypt) {
        // PATH = "http://huifenbao.dequanhuibao.com/Api/Index/getuser?idcard=" + idcard;
        PATH = "http://huifenbao.dequanhuibao.com/Api/Index/getuser?sign=" + afterencrypt;
        params = new RequestParams(PATH);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                string_result = result;
                System.out.println("getUser = " + result);
                UserInfo userInfo = GsonUtil.gsonIntance().gsonToBean(result, UserInfo.class);
                if (userInfo.getStatus() == 1) {
                    //Toast.makeText(MainActivity.this, "" + userInfo.getData().toString(), Toast.LENGTH_SHORT).show();
                    etName.setText(userInfo.getData().getName());
                    etPhone.setText(userInfo.getData().getMobile());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("error = " + ex);
                if (!TextUtils.isEmpty(string_result)) {
                    ErrorInfo errorInfo = GsonUtil.gsonIntance().gsonToBean(string_result, ErrorInfo.class);
                    if (errorInfo.getStatus() == 1) {
                        // Toast.makeText(MainActivity.this, "" + errorInfo.getData().toString(), Toast.LENGTH_SHORT).show();
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

    private String order_result;

    public void addOrder(String sign) {
        PATH = "http://huifenbao.dequanhuibao.com/Api/Index/addorder?sign="+sign;
        params = new RequestParams(PATH);
//        params.addBodyParameter("idcard", idcard);
//        params.addBodyParameter("name", name);
//        params.addBodyParameter("mobile", mobile);
//        params.addBodyParameter("money", money);

        Log.e("mian_addorder",PATH);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                order_result = result;
                Log.e("mian_addorder", result);
                AddOrder addOrder = GsonUtil.gsonIntance().gsonToBean(result, AddOrder.class);
                if (addOrder.getStatus() == 1) {
                    phones(MainActivity.this, array, idcard);

                    Intent intent = new Intent(MainActivity.this, PayActivity.class);
                    intent.putExtra("id", addOrder.getData());
                    startActivity(intent);

                    actvIdcard.setText("");
                    etName.setText("");
                    etPhone.setText("");
                    etMoney.setText("");

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!TextUtils.isEmpty(order_result)) {
                    ErrorInfo errorInfo = GsonUtil.gsonIntance().gsonToBean(string_result, ErrorInfo.class);
                    if (errorInfo.getStatus() == 1) {
                        Toast.makeText(MainActivity.this, "" + errorInfo.getData().toString(), Toast.LENGTH_SHORT).show();
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

    //双击的时间间隔
    private long millis = 0;

    /**
     * 监听返回键 双击退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @SuppressLint("WrongConstant")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //双击退出
            if (System.currentTimeMillis() - millis < 1000) {
                return super.onKeyDown(keyCode, event);
            } else {
                Toast.makeText(this, "再次点击退出程序", Toast.LENGTH_SHORT).show();
                millis = System.currentTimeMillis();
                return false;
            }

        }
        return false;
    }

}
