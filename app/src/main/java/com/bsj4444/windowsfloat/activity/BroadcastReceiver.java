package com.bsj4444.windowsfloat.activity;

import android.content.Context;
import android.content.Intent;

import com.bsj4444.windowsfloat.service.FloatWindowService;

/**
 * Created by Administrator on 17.3.12.
 */

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1=new Intent(context,FloatWindowService.class);
        context.startService(intent1);
    }
}
