package com.suramire.myapplication.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.suramire.myapplication.service.PostService;
import com.suramire.myapplication.util.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Suramire on 2017/6/22.
 */

public class SystemSettingsActivity extends BaseActivity {
    @Bind(R.id.sys_ip)
    EditText iptextview;
    @Bind(R.id.button7)
    Button button7;
    @Bind(R.id.sys_postswitch)
    Switch switch1;
    @Bind(R.id.sys_postspace)
    EditText sysPostspace;
    @Bind(R.id.sys_port)
    EditText porttextview;
    @Bind(R.id.sys_banner)
    Switch sysBanner;

    private PendingIntent operation;
    private Intent postIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemsettings);
        ButterKnife.bind(this);

        iptextview.setText((String) SPUtils.get(this, "ip", "10.0.2.2"));
        porttextview.setText((String) SPUtils.get(this, "port", "8080"));
        sysBanner.setChecked((Boolean) SPUtils.get(this,"banner",true));
        postIntent = new Intent(this, PostService.class);


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private AlarmManager alarmManager;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //定时接收推送内容
                    postIntent.putExtra("space", Integer.parseInt(sysPostspace.getText().toString().trim()));
                    operation = PendingIntent.getService(SystemSettingsActivity.this, 0, postIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC, SystemClock.currentThreadTimeMillis(), 3000, operation);

                } else {
                    alarmManager.cancel(operation);
                }
            }
        });
    }

    @OnClick(R.id.button7)
   public void onViewClicked() {
        //保存ip
        try {
            SPUtils.put(this, "ip", iptextview.getText().toString().trim());
            SPUtils.put(this, "port", porttextview.getText().toString().trim());
            SPUtils.put(this, "banner", sysBanner.isChecked());
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("SystemSettingsActivity", "Exception:" + e);
        }

    }
}
