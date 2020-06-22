package com.tksflysun.hi.ui.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.tksflysun.hi.R;
import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.service.TcpService;
import com.tksflysun.hi.ui.main.fragement.friendlist.FriendListFragment;
import com.tksflysun.hi.ui.main.fragement.messagelist.MessageListFragment;
import com.tksflysun.hi.ui.main.fragement.myinfo.MyInfoFragment;


/*
 * FragmentManager manager = getSupportFragmentManager();
 * FragmentTransaction transaction = manager.beginTransaction();
 * transaction.add(R.id.main_body,new MessageListFragment()).commit();
 * */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    //来自main_title_bar.xml
    private TextView tv_main_title;//标题
    private TextView tv_back;//返回按钮
    private RelativeLayout title_bar;// android:id="@+id/title_bar"
    //来自activity_main.xml
    private RelativeLayout main_body;
    private TextView bottom_fragement_text_chat_list;
    private ImageView bottom_fragement_image_chat_list;
    private TextView bottom_fragement_text_friend_list;
    private ImageView bottom_fragement_image_friend_list;
    private TextView bottom_fragement_text_myinfo;
    private ImageView bottom_fragement_image_myinfo;
    private LinearLayout main_bottom_bar;
    private RelativeLayout bottom_fragement_chat_list_btn;
    private RelativeLayout bottom_fragement_friend_list_btn;
    private RelativeLayout bottom_fragement_myinfo_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initNavigation();
        initBody();
        initBottomBar();
        Intent intent = getIntent();
        int initStatus = intent.getIntExtra("initStatus", 1);
        setInitStatus(initStatus);
        Intent tcpService = new Intent(this, TcpService.class);
        startService(tcpService);
    }

    private void initNavigation() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
    }

    private void initBody() {
        main_body = (RelativeLayout) findViewById(R.id.main_body);
    }

    private void initBottomBar() {
        bottom_fragement_text_chat_list =
                (TextView) findViewById(R.id.bottom_fragement_text_chat_list);
        bottom_fragement_image_chat_list =
                (ImageView) findViewById(R.id.bottom_fragement_image_chat_list);
        bottom_fragement_chat_list_btn =
                (RelativeLayout) findViewById(R.id.bottom_fragement_chat_list_btn);
        bottom_fragement_text_friend_list =
                (TextView) findViewById(R.id.bottom_fragement_text_friend_list);
        bottom_fragement_image_friend_list =
                (ImageView) findViewById(R.id.bottom_fragement_image_friend_list);
        bottom_fragement_friend_list_btn =
                (RelativeLayout) findViewById(R.id.bottom_fragement_friend_list_btn);
        bottom_fragement_text_myinfo =
                (TextView) findViewById(R.id.bottom_fragement_text_myinfo);
        bottom_fragement_image_myinfo =
                (ImageView) findViewById(R.id.bottom_fragement_image_myinfo);
        bottom_fragement_myinfo_btn =
                (RelativeLayout) findViewById(R.id.bottom_fragement_myinfo_btn);
        main_bottom_bar = (LinearLayout) findViewById(R.id.main_bottom_bar);
        setListener();
    }

    private void setListener() {
        for (int i = 0; i < main_bottom_bar.getChildCount(); i++) {
            main_bottom_bar.getChildAt(i).setOnClickListener(this);
        }
    }

    private void setInitStatus(int status) {
        clearBottomImageState();
        setSelectedStatus(status);
        if (status == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_body, new MessageListFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.main_body
                    , new FriendListFragment()).commit();
        }


    }

    private void setSelectedStatus(int index) {
        switch (index) {
            case 0:
                //mCourseBtn.setSelected(true);
                bottom_fragement_image_chat_list.setImageResource(R.drawable.message_press);
                bottom_fragement_text_chat_list.setTextColor(Color.parseColor("#0097f7"));
                tv_main_title.setText("嗨");
                break;
            case 1:
                //mExercisesBtn.setSelected(true);
                bottom_fragement_image_friend_list.setImageResource(R.drawable.contacts_press);
                bottom_fragement_text_friend_list.setTextColor(Color.parseColor("#0097f7"));
                tv_main_title.setText("好友");

                break;
            case 2:
                //mMyInfoBtn.setSelected(true);
                bottom_fragement_image_myinfo.setImageResource(R.drawable.me_press);
                bottom_fragement_text_myinfo.setTextColor(Color.parseColor(
                        "#0097f7"));
                title_bar.setVisibility(View.VISIBLE);
                tv_main_title.setText("我");
                break;
        }
    }

    private void clearBottomImageState() {
        bottom_fragement_text_chat_list.setTextColor(Color.parseColor(
                "#666666"));
        bottom_fragement_text_friend_list.setTextColor(Color.parseColor(
                "#666666"));
        bottom_fragement_text_myinfo.setTextColor(Color.parseColor("#666666"));
        bottom_fragement_image_chat_list.setImageResource(R.drawable.message_normal);
        bottom_fragement_image_friend_list.setImageResource(R.drawable.contacts_normal);
        bottom_fragement_image_myinfo.setImageResource(R.drawable.me_normal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_fragement_chat_list_btn:
                clearBottomImageState();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body, new MessageListFragment()).commit();
                setSelectedStatus(0);
                break;
            case R.id.bottom_fragement_friend_list_btn:
                clearBottomImageState();
                setSelectedStatus(1);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body, new FriendListFragment()).commit();
                break;
            case R.id.bottom_fragement_myinfo_btn:
                clearBottomImageState();
                setSelectedStatus(2);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_body, new MyInfoFragment()).commit();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }
}