package com.zlw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PostedActivity extends BaseActivity {
    private SwipeMenuListView listView;

    private ArrayList<Note> contentList=new ArrayList<Note>();
//    private String path= Constant.BASEURL+"android_service/GetData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poated);
        requstNet();
        listView= (SwipeMenuListView) findViewById(R.id.listView);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                openItem.setWidth(90);
//                // set item title
//                openItem.setTitle("Open");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setTitle("删除帖子");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setWidth(240);

                // set a icon
                deleteItem.setIcon(R.mipmap.del_icon_normal);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator

        listView.setMenuCreator(creator);

    }

    private void setMenuItemClick(final MyBaseAdapter adapter) {
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
//                    case 0:
                        // open

//                        break;
                    case 0:
                        // delete
                        //点击删除按钮，对服务器进行请求，根据Note的nid删除Note

                        Note note = contentList.get(position);
                        Integer nid = note.getNid();

                        OkHttpClient okHttpClient=new OkHttpClient();

                        final Request.Builder builder = new Request.Builder();
//                        RequestBody requestBody = new FormEncodingBuilder().add("nid", String.valueOf(nid)).add("flag","deletebyid").build();
//                        Request request = builder.post(requestBody).url(path).build();
                        Request request = builder.get().url(Constant.URL+"deletebyid&nid="+nid).build();

                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {

                            }

                            @Override
                            public void onResponse(Response response) throws IOException {


                                String result = response.body().string();
                                Log.i("flag","删除成功"+result);
                                contentList.remove(position);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });

                            }
                        });


                        break;
                }
                Toast.makeText(PostedActivity.this,position+"项'", Toast.LENGTH_SHORT).show();;
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }


    /**
     * 判断是否有网络的方法
     * @return  true 有效网络  false 没网络或者连接了无效网络
     */
    public boolean isconnectionInternet(){
        //获取连接管理对象
        ConnectivityManager cm=(ConnectivityManager)PostedActivity.this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络信息对象
        NetworkInfo ni=cm.getActiveNetworkInfo();
        if(ni!=null){
            if(ni.isAvailable()){
                return true;
            }
        }
        return false;
    }

    /**
     * 访问服务器，查询Note表的所有数据，
     * 并装载在adapter中
     */
    private void requstNet() {


//        OkHttpClient okHttpClient=new OkHttpClient();
//
//        Request.Builder builder = new Request.Builder();
//        RequestBody requestBody = new FormEncodingBuilder().add("flag","queryall").build();
//        Request request = builder.post(requestBody).url(path).build();
//
//        Call call = okHttpClient.newCall(request);

//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//
//
//                String result = response.body().string();
//                Log.i("flag",result);
//
//                Gson gson=new Gson();
//
//
//                Type type = new TypeToken<ArrayList<Note>>() {
//                }.getType();
//                final ArrayList<Note> list = gson.fromJson(result,type);
//                if(list.size()>0){
//                    contentList=list;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                           MyBaseAdapter adapter=new MyBaseAdapter(PostedActivity.this,contentList);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    MyBaseAdapter adapter=new MyBaseAdapter(PostedActivity.this,contentList);
//                                    listView.setAdapter(adapter);
//                                    adapter.notifyDataSetChanged();
//                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                            Toast.makeText(PostedActivity.this,i+"项'", Toast.LENGTH_SHORT).show();
//
//                                            Note note = contentList.get(i);
//                                            int nid = note.getNid();
//                                            if (!isconnectionInternet()){
//
//                                                Toast.makeText(PostedActivity.this,"没有网络，不能进行网络服务", Toast.LENGTH_SHORT).show();
//                                            }
//                                            OkHttpClient okHttpClient=new OkHttpClient();
//                                            final Request.Builder builder = new Request.Builder();
//                                            RequestBody requestBody = new FormEncodingBuilder().add("nid", String.valueOf(nid)).add("flag","querybyid").build();
//                                            Request request = builder.post(requestBody).url(path).build();
//                                            Call call = okHttpClient.newCall(request);
//                                            call.enqueue(new Callback() {
//                                                @Override
//                                                public void onFailure(Request request, IOException e) {
//
//                                                }
//
//                                                @Override
//                                                public void onResponse(Response response) throws IOException {
//
//
//                                                    String result = response.body().string();
//
//                                                    Gson gson=new Gson();
//
//                                                    Note note = gson.fromJson(result, Note.class);
//                                                    Intent intent = new Intent(PostedActivity.this, updateNote.class);
//                                                    Bundle bundle=new Bundle();
//                                                    bundle.putSerializable("note",note);
//                                                    intent.putExtras(bundle);
//                                                    startActivity(intent);
//                                                }
//                                            });
//                                        }
//                                    });
//                                    setMenuItemClick(adapter);
//                                }
//                            });
//                        }
//                    });
//                }
//            }
//        });

        HTTPUtil.getCall(Constant.URL + "queryall&uid=" + (int) SPUtils.get(PostedActivity.this, "uid", 0), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {


                String result = response.body().string();
//                Log.i("flag",result);

                Gson gson=new Gson();


                Type type = new TypeToken<ArrayList<Note>>() {
                }.getType();
                final ArrayList<Note> list = gson.fromJson(result,type);
                if(list.size()>0){
                    contentList=list;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                           MyBaseAdapter adapter=new MyBaseAdapter(PostedActivity.this,contentList);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyBaseAdapter adapter=new MyBaseAdapter(PostedActivity.this,contentList);
                                    listView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                            Toast.makeText(PostedActivity.this,i+"项'", Toast.LENGTH_SHORT).show();

                                            Note note = contentList.get(i);
                                            int nid = note.getNid();
                                            if (!isconnectionInternet()){

                                                Toast.makeText(PostedActivity.this,"没有网络，不能进行网络服务", Toast.LENGTH_SHORT).show();
                                            }
                                            OkHttpClient okHttpClient=new OkHttpClient();
                                            final Request.Builder builder = new Request.Builder();
//                                            RequestBody requestBody = new FormEncodingBuilder().add("nid", String.valueOf(nid)).add("flag","querybyid").build();
//                                            Request request = builder.post(requestBody).url(path).build();
                                            Request request = builder.get().url(Constant.URL+"querybyid&nid="+nid).build();
                                            Call call = okHttpClient.newCall(request);
                                            call.enqueue(new Callback() {
                                                @Override
                                                public void onFailure(Request request, IOException e) {

                                                }

                                                @Override
                                                public void onResponse(Response response) throws IOException {


                                                    String result = response.body().string();

                                                    Gson gson=new Gson();

                                                    Note note = gson.fromJson(result, Note.class);
                                                    Intent intent = new Intent(PostedActivity.this, updateNote.class);
                                                    Bundle bundle=new Bundle();
                                                    bundle.putSerializable("note",note);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    });
                                    setMenuItemClick(adapter);
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onResume() {
        requstNet();
        super.onResume();
    }
}
