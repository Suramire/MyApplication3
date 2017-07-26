package com.zjw.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.zjw.bean.UserData;
import com.zjw.json.WriteJson;
import com.zjw.web.Operaton;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends BaseActivity {
    private EditText mAccount; // 用户名编辑
    private EditText mPwd; // 密码编辑
    private EditText mrePwd; // 密码编辑
    private EditText mhobby;
    private EditText mcharacter;
    private Button mSureButton; // 确定按钮
    private Button mCancelButton; // 取消按钮
    ProgressDialog dialog;
    private String userName = null;
    private String userPwd = null;
    private String userrepwd = null;
    private String userhobby = null;
    private String usercharacter = null;
    String jsonString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;

    }

    @Override
    protected void initView() {
        mAccount = (EditText) findViewById(R.id.register_edit_account);
        mPwd = (EditText) findViewById(R.id.register_edit_pwd);
        mrePwd = (EditText) findViewById(R.id.register_edit_repwd);
        mhobby = (EditText) findViewById(R.id.register_edit_hobby);
        mcharacter = (EditText) findViewById(R.id.register_edit_charact);

        mSureButton = (Button) findViewById(R.id.register_btn_submint);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancle);

        dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setTitle("正在注册中");
        dialog.setMessage("请稍等...");

        mAccount.setOnFocusChangeListener(new EtusernameOnFocusChange());

        mSureButton.setOnClickListener(mListener); // 注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(mListener);
    }

    View.OnClickListener mListener = new View.OnClickListener() { // 不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_submint: // 确认按钮，提交信息
                    register_check();
                    break;
                case R.id.register_btn_cancle: // 取消按钮的监听事件,由注册界面返回登录界面
                    Intent intent_Register_to_Login = new Intent(
                            RegisterActivity.this, LoginActivity.class);
                    startActivity(intent_Register_to_Login);
                    finish();
                    break;
            }
        }

        // 注册实现
        private void register_check() {

            if (isUserNameAndPwdValid()) {
                dialog.show();
                new Thread(new Runnable() {

                    public void run() {

                        Operaton operaton = new Operaton();

                        UserData user = new UserData(userName, userPwd,
                                userhobby, usercharacter);
                        // 构造一个user对象
                        List<UserData> list = new ArrayList<UserData>();
                        list.add(user);
                        WriteJson writeJson = new WriteJson();
                        // 将user对象写出json形式字符串
                        jsonString = writeJson.getJsonData(list);
                        System.out.println(jsonString);
                        String result = operaton.UpData("CheckLogin", jsonString);
                        Message msg = new Message();
                        System.out.println("result---->" + result);
                        msg.obj = result;
                        handler1.sendMessage(msg);
                    }
                }).start();
            }

        }

        Handler handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                dialog.dismiss();
                String msgobj = msg.obj.toString();
                if (msgobj.equals("t")) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
    };

    private class EtusernameOnFocusChange implements OnFocusChangeListener {
        public void onFocusChange(View v, boolean hasFocus) {
            if (!mAccount.hasFocus()) {
                new Thread(new Runnable() {
                    // 如果用户名不为空，那么将用户名提交到服务器上进行验证，看用户名是否存在
                    public void run() {
                        Operaton operaton = new Operaton();
                        String result = operaton.checkusername("CheckName",
                                userName);
                        System.out.println("result:" + result);
                        Message message = new Message();
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                }).start();
            }

        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String msgobj = msg.obj.toString();
            System.out.println(msgobj);
            System.out.println(msgobj.length());

            if (msgobj.equals("t")) {
                mAccount.requestFocus();
                mAccount.setError("用户名" + userName + "已存在");
            } else {
                mPwd.requestFocus();
            }
            super.handleMessage(msg);
        }
    };

    private boolean isUserNameAndPwdValid() {
        userName = mAccount.getText().toString().trim();
        userPwd = mPwd.getText().toString().trim();
        userrepwd = mrePwd.getText().toString().trim();
        userhobby = mhobby.getText().toString().trim();
        usercharacter = mcharacter.getText().toString().trim();

        if (userPwd.equals("")) {
            // Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            mPwd.requestFocus();
            mPwd.setError("密码不能为空");
            return false;
        } else if (!userPwd.equals(userrepwd)) {
            mrePwd.requestFocus();
            mrePwd.setError("两次输入密码不一致，请重新输入！");

            return false;
        } else if (userhobby.equals("")) {
            // Toast.makeText(this, "兴趣不能为空", Toast.LENGTH_SHORT).show();
            mhobby.requestFocus();
            mhobby.setError("兴趣不能为空");
            return false;
        } else if (usercharacter.equals("")) {
            // Toast.makeText(this, "爱好不能为空", Toast.LENGTH_SHORT).show();
            mcharacter.requestFocus();
            mcharacter.setError("兴趣不能为空");
            return false;
        }
        return true;
    }

}
