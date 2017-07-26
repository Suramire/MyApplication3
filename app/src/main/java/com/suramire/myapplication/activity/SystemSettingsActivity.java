package com.suramire.myapplication.activity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.suramire.myapplication.R;
import com.suramire.myapplication.base.App;
import com.suramire.myapplication.base.BaseActivity;
import com.suramire.myapplication.service.PostService;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.SPUtils;

import butterknife.Bind;
import butterknife.OnClick;

import static java.lang.Integer.parseInt;

/**
 * Created by Suramire on 2017/6/22.
 */

public class SystemSettingsActivity extends BaseActivity {
    @Bind(R.id.sys_ip)
    EditText iptextview;
    @Bind(R.id.button7)
    Button button7;
    @Bind(R.id.sys_postswitch)
    Switch notification;
    @Bind(R.id.sys_postspace)
    EditText sysPostspace;
    @Bind(R.id.sys_port)
    EditText porttextview;
    @Bind(R.id.sys_banner)
    Switch sysBanner;

    private PendingIntent operation;
    private Intent postIntent;
    public final static  int RESULTCODE = 0x10;
    private AlarmManager alarmManager;
    private App mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_systemsettings;
    }

    @Override
    protected void initView() {
        mContext = App.getContext();
        notification.setChecked(Constant.isPostRunning);
        iptextview.setText((String) SPUtils.get( "ip", "10.0.2.2"));
        porttextview.setText((String) SPUtils.get("port", "8080"));
        sysBanner.setChecked((Boolean) SPUtils.get("banner",true));
        sysPostspace.setText((String)SPUtils.get("space","30"));
        postIntent = new Intent(this, PostService.class);

        postIntent.putExtra("space", parseInt(sysPostspace.getText().toString().trim()));
        operation = PendingIntent.getService(SystemSettingsActivity.this, 0, postIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //推送服务开关变化时
        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //定时接收推送内容
                    int space = parseInt(sysPostspace.getText().toString().trim());

                    if(space>0){
                        Toast.makeText(mContext, "已开启推送服务,推送间隔为" + space + "分钟", Toast.LENGTH_SHORT).show();
//                    L.e("开启推送 间隔:" + space);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.currentThreadTimeMillis(), space*1000, operation);
                        Constant.isPostRunning = true;
//                        setResult(RESULTCODE);
//                        finish();
                    }
                } else {
                    Toast.makeText(mContext, "已关闭推送服务", Toast.LENGTH_SHORT).show();
                    Constant.isPostRunning = false;
                    //取消定时推送服务
                    alarmManager.cancel(operation);
                    //清除通知栏的消息
                    NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(0x233);
                }
            }
        });
    }

    @OnClick(R.id.button7)
   public void onViewClicked() {
        String trim1 = sysPostspace.getText().toString().trim();
        String trim = iptextview.getText().toString().trim();
        String trim2 = porttextview.getText().toString().trim();
        if(TextUtils.isEmpty(trim1)|| TextUtils.isEmpty(trim)|| TextUtils.isEmpty(trim2)){
            Toast.makeText(mContext, "选项不可为空", Toast.LENGTH_SHORT).show();
        }else{
            //保存ip
            try {
                SPUtils.put("ip", trim);
                SPUtils.put("port", trim2);
                SPUtils.put("banner", sysBanner.isChecked());
                SPUtils.put("space",trim1);
                Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                setResult(Constant.CHANGEUCCESS);
                finish();
            } catch (Exception e) {
                Log.e("SystemSettingsActivity", "Exception:" + e);
            }
        }


    }
}
