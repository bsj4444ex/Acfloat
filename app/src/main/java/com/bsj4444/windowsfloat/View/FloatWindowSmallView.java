package com.bsj4444.windowsfloat.View;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsj4444.windowsfloat.R;
import com.bsj4444.windowsfloat.service.FloatWindowService;
import com.bsj4444.windowsfloat.util.Util2;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 17.3.11.
 */
public class FloatWindowSmallView extends LinearLayout {

    public static int viewWidth;
    public static int viewHeight;
    private static int statusBarHeight;
    private WindowManager windowManager;
    private WindowManager.LayoutParams mParams;
    private float xInScreen;
    private float yInScreen;
    private float xDownInScreen;
    private float yDownInScreen;
    private float xInView;
    private float yInView;
    private Context context;

    public FloatWindowSmallView(Context context) {
        super(context);
        this.context=context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window,this);
        View view = findViewById(R.id.small_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        TextView percentView = (TextView) findViewById(R.id.floatView);
        percentView.setText(MyWindowmanager.getUserPercentValue(context)+"%");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("bei2","down");
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY()-getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY()-getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("bei2","move");
                xInScreen = event.getRawX();
                yInScreen = event.getRawY()-getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                Log.d("bei2","up");
                long time=event.getEventTime()-event.getDownTime();
//                if(xDownInScreen == xInScreen&&yDownInScreen==yInScreen){
//                    openBigWindow();
//                }
                Log.d("bei2",time+"");
                if(time<=300){
                    if(Math.abs(xDownInScreen-xInScreen)<5&&Math.abs(yDownInScreen-yInScreen)<5){
                        if (FloatWindowService.isHome){
                            openBigWindow();
                        }
                        else{
                            Intent i= new Intent(Intent.ACTION_MAIN);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addCategory(Intent.CATEGORY_HOME);
                            context.startActivity(i);
                        }
                    }
                }
                else{
                    if (Math.abs(xDownInScreen-xInScreen)<5&&Math.abs(yDownInScreen-yInScreen)<5){
                        Log.d("bei2","longan");
                        Util2.showRecentlyApp();
                    }
                }
                break;
            default:break;
        }
        return true;
    }

    public void setParams(WindowManager.LayoutParams params){
        mParams = params;
    }

    private void updateViewPosition(){
        mParams.x = (int) (xInScreen-xInView);
        mParams.y = (int) (yInScreen-yInView);
        windowManager.updateViewLayout(this,mParams);
    }

    private void openBigWindow(){
        MyWindowmanager.createBigWindow(getContext());
        MyWindowmanager.createSmallWindow(getContext());
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight(){
        if(statusBarHeight == 0){
            try{
                Class<?> c=Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x=(Integer)field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
        //return 0;
    }
}
