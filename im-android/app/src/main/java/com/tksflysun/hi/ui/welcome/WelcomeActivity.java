package com.tksflysun.hi.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.tksflysun.hi.R;
import com.tksflysun.hi.bean.User;
import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.ui.login.LogInActivity;
import com.tksflysun.hi.ui.main.MainActivity;

public class WelcomeActivity extends AppCompatActivity {
    //private Button btn1;
    // private Button btn2;
    // private Button btn3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        /** btn1 = findViewById(R.id.rtc1);
         btn2 = findViewById(R.id.rtc2);
         btn3 = findViewById(R.id.newRtc);
         btn1.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(),
        WebRtcActivity.class);
        startActivity(intent);
        }
        });
         btn2.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(),
        WebRtcActivityTest.class);
        startActivity(intent);
        }
        });
         btn3.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        new Handler().postDelayed(new Runnable() {
        @Override public void run() {
        User user = HiApplication.getInstance().getUser();
        String token = null;
        if (user != null) {
        token = user.getToken();
        }

        Log.i("已经存在的token", token + "token");
        if (TextUtils.isEmpty(token)) {
        Intent intent =
        new Intent(getApplicationContext(),
        LogInActivity.class);
        startActivity(intent);
        } else {
        Intent intent =
        new Intent(getApplicationContext(),
        MainActivity.class);
        startActivity(intent);
        }

        }
        }, 3000);
        }
        });**/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = HiApplication.getInstance().getUser();
                String token = null;
                if (user != null) {
                    token = user.getToken();
                }

                Log.i("已经存在的token", token + "token");
                if (TextUtils.isEmpty(token)) {
                    Intent intent = new Intent(getApplicationContext(),
                            LogInActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(),
                            MainActivity.class);
                    startActivity(intent);
                }

            }
        }, 1500);
    }
}
