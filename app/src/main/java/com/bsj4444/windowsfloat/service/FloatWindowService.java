package com.bsj4444.windowsfloat.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bsj4444.windowsfloat.View.MyWindowmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 17.3.11.
 */

public class FloatWindowService extends Service {

    private Handler handler = new Handler();
    private Timer timer;
    public static boolean isHome;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(timer == null){
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(),0,500);//开启定时器，每0.5秒刷新一次
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer=null;
    }

    class RefreshTask extends TimerTask{

        @Override
        public void run() {

            isHome=isHome();

            /**
             * 在桌面，没窗口显示
             * 创建小窗口
             * */
            if (isHome&& !MyWindowmanager.isWindowShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowmanager.createSmallWindow(getApplicationContext());
                    }
                });
            }
            /**
             * 不在桌面，有窗口显示
             *
             * */
            else if(!isHome&&MyWindowmanager.isWindowShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowmanager.updateUsedPercent(getApplicationContext());
                        MyWindowmanager.removeBigWindow(getApplicationContext());
                    }
                });
            }
            /**
             * 不在桌面，没窗口显示
             *
             * */
            else if(!isHome&&!MyWindowmanager.isWindowShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowmanager.createSmallWindow(getApplicationContext());
                    }
                });
            }
            /**
             * 在桌面，有窗口显示
             * 更新小窗口内存百分比
             * */
            else if(isHome&&MyWindowmanager.isWindowShowing()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowmanager.updateUsedPercent(getApplicationContext());
                    }
                });
            }
        }
    }
    /**
     * 判断当前界面是否是桌面
     */
    public boolean isHome(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }
    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    public List<String> getHomes(){
        List<String> names = new ArrayList<>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri:resolveInfo){
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }


}
