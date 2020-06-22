package com.tksflysun.hi.ui.main.fragement.myinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.tksflysun.hi.R;
import com.tksflysun.hi.common.HiApplication;
import com.tksflysun.hi.tcp.server.TcpServer;
import com.tksflysun.hi.ui.login.LogInActivity;


public class MyInfoFragment extends Fragment {

    private ImageView iv_head_icon;//
    private TextView tv_user_name;
    private LinearLayout ll_head;
    private RelativeLayout rl_course_history;
    private RelativeLayout rl_setting;
    private TextView loginOutBtn;
    private TextView cleanCacheBtn;

    //private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myinfo, null);
        loginOutBtn = view.findViewById(R.id.login_out_btn);
        loginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LogInActivity.class);
                TcpServer.close();
                getContext().startActivity(intent);
            }
        });
        cleanCacheBtn = view.findViewById(R.id.clean_cache_btn);
        cleanCacheBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HiApplication) getActivity().getApplication()).cleanCache();
            }
        });
        return view;
    }


}
