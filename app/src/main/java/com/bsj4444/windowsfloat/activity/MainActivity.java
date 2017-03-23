package com.bsj4444.windowsfloat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.bsj4444.windowsfloat.R;
import com.bsj4444.windowsfloat.service.FloatWindowService;

/**
 * Created by Administrator on 17.3.11.
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button start = (Button)findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FloatWindowService.class);
                startService(intent);
                finish();
            }
        });
    }
}
