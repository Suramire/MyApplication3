package com.zlw;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zlw on 2017/6/25.
 */

public class Operation extends BaseActivity {
//    private String path= Constant.BASEURL+"android_service/GetData";
    private EditText ed_title,ed_content,ed_type;
    private CheckBox cb;
    private Spinner spinner;
    private Note note=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_layout);

        initView();

        //判断共享是否被选中
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    note.setIsshare(true);
                    Log.i("check","true");

                }else{
                    note.setIsshare(false);
                    Log.i("check","false");

                }
            }
        });

        final List<String> list=new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.types);
        for (int i = 0; i < stringArray.length; i++) {
            list.add(stringArray[i]);
        }
        ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data = list.get(i);
                note.setType(data);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner.setAdapter(adapter);


    }

    public  void initView() {
        note=new Note();
        ed_content = (EditText) findViewById(R.id.id_content);
        ed_title = (EditText) findViewById(R.id.id_title);
        ed_type = (EditText) findViewById(R.id.ed_type);
        cb= (CheckBox) findViewById(R.id.checkBox);
        spinner= (Spinner) findViewById(R.id.id_spinner);

    }



    /**
     * 点击发布按钮
     *
     */
    public void release(){



        init(note);


        Gson gson=new Gson();
        String toJson = gson.toJson(note);
        Log.i("flag",toJson);
//        Toast.makeText(this,toJson, Toast.LENGTH_SHORT).show();
        OkHttpClient okHttpClient=new OkHttpClient();


        Request.Builder builder = new Request.Builder();
//        RequestBody requestBody = new FormEncodingBuilder().add("note",toJson).add("flag","add").build();
//
//        Request request = builder.post(requestBody).url(path).addHeader("content-type","application/json:charset:utf-8").build();
        Request request = builder.get().url(Constant.URL+"add&note="+toJson).addHeader("content-type","application/json:charset:utf-8").build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

        }

            @Override
            public void onResponse(Response response) throws IOException {


                String result = response.body().string();
                Log.i("flag",result);

            }
        });
    }

    /**
     * 初始化Note
     * @param note
     */
    private void init(Note note) {
        String current = formatDate();
        note.setTitle(ed_title.getText().toString());
        note.setContent(ed_content.getText().toString());
//        note.setPublishTime(current);
        note.setPublishtime(new Date());
        note.setEdittime(new Date());
        note.setViewtime(new Date());
        note.setCount(0);
        note.setUid((int)SPUtils.get(Operation.this,"uid",0));
        note.setTag(ed_type.getText().toString().trim());
        note.setIsshare(cb.isChecked());
//        note.setEditTime(current);
//        note.setViewTime(current);


//        Toast.makeText(this,current, Toast.LENGTH_SHORT).show();
    }

    /**
     * 格式化当前时间
     */
    public static String formatDate(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = sDateFormat.format(new Date(System.currentTimeMillis()));

        Log.i("time",format);
        return  format;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_done){
            release();
        }
        return super.onOptionsItemSelected(item);
    }
}
