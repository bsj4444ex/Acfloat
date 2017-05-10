package com.bsj4444.windowsfloat.View;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsj4444.windowsfloat.R;
import com.bsj4444.windowsfloat.service.FloatWindowService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 17.3.11.
 */

public class MyWindowmanager {

    private static FloatWindowSmallView smallWindow;
    private static FloatWindowBigView bigWindow;
    private static WindowManager.LayoutParams smallWindowParams;
    private static WindowManager.LayoutParams bigWindowParams;
    private static WindowManager mWindowManager;
    private static ActivityManager mActivityManager;
    private static PackageManager mPackageManager;

    private static int[] acImage={
                R.drawable.ac_1,R.drawable.ac_2,R.drawable.ac_3,R.drawable.ac_4,
                R.drawable.ac_5,R.drawable.ac_6,R.drawable.ac_7,R.drawable.ac_8,
                R.drawable.ac_9,R.drawable.ac_10,R.drawable.ac_11,R.drawable.ac_12,
                R.drawable.ac_13,R.drawable.ac_14,R.drawable.ac_15,R.drawable.ac_16,
                R.drawable.ac_17,R.drawable.ac_18,R.drawable.ac_19,R.drawable.ac_20,
                R.drawable.ac_21,R.drawable.ac_22,R.drawable.ac_23,R.drawable.ac_24,
                R.drawable.ac_25,R.drawable.ac_26,R.drawable.ac_27,R.drawable.ac_28
        };

    public static void createSmallWindow(Context context){
        WindowManager windowManager = getmWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if(smallWindow == null){
            smallWindow = new FloatWindowSmallView(context);
            if (smallWindowParams==null){
                smallWindowParams = new WindowManager.LayoutParams();
                smallWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                smallWindowParams.gravity= Gravity.LEFT|Gravity.TOP;
                smallWindowParams.width=FloatWindowSmallView.viewWidth;
                smallWindowParams.height=FloatWindowSmallView.viewHeight;
                smallWindowParams.x=screenWidth;
                smallWindowParams.y=screenHeight/2;
            }
            smallWindow.setParams(smallWindowParams);
            windowManager.addView(smallWindow,smallWindowParams);
        }
    }

    public static void createBigWindow(Context context){
        WindowManager windowManager = getmWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if(bigWindow == null){
            bigWindow = new FloatWindowBigView(context);
            if (bigWindowParams==null){
                bigWindowParams = new WindowManager.LayoutParams();
                bigWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                bigWindowParams.format = PixelFormat.RGBA_8888;
                bigWindowParams.gravity= Gravity.LEFT|Gravity.TOP;
                bigWindowParams.width=FloatWindowBigView.viewWidth;
                bigWindowParams.height=FloatWindowBigView.viewHeight;
                bigWindowParams.x=screenWidth/2-FloatWindowBigView.viewWidth/2;
                bigWindowParams.y=screenHeight/2-FloatWindowBigView.viewHeight/2;
            }
            //smallWindow.setParams(smallWindowParams);
            windowManager.addView(bigWindow,bigWindowParams);
        }
    }

    private static WindowManager getmWindowManager(Context context){
        if(mWindowManager==null){
            mWindowManager = (WindowManager) context.getSystemService(
                    Context.WINDOW_SERVICE
            );
        }
        return mWindowManager;
    }

    private static ActivityManager getmActivityManager(Context context){
        if(mActivityManager==null){
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    private static PackageManager getmPackageManager(Context context){
        if(mPackageManager==null){
            mPackageManager =  context.getPackageManager();
        }
        return mPackageManager;
    }

    public static int getUserPercentValue(Context context){
        String dir = "/proc/meminfo";
        try{
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr,2048);
            String memoryLine = br.readLine();
            String subMemoruLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoruLine.replaceAll("\\D+",""));
            long availableSize = getAvailableMemory(context)/1024;
            int percent = (int) ((totalMemorySize-availableSize)/(float)totalMemorySize*100);
            return percent;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static void removeBigWindow(Context context){
        if(bigWindow!=null){
            WindowManager windowManager = getmWindowManager(context);
            windowManager.removeView(bigWindow);
            bigWindow=null;
        }
    }
    public static void removeSmallWindow(Context context){
        if (smallWindow!=null){
            WindowManager windowManager = getmWindowManager(context);
            windowManager.removeView(smallWindow);
            smallWindow=null;
        }
    }

    private static long getAvailableMemory(Context context){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getmActivityManager(context).getMemoryInfo(mi);
        return mi.availMem;
    }

    public static boolean isWindowShowing(){
        return smallWindow!=null||bigWindow!=null;
    }

    public static void updateUsedPercent(Context context){
        if(smallWindow!=null){
            int precent=getUserPercentValue(context);
            TextView percentView = (TextView)smallWindow.findViewById(R.id.floatView);
            ImageView imageView = (ImageView) smallWindow.findViewById(R.id.image);
            if(precent==0){
                percentView.setText("悬浮窗");
            }else{
                percentView.setText(precent+"%");
            }
            if (precent<72||precent==100){
                imageView.setImageResource(acImage[0]);
            }
            else{
                imageView.setImageResource(acImage[precent-72]);
            }
            //当不在桌面时
            if (!FloatWindowService.isHome){
                percentView.setVisibility(View.GONE);
                imageView.setAlpha(0.2f);
            }
            else{
                percentView.setVisibility(View.VISIBLE);
                imageView.setAlpha(1f);
            }
        }
    }

    public static List<String> getRunningProcessName(Context context){
        List<String> list=new ArrayList<>();
        ActivityManager manager=getmActivityManager(context);
        for (int i=0;i<manager.getRunningAppProcesses().size();i++){
            String processName = manager.getRunningAppProcesses().get(i).processName;
            if(processName!=null) {
                Log.d("beiledeng",processName);
                list.add(processName);
            }
        }
        return list;
    }

    public static List<Drawable> getIconList(Context context){
        List<String> packageName = getRunningProcessName(context);
        PackageManager pm = getmPackageManager(context);
        List<Drawable> iconList=new ArrayList<>();
        for (int i=0;i<packageName.size();i++){
            try {
                Drawable icon = pm.getApplicationIcon(packageName.get(i));
                iconList.add(icon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return iconList;
    }
}
