package com.tksflysun.hi.ui.video;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.tksflysun.hi.R;

/**
 * 发送视频聊天
 */
public class VideoSendActivity  extends AppCompatActivity {
    private Button endVideoBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_send);
        endVideoBtn=findViewById(R.id.end_video_btn);
        endVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
