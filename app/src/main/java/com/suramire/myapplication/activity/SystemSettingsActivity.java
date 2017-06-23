package com.suramire.myapplication.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.suramire.myapplication.R;
import com.suramire.myapplication.service.PostService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Suramire on 2017/6/22.
 */

public class SystemSettingsActivity extends AppCompatActivity {
    @Bind(R.id.sys_ip)
    EditText editText2;
    @Bind(R.id.button7)
    Button button7;
    @Bind(R.id.sys_postswitch)
    Switch switch1;
    @Bind(R.id.sys_postspace)
    EditText sysPostspace;
    private SharedPreferences system;
    private PendingIntent operation;
    private Intent postIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemsettings);
        ButterKnife.bind(this);
        system = getSharedPreferences("system", MODE_PRIVATE);
        String ip = system.getString("ip", "192.168.1.102");
        editText2.setText(ip);
        postIntent = new Intent(this, PostService.class);


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private AlarmManager alarmManager;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //定时接收推送内容
                    postIntent.putExtra("space",Integer.parseInt(sysPostspace.getText().toString().trim()));
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
            SharedPreferences.Editor edit = system.edit();
            edit.putString("ip", editText2.getText().toString().trim());
            edit.commit();
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("SystemSettingsActivity", "Exception:" + e);
        }

    }
}
