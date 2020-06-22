package com.tksflysun.hi.ui.video;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.tksflysun.hi.R;
import com.tksflysun.hi.tcp.IMsgListener;

import java.util.List;

/**
 * 接受视频聊天
 */
public class VideoReceiveActivity extends AppCompatActivity {
    private Button acceptBtn;
    private long userId;
    private long friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_receive);
        userId = getIntent().getLongExtra("userId", 0);
        friendId = getIntent().getLongExtra("friendId", 0);
        acceptBtn = findViewById(R.id.video_accept_btn);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        VideoChatActivity.class);
                intent.putExtra("isCaller", false);
                intent.putExtra("userId", userId);
                intent.putExtra("friendId", friendId);
                startActivity(intent);
            }
        });
    }


}
