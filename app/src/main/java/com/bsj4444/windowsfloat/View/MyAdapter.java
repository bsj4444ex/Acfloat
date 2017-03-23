package com.bsj4444.windowsfloat.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bsj4444.windowsfloat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 17.3.14.
 */

public class MyAdapter extends BaseAdapter {

    private List<Drawable> icons=null;
    Context context;
    LayoutInflater mInflater;

    public MyAdapter(Context context){
        this.context=context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        icons = new ArrayList<Drawable>();
        icons = MyWindowmanager.getIconList(context);
    }

    @Override
    public int getCount() {
        return icons.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView appIcon = new ImageView(context);
//        appIcon.setImageDrawable(icons.get(position));
//        return appIcon;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.iteam, null);
            holder.appIcon = (ImageView)convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.appIcon.setImageDrawable(icons.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView appIcon;
    }

}
