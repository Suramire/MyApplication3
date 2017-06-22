package com.suramire.myapplication.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.suramire.myapplication.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Suramire on 2017/6/22.
 */

public class SystemSettingsActivity extends AppCompatActivity {
    @Bind(R.id.editText2)
    EditText editText2;
    @Bind(R.id.button7)
    Button button7;
    private SharedPreferences system;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemsettings);
        ButterKnife.bind(this);
        system = getSharedPreferences("system", MODE_PRIVATE);
        String ip = system.getString("ip", "192.168.1.102");
        editText2.setText(ip);
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
