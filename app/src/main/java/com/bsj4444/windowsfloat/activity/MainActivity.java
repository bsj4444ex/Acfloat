package com.bsj4444.windowsfloat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bsj4444.windowsfloat.R;
import com.bsj4444.windowsfloat.View.MyWindowmanager;
import com.bsj4444.windowsfloat.service.FloatWindowService;

/**
 * Created by Administrator on 17.3.11.
 */

public class MainActivity extends Activity {

    private Button setAlpha;
    private EditText alpha;
    private TextView aplhaText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button start = (Button)findViewById(R.id.button);
        setAlpha=(Button)findViewById(R.id.setAlpha);
        alpha = (EditText) findViewById(R.id.alpha);
        aplhaText = (TextView) findViewById(R.id.alphaText);
        aplhaText.setText("当前透明透明度为:"+MyWindowmanager.windowsAlpha*100);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FloatWindowService.class);
                startService(intent);
                //finish();
            }
        });

        setAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s=alpha.getText().toString();
                if ("".equals(s)){
                    aplhaText.setText("不可以为空");
                }
                else {
                    int i=Integer.parseInt(s);
                    if (i>=0&&i<=100){
                        MyWindowmanager.windowsAlpha=(float)i/100;
                        aplhaText.setText("当前透明透明度为:"+i);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
