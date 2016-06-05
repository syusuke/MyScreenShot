package com.gson8.myscreenshot.video;

/*
 * ScreenShot making by Syusuke/琴声悠扬 on 2016/6/2
 * E-Mail: Zyj7810@126.com
 * Package: gson8.com.vedioshot.adapter.SpinnerAdapter
 * Description: null
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter {

    private Object[] obj;
    private Context mContext;

    public SpinnerAdapter(Context contexts, Object[] objects) {
        this.mContext = contexts;
        this.obj = objects;
    }


    @Override
    public int getCount() {
        return obj.length;
    }

    @Override
    public Object getItem(int position) {
        return obj[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(obj[position].toString());

        return convertView;
    }


    class ViewHolder {
        TextView tv;
    }

}
