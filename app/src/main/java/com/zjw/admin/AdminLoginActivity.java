package com.zjw.admin;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.zjw.web.Operaton;

public class AdminLoginActivity extends BaseActivity {
	private EditText mAdminName;
	private EditText mAdminPwd;
	private Button mloginbtn;

	private String adname;
	private String adpwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

    @Override
    protected int getContentViewId() {
        return R.layout.activity_adminlogin;
    }

    @Override
    protected void initView() {
        mAdminName = (EditText) findViewById(R.id.login_edit_admin);
        mAdminPwd = (EditText) findViewById(R.id.admin_pwd);
        mloginbtn = (Button) findViewById(R.id.login_btn_adminlogin);

        mloginbtn.setOnClickListener(mListener);
    }

    OnClickListener mListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			adname = mAdminName.getText().toString().trim();
			adpwd = mAdminPwd.getText().toString().trim();
			new Thread(new Runnable() {

				@Override
				public void run() {
					Operaton operaton = new Operaton();
					String result = operaton.adminlogin("AdminCheck", adname, adpwd);
					Message msg = new Message();
					msg.obj = result;
					handler.sendMessage(msg);
				}
			}).start();

		}

		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				String string = (String) msg.obj;

				Toast.makeText(AdminLoginActivity.this, string, Toast.LENGTH_SHORT).show();
				super.handleMessage(msg);
			}
		};

	};

}
