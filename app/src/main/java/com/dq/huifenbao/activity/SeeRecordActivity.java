package com.dq.huifenbao.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dq.huifenbao.bean.UserInfo2;
import com.dq.huifenbao.utils.GsonUtil;
import com.dq.huifenbao.utils.MySharedPreferences;
import com.dq.huifenbao.R;
import com.dq.huifenbao.adapter.RecordAdapter;
import com.dq.huifenbao.bean.Record;
import com.dq.huifenbao.openssl.Base64Utils;
import com.dq.huifenbao.openssl.RSAUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.URLEncoder;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus on 2018/3/31.
 */

public class SeeRecordActivity extends Activity {
    @Bind(R.id.actvIdcards)
    AutoCompleteTextView actvIdcard;
    @Bind(R.id.butSeeRecord)
    Button butSeeRecord;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.tvNoRecord)
    TextView tvNoRecord;
    @Bind(R.id.ivBack)
    ImageView ivBack;

    private String[] array;
    private String PATH = "";
    private String PATH_RSA = "";
    private RequestParams params = null;

    private String idcard;

    private List<Record.DataBean> list = new ArrayList<>();
    private RecordAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        setUI();
        mAdapter = new RecordAdapter(this, list);
        listView.setAdapter(mAdapter);

        //setAutoCompleteTextView();
    }

    public void setUI() {
        if (!TextUtils.isEmpty(MySharedPreferences.getPreference(this, "idcard"))) {
            actvIdcard.setText(MySharedPreferences.getPreference(this, "idcard"));
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
        actvIdcard.setAdapter(adapter);
        //设置输入多少字符后提示，默认值为2，在此设为１
        actvIdcard.setThreshold(1);
    }

    @OnClick(R.id.butSeeRecord)
    public void onViewClicked() {
        setAcClear();
        //idcard = actvIdcard.getText().toString().trim();
        PATH_RSA = "idcard=" + actvIdcard.getText().toString().trim();
        if (!TextUtils.isEmpty(PATH_RSA)) {

            try {
                PrivateKey privateKey = RSAUtils.loadPrivateKey(RSAUtils.PRIVATE_KEY);
                byte[] encryptByte = RSAUtils.encryptDataPrivate(PATH_RSA.getBytes(), privateKey);
                getRecord(URLEncoder.encode(Base64Utils.encode(encryptByte).toString(), "UTF-8"));

                // getUser(URLEncoder.encode(Base64Utils.encode(encryptByte).toString(), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    String record_result;

    public void getRecord(String sign) {
        PATH = "http://huifenbao.dequanhuibao.com/Api/Index/search?sign=" + sign;
        params = new RequestParams(PATH);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                record_result = result;
                Log.e("record", result);
                Record record = GsonUtil.gsonIntance().gsonToBean(result, Record.class);
                if (record.getStatus() == 1) {
                    tvNoRecord.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    list.clear();
                    list.addAll(record.getData());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!TextUtils.isEmpty(record_result)) {
                    UserInfo2 u2 = GsonUtil.gsonIntance().gsonToBean(record_result, UserInfo2.class);
                    if (u2.getStatus() == 0) {
                        tvNoRecord.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
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

    public void setAcClear() {
        InputMethodManager inputmanger = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(this.getWindow().peekDecorView().getWindowToken(), 0);
    }


    @OnClick(R.id.ivBack)
    public void onClick() {
        SeeRecordActivity.this.finish();
    }
}
