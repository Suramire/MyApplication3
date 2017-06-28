package com.zjw.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.suramire.myapplication.MainActivity;
import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.zjw.admin.AdminLoginActivity;
import com.zjw.web.Operaton;

public class LoginActivity extends BaseActivity {
    private EditText mAccount; // 用户名编辑
    private EditText mPwd; // 密码编辑
    private Button mRegisterButton; // 注册按钮
    private Button mLoginButton; // 登录按钮
    private Button madminLoginButton; // 登录按钮
    private CheckBox mRememberCheck;
    private CheckBox mAutoCheck;

    private String userName;
    private String userPwd;

    private boolean choseAutologin;
    private boolean choseRemember;

    private static ProgressDialog dialog;// 提示框
    // private String info;// 返回数据

    private SharedPreferences login_sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 获取控件
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        madminLoginButton = (Button) findViewById(R.id.adminlogin);
        mRememberCheck = (CheckBox) findViewById(R.id.Login_check_Remember);
        mAutoCheck = (CheckBox) findViewById(R.id.Login_check_auto);

        login_sp =this. getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String name = login_sp.getString("username", "");
        String pwd = login_sp.getString("password", "");
        choseRemember = login_sp.getBoolean("ischeck", false);
        choseAutologin = login_sp.getBoolean("autologin", false);

        // 如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if (choseRemember) {

            mRememberCheck.setChecked(true);
            mAccount.setText(name);
            mPwd.setText(pwd);
        }
        // 如果上次登录选了自动登录，那进入登录页面也自动勾选自动登录
        if (choseAutologin) {
            mAutoCheck.setChecked(true);
            Intent autoIntent=new Intent(LoginActivity.this, MainActivity.class);
            startActivity(autoIntent);
            finish();
        }

        // 三个监听
        mRegisterButton.setOnClickListener(mListener);
        mLoginButton.setOnClickListener(mListener);
        madminLoginButton.setOnClickListener(mListener);
        mRememberCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (!choseAutologin) {
                    Editor editor=login_sp.edit();
                    editor.putString("username", userName);
                    editor.putString("password", userPwd);
                    editor.commit();
                }
            }
        } );
        mAutoCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (!choseRemember) {
                    Editor editor=login_sp.edit();
                    editor.putString("username", userName);
                    editor.putString("password", userPwd);
                    editor.commit();
                }
            }
        });

    }


    OnClickListener mListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn_register: // 注册按钮
                    Intent Register = new Intent(LoginActivity.this,
                            RegisterActivity.class);
                    startActivity(Register);
                    finish();
                    break;
                case R.id.login_btn_login: // 登录按钮
                    if (!checkNetwork()) {
                        Toast toast = Toast.makeText(LoginActivity.this, "网络未连接",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("正在登陆，请稍后...");
                    dialog.setCancelable(false);
                    login();

                    break;
                case R.id.adminlogin: // 管理员登录按钮
                    Intent admin = new Intent(LoginActivity.this,
                            AdminLoginActivity.class);
                    startActivity(admin);
                    finish();
                    break;

                default:
                    break;
            }

        }

        public void login() {
            userName = mAccount.getText().toString().trim();
            userPwd = mPwd.getText().toString().trim();
            if (isUserNameAndPwdValid()) {

                dialog.show();
                new Thread(new Runnable() {

                    public void run() {
                        Operaton operaton = new Operaton();
                        String result = operaton.login("CheckLogin", userName,
                                userPwd);
                        Message msg = new Message();
                        msg.obj = result;
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        }

        Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                String string = (String) msg.obj;
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, string, Toast.LENGTH_SHORT).show();
                super.handleMessage(msg);
            }
        };

    };


    // 检测网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    // 检测用户名，密码
    public boolean isUserNameAndPwdValid() {
        // String userName = mAccount.getText().toString().trim(); //
        // String userPwd = mPwd.getText().toString().trim();
        if (userName == null || userName.length() <= 0) {
            // Toast.makeText(this, "用户名为空，请重新输入！", Toast.LENGTH_SHORT).show();
            mAccount.requestFocus();
            mAccount.setError("用户名不能为空");
            return false;
        } else if (userPwd.equals("")) {
            // Toast.makeText(this, "密码为空，请重新输入!", Toast.LENGTH_SHORT).show();
            mPwd.requestFocus();
            mPwd.setError("密码不能为空");
            return false;
        }
        return true;
    }
}