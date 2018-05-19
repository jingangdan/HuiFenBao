package com.dq.huifenbao.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dq.huifenbao.R;
import com.dq.huifenbao.bean.UserInfo1;
import com.dq.huifenbao.bean.UserInfo2;
import com.dq.huifenbao.openssl.Base64Utils;
import com.dq.huifenbao.openssl.RSAUtils;
import com.dq.huifenbao.utils.GsonUtil;
import com.dq.huifenbao.utils.MySharedPreferences;
import com.dq.huifenbao.utils.ToastUtils;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.BuildConfig;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.URLEncoder;
import java.security.PrivateKey;

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
    @Bind(R.id.butOut)
    Button butOut;

    private String idcard, name, mobile, money;

    private String PATH = "";
    private RequestParams params = null;
    private String string_result;

    private String[] array;

    private String PATH_RSA = "";

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        x.Ext.init(this.getApplication());
        x.Ext.setDebug(BuildConfig.DEBUG);

        setUI();

        PgyCrashManager.register(this);

        PgyFeedback.getInstance().setMoreParam("tao", "PgyFeedbackShakeManager");

        PgyFeedbackShakeManager.setShakingThreshold(1000);

        PgyUpdateManager.register(MainActivity.this,
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("更新")
                                .setMessage("有新版本上线，是否更新？")
                                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url;
                                        JSONObject jsonData;
                                        try {
                                            jsonData = new JSONObject(
                                                    result);
                                            if ("0".equals(jsonData.getString("code"))) {
                                                JSONObject jsonObject = jsonData
                                                        .getJSONObject("data");
                                                url = jsonObject
                                                        .getString("downloadURL");

                                                startDownloadTask(
                                                        MainActivity.this,
                                                        url);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).show();

                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        Toast.makeText(getApplicationContext(), "没有更新", Toast.LENGTH_SHORT).show();
                    }
                });

        //动态请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS);
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            }
        }

        // setAutoCompleteTextView();

    }

    public void setUI() {
        if (!TextUtils.isEmpty(MySharedPreferences.getPreference(this, "idcard"))) {
            actvIdcard.setText(MySharedPreferences.getPreference(this, "idcard"));
        }
        if (!TextUtils.isEmpty(MySharedPreferences.getPreference(this, "name"))) {
            etName.setText(MySharedPreferences.getPreference(this, "name"));
        }
        if (!TextUtils.isEmpty(MySharedPreferences.getPreference(this, "mobile"))) {
            etPhone.setText(MySharedPreferences.getPreference(this, "mobile"));
        }
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

//    @OnClick(R.id.ll_mian)
//    public void onClick() {
//        // 隐藏虚拟键盘
//        InputMethodManager inputmanger = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputmanger.hideSoftInputFromWindow(this.getWindow().peekDecorView().getWindowToken(), 0);
//
//        //idcard = etIdcard.getText().toString().trim();
//        // idcard = actvIdcard.getText().toString().trim();
//        PATH_RSA = "idcard=" + actvIdcard.getText().toString().trim();
//
//        if (!TextUtils.isEmpty(PATH_RSA)) {
//            try {
//                PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
//                byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
//                getUser(URLEncoder.encode(Base64Utils.encode(encryptByte).toString(), "UTF-8"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @OnClick({R.id.butOk, R.id.butSearch, R.id.butOut})
    public void onViewClicked(View view) {
        // idcard = etIdcard.getText().toString().trim();
        idcard = actvIdcard.getText().toString().trim();
        name = etName.getText().toString().trim();
        mobile = etPhone.getText().toString().trim();
        money = etMoney.getText().toString().trim();

        PATH_RSA = "idcard=" + idcard + "&name=" + name + "&mobile=" + mobile + "&money=" + money;

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

            case R.id.butOut:
                //退出
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("确定退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MySharedPreferences.savePreference(MainActivity.this, "isLogin", "");
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                builder.create().dismiss();
                            }
                        });
                builder.create().show();

                break;
        }
    }

    public void getUser(String afterencrypt) {
        // PATH = "http://huifenbao.dequanhuibao.com/Api/Index/getuser?idcard=" + idcard;
        PATH = "http://huifenbao.dequanhuibao.com/Api/Index/getuser?sign=" + afterencrypt;
        System.out.println("111 = " + PATH);
        params = new RequestParams(PATH);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                string_result = result;
                System.out.println("getUser = " + result);
                UserInfo1 userInfo = GsonUtil.gsonIntance().gsonToBean(result, UserInfo1.class);
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
                    UserInfo2 u2 = GsonUtil.gsonIntance().gsonToBean(string_result, UserInfo2.class);
                    if (u2.getStatus() == 1) {
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
        PATH = "http://huifenbao.dequanhuibao.com/Api/Index/addorder?sign=" + sign;
        params = new RequestParams(PATH);
        Log.e("mian_addorder", PATH);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                order_result = result;
                Log.e("mian_addorder", result);
                UserInfo2 u2 = GsonUtil.gsonIntance().gsonToBean(result, UserInfo2.class);
                if (u2.getStatus() == 1) {
                    Intent intent = new Intent(MainActivity.this, PayActivity.class);
                    intent.putExtra("id", u2.getData());
                    startActivity(intent);
                }

                if (u2.getStatus() == 0) {
                    Toast.makeText(MainActivity.this, "" + u2.getData(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!TextUtils.isEmpty(order_result)) {
                    UserInfo2 u2 = GsonUtil.gsonIntance().gsonToBean(string_result, UserInfo2.class);
                    if (u2.getStatus() == 1) {
                        Toast.makeText(MainActivity.this, "" + u2.getData().toString(), Toast.LENGTH_SHORT).show();
                    }

                    if (u2.getStatus() == 0) {
                        ToastUtils.getInstance(MainActivity.this).showMessage(u2.getData());
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

    @Override
    protected void onPause() {
        PgyFeedbackShakeManager.unregister();
        super.onPause();
    }

    @Override
    protected void onResume() {
        PgyFeedbackShakeManager.register(MainActivity.this, false);
//        PgyUpdateManager.unregister();
        super.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                    }
                }
            }
            case REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "允许读写存储！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "未允许读写存储！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
            }
        }
    }

}
