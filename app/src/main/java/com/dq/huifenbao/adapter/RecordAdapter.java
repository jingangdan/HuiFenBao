package com.dq.huifenbao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dq.huifenbao.R;
import com.dq.huifenbao.bean.Record;

import java.util.List;

/**
 * Created by jingang on 2018/3/31.
 */

public class RecordAdapter extends BaseAdapter {
    private List<Record.DataBean> list;
    private Context mContext;

    public RecordAdapter(Context mContext, List<Record.DataBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, viewGroup, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvOrdersn.setText("订单号：" + list.get(i).getOrdersn());
        holder.tvPaytime.setText("还款时间：" + list.get(i).getPaytime());
        holder.tvMoney.setText("还款记录：" + list.get(i).getMoney());

        return convertView;
    }

    class ViewHolder {
        TextView tvOrdersn, tvPaytime, tvMoney;

        public ViewHolder(View itemView) {
            tvOrdersn = (TextView) itemView.findViewById(R.id.tvOrdersn);
            tvPaytime = (TextView) itemView.findViewById(R.id.tvPaytime);
            tvMoney = (TextView) itemView.findViewById(R.id.tvMoney);

        }

    }


}
