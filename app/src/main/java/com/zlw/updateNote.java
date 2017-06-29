package com.zlw;

import android.content.Intent;
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
import com.suramire.myapplication.util.L;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zlw on 2017/6/26.
 */

public class updateNote extends BaseActivity {

//    private String path= Constant.BASEURL+"android_service/GetData";
    private EditText ed_title,ed_content,ed_type;
    private CheckBox cb;
    private Spinner spinner;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_layout);
        initView();
        initdata();
    }

    private void initdata() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        note = (Note) bundle.getSerializable("note");
        ed_title.setText(note.getTitle());
//        Toast.makeText(this,"id="+note.getNid()+"=="+note.getTitle()+"===="+note.getContent(), Toast.LENGTH_SHORT).show();

        ed_content.setText(note.getContent());

//        Toast.makeText(this,"id="+note.getNid()+"=="+note.getTitle()+"===="+note.getContent(),Toast.LENGTH_SHORT).show();

        ed_content.setText(note.getContent());
        ed_type.setText(note.getType());
        //从数据库中取得isshare
        if(note.getIsshare()){
            cb.setChecked(true);
        }else{
            cb.setChecked(false);
        }
        spinner.setSelection(Integer.parseInt(note.getType()));

        //判断共享是否被选中
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    note.setIsshare(true);
                    Log.i("check:",note.getIsshare()+"");

                }else{
                    note.setIsshare(false);
                    Log.i("check:",note.getIsshare()+"");

                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data =String.valueOf(i+1);
                note.setType(data);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * 修改帖子
     * @param
     */
    public void updateNote(){


        init(note);


        Gson gson=new Gson();
        String toJson = gson.toJson(note);
//        Log.i("flag",toJson);
//        Toast.makeText(this,toJson, Toast.LENGTH_SHORT).show();
        OkHttpClient okHttpClient=new OkHttpClient();

        Request.Builder builder = new Request.Builder();
//        RequestBody requestBody = new FormEncodingBuilder().add("note",toJson).add("flag","update").build();
//        Request request = builder.post(requestBody).url(path).build();
        Request request = builder.get().url(Constant.URL+"update&note="+toJson).build();
        L.e("JSON:" + toJson);

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                L.e("更新result"+e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result = response.body().string();
                L.e("更新帖子结果:" + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result.equals("true")){
                            Toast.makeText(updateNote.this, "修改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(updateNote.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


    }


    private void init(Note note) {
        String current = Operation.formatDate();
        note.setTitle(ed_title.getText().toString());
        note.setContent(ed_content.getText().toString());
//        note.setEditTime(current);
        note.setEdittime(new Date());
        note.setUid((int)SPUtils.get(this,"uid",0));
        note.setTag(ed_type.getText().toString());

    }
    public  void initView() {
        ed_content = (EditText) findViewById(R.id.id_content);
        ed_title = (EditText) findViewById(R.id.id_title);
        ed_type= (EditText) findViewById(R.id.ed_type);
        cb= (CheckBox) findViewById(R.id.checkBox);
        spinner= (Spinner) findViewById(R.id.id_spinner);
        final List<String> list=new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.types);
        for (int i = 0; i < stringArray.length; i++) {
            list.add(stringArray[i]);
        }
        ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);


        spinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_done){
            updateNote();
        }
        return super.onOptionsItemSelected(item);
    }
}
