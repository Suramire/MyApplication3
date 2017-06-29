package com.zxf.scode;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.DateUtil;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.L;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;


public class Information extends BaseActivity{
	private EditText ausername;
	private RadioGroup asex;
	private EditText abirth;
	private EditText aemail;
	private EditText aphone;
	private EditText ahobbyone;
	private EditText ahobbytwo;
	private EditText ahobbythree;
	private EditText apersonality;
//	private Button back;
//	private Button save;
	ProgressDialog dialog;
	private String username=null;
	private String sex="男";
	private String birth=null;//日期转换在服务端进行
	private String email=null;
	private String phone=null;
	private String hobbyone=null;
	private String hobbytwo=null;
	private String hobbythree=null;
	private String personality=null;
	String jsonString = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);
//		int uid =getIntent().getIntExtra("uid", 0);


		ausername=(EditText) findViewById(R.id.username);
		asex=(RadioGroup) findViewById(R.id.sex);
        // TODO: 2017/6/29  日期格式转换
        abirth=(EditText) findViewById(R.id.birth);
		aemail=(EditText)findViewById(R.id.email);
		aphone=(EditText)findViewById(R.id.phone);
		ahobbyone=(EditText)findViewById(R.id.hobbyone);
		ahobbytwo=(EditText)findViewById(R.id.hobbytwo);
		ahobbythree=(EditText)findViewById(R.id.hobbythree);
		apersonality=(EditText)findViewById(R.id.personality);
//		back=(Button)findViewById(R.id.back);
//		save=(Button)findViewById(R.id.save);
        User mUser = Constant.user;
        ausername.setText(mUser.getUsername());
        if(mUser.getSex().equals("男")){
            findViewById(R.id.radio0).setSelected(true);
        }
        abirth.setText(DateUtil.dateToString(mUser.getBirth()));
        aemail.setText(mUser.getEmail());
        aphone.setText(mUser.getPhone());
        ahobbyone.setText(mUser.getHobbyone());
        apersonality.setText(mUser.getPersonality());
        //选择的性别判断
        asex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
               if(i==R.id.radio0){
                   sex = "男";
               }else{
                   sex = "女";
               }
                L.e("性别:" + sex);
            }
        });
		
		dialog = new ProgressDialog(Information.this);
		dialog.setTitle("正在保存中");
		dialog.setMessage("请稍等...");
//		save.setOnClickListener(onClickListener);
		//mSureButton.setOnClickListener(mListener);
	}
