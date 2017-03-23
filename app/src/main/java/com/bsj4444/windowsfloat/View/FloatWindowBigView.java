package com.bsj4444.windowsfloat.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bsj4444.windowsfloat.R;
import com.bsj4444.windowsfloat.service.FloatWindowService;

import java.util.List;

/**
 * Created by Administrator on 17.3.11.
 */

public class FloatWindowBigView extends LinearLayout {

    public static int viewWidth;
    public static int viewHeight;
    //Context context;

    public FloatWindowBigView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_big,this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        Button close = (Button) findViewById(R.id.close);
        Button back = (Button) findViewById(R.id.back);

        //final List<Drawable> icon = MyWindowmanager.getIconList(context);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new MyAdapter(context));

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWindowmanager.removeBigWindow(context);
                MyWindowmanager.removeSmallWindow(context);
                Intent intent = new Intent(getContext(), FloatWindowService.class);
                context.stopService(intent);

            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWindowmanager.removeBigWindow(context);
                MyWindowmanager.createSmallWindow(context);

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MyWindowmanager.removeBigWindow(getContext());
        MyWindowmanager.createSmallWindow(getContext());
        return super.onTouchEvent(event);
    }
}
