package com.dq.huifenbao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus on 2018/3/31.
 */

public class PayReturnActivity extends Activity {
    @Bind(R.id.tvPROrdersn)
    TextView tvPROrdersn;
    @Bind(R.id.tvZOrdersn)
    TextView tvZOrdersn;
    @Bind(R.id.tvPRMoney)
    TextView tvPRMoney;
    @Bind(R.id.butGoShouYe)
    Button butGoShouYe;
    @Bind(R.id.butGoSeeRecord)
    Button butGoSeeRecord;

    private String zhifubao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payreturn);
        ButterKnife.bind(this);

        zhifubao = getIntent().getStringExtra("zhifubao");
        if(!TextUtils.isEmpty(zhifubao)){
            ZhiFuBao zhiFuBao = GsonUtil.gsonIntance().gsonToBean(zhifubao, ZhiFuBao.class);
            tvPROrdersn.setText(zhiFuBao.getAlipay_trade_app_pay_response().getOut_trade_no());
            tvZOrdersn.setText(zhiFuBao.getAlipay_trade_app_pay_response().getTrade_no());
            tvPRMoney.setText(zhiFuBao.getAlipay_trade_app_pay_response().getTotal_amount());
        }
    }

    @OnClick({R.id.butGoShouYe, R.id.butGoSeeRecord})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.butGoShouYe:
                PayReturnActivity.this.finish();
                break;
            case R.id.butGoSeeRecord:
                startActivity(new Intent(PayReturnActivity.this, SeeRecordActivity.class));
                PayReturnActivity.this.finish();
                break;
        }
    }
}