//		    View.OnClickListener onClickListener=new View.OnClickListener(){
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				switch (v.getId()) {
//				case R.id.back:
////					Intent backintent=new Intent(Information.this,MyInformation.class);
////					startActivity(backintent);
////					finish();
//					break;
//				case R.id.save:
//					save_check();
//					break;
//				default:
//					break;
//				}
//			}
//		};
//实现保存             
private void save_check() {
    int uid = (int) SPUtils.get(Information.this,"uid",0);
                            String urlString =Constant.BASEURL+ "bbs/Update?do=update&username="+username
            +"&sex="+sex+"&birth="+birth+"&email="+email+"&phone="+phone
            +"&hobbyone="+hobbyone+"&personality="+personality+"&uid="+uid;

    L.e("url"+urlString);
    HTTPUtil.getCall(urlString, new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(Response response) throws IOException {
            final String string = response.body().string();
            L.e("修改信息结果"+string);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!TextUtils.isEmpty(string)){
                        if(string.equals("1")){
                            Toast.makeText(Information.this, "修改成功,请重新登录", Toast.LENGTH_SHORT).show();
                            setResult(Constant.UPDATESUCCESS);
                            SPUtils.put(Information.this,"uid",0);
                            finish();
                        }else{
                            Toast.makeText(Information.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        }
    });
//				new Thread(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if(ishaveneweinformation()){
//                            try {
//
//
//                                URL url = new URL(urlString);
//							HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//							Log.e("ResponseCode", urlConnection.getResponseCode()+"");
//							InputStream is =urlConnection.getInputStream();
//			                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			                byte[] buffer = new byte[1024];
//			                int len = 0;
//			                while(-1 != (len = is.read(buffer))){
//			                    baos.write(buffer,0,len);
//			                    baos.flush();
//			                }
//			                    Log.e("result",baos.toString("utf-8") );
//                                runOnUiThread( new Runnable() {
//                                    public void run() {
//                                        try {
//                                            if(baos.toString("utf-8").equals("1")){
//                                                Toast.makeText(Information.this, "修改成功,请重新登录", Toast.LENGTH_SHORT).show();
//                                                setResult(Constant.UPDATESUCCESS);
//                                                finish();
//                                            }
//
//
//                                            else{
//                                                Toast.makeText(Information.this, "修改失败", Toast.LENGTH_SHORT).show();
//                                            }
//                                        } catch (UnsupportedEncodingException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
//                            }catch (IOException ie){
//                                Toast.makeText(Information.this, "修改失败,请检查字段格式", Toast.LENGTH_SHORT).show();
//                            }
//                             catch (final Exception e) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(Information.this, "修改失败"+ e, Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
////							Log.e("error",e+"");
////						}
//						}
//
////						String urlString = "http://10.0.2.2:8090/android_service/Update?username="+usernameString+"&uid="+1;
//
//
////						URL url;
////
//
//////							urlConnection.connect();
//
//					}
//				}).start();
				
				//實現查詢
				
				// TODO Auto-generated method stub
//				if(ishaveneweinformation()){
//					dialog.show();
//					new Thread(new Runnable() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							ModifyOperaton modifyOperaton = new ModifyOperaton();
//							User user=new User(username,sex,birth,email,phone,hobbyone,hobbytwo,hobbythree,personality);
//							List<User> list=new ArrayList<User>();
//							list.add(user);
//							ModifyWriteJson modifyWriteJson=new ModifyWriteJson();
//							jsonString = modifyWriteJson.getJsonData(list);
//							System.out.println(jsonString);
//							String result = modifyOperaton.UpData("Checksave", jsonString);
//							Message msg = new Message();
//							System.out.println("result---->" + result);
//							msg.obj = result;
//							handler.sendMessage(msg);
//							
//						}
//					}).start();
//				}
			}
//			Handler handler = new Handler() {
//				@Override
//				public void handleMessage(Message msg) {
//					dialog.dismiss();
//					String msgobj = msg.obj.toString();
//					if (msgobj.equals("ok")) {
//						Toast.makeText(Information.this, "保存成功", Toast.LENGTH_LONG).show();
//
////						Intent intent = new Intent();
////						intent.setClass(Information.this, MyInformation.class);
////						startActivity(intent);
//					} else {
//						Toast.makeText(Information.this, "保存失败", Toast.LENGTH_LONG).show();
//					}
//					super.handleMessage(msg);
//				}
//			};
//			Handler handler1= new Handler() {
//				@Override
//				public void handleMessage(Message msg) {
//					String msgobj = msg.obj.toString();
//					System.out.println(msgobj);
//					System.out.println(msgobj.length());
//
//					if (msgobj.equals("ok")) {
////						save.requestFocus();
////						save.setError("已保存");
//					}
//					super.handleMessage(msg);
//				}
//			};
			

		private boolean ishaveneweinformation()  {
			// TODO Auto-generated method stub 
			username=ausername.getText().toString().trim();

			//birth
			email=aemail.getText().toString().trim();
			phone=aphone.getText().toString().trim();
			hobbyone=ahobbyone.getText().toString().trim();
			hobbytwo=ahobbytwo.getText().toString().trim();
			hobbythree=ahobbythree.getText().toString().trim();
			personality=apersonality.getText().toString().trim();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            birth =abirth.getText().toString().trim();
            if (username.equals("")) {
				ausername.requestFocus();
				ausername.setError("用户名不能为空");
				return false;
			}
					return true;
		}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            if(ishaveneweinformation()){
                save_check();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
		
