package com.suramire.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.classic.adapter.CommonRecyclerAdapter;
import com.suramire.myapplication.R;
import com.suramire.myapplication.test.Student;
import com.suramire.myapplication.util.Constant;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Suramire on 2017/6/20.
 */

public class SearchActivity extends AppCompatActivity {
    @Bind(R.id.search_listview)
    ListView searchListview;
    @Bind(R.id.search_hot_recyclerview)
    RecyclerView searchHotRecyclerview;
    @Bind(R.id.search_layout_hot)
    LinearLayout searchLayoutHot;
    @Bind(R.id.search_layout_hot2)
    LinearLayout searchLayoutHot2;

    private List<Student> data;
    private SearchView searchView;
    CommonAdapter<Note> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Log.d("SearchActivity", "Constant.isLogin:" + Constant.isLogin);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("啦啦啦");
        strings.add("UC");
        strings.add("正文");
        initListView();
        final MyHandler myHandler = new MyHandler();
        searchHotRecyclerview.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.HORIZONTAL, false));
        searchHotRecyclerview.setAdapter(new CommonRecyclerAdapter<String>(SearchActivity.this, R.layout.item_hot, strings) {

            @Override
            public void onUpdate(BaseAdapterHelper helper, String item, int position) {
                helper.setText(R.id.search_item_hot, item);
                helper.setOnClickListener(R.id.search_item_hot, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String trim = ((Button) view).getText().toString().trim();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                searchBySomething(trim, myHandler);
                            }
                        }).start();
                    }
                });
            }
        });

//        ImageView imageView = new ImageView(SearchActivity.this);
//        imageView.setImageResource(R.drawable.ic_menu_manage);
//        searchListview.setEmptyView(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        searchView = (SearchView) menu.findItem(R.id.action_settings).getActionView();
        searchView.setIconifiedByDefault(false);//默认展开
        searchView.setIconified(false);//默认焦点在搜索框上
        searchView.setQueryHint("搜索标题,内容,标签...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                final MyHandler myHandler = new MyHandler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        searchBySomething(query, myHandler);
                    }
                }).start();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 初始化listview 清空之前存在的数据
     */
    private void initListView(){
        ArrayList<Note> notes = new ArrayList<>();
        adapter = new CommonAdapter<Note>(SearchActivity.this, R.layout.item_search, notes) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, Note item, int position) {
                helper.setText(R.id.search_tag, item.getTag())
                        .setText(R.id.search_title, item.getTitle())
                        .setText(R.id.search_content, item.getContent())
                        .setText(R.id.search_count, item.getCount() + "");
            }
        };
        searchListview.setAdapter(adapter);
    }


    /**
     * 根据关键字模糊查询
     *
     * @param query     关键字
     * @param myHandler 进行UI操作
     */
    public void searchBySomething(String query, MyHandler myHandler) {
        try {
            String query0 = URLEncoder.encode(query, "utf-8");//设置编码
            URL url1 = new URL(Constant.BASEURL + "bbs/GetResult?query=" + query0);
            String s = Constant.BASEURL + "bbs/GetResult?query=" + query;
            Log.d("SearchActivity", s);
            HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
            ObjectInputStream objectInputStream = new ObjectInputStream(urlConnection.getInputStream());
            Object o = objectInputStream.readObject();//读取对象
            List<Note> notes = (List<Note>) o;
            Message message = Message.obtain();
            if (notes.size() > 0) {
                Log.d("SearchActivity", "notes.size():" + notes.size());
                message.what = Constant.SHOWRESULT;
                message.obj = notes;

            }else{
                message.what = Constant.SHOWNOTHING;
//                message.obj = notes;
            }
            myHandler.sendMessage(message);

        } catch (MalformedURLException e) {
            Log.e("SearchActivity", "MalformedURLException:" + e);
        } catch (IOException e) {
            Log.e("SearchActivity", "IOException:" + e);
        } catch (ClassNotFoundException e) {
            Log.e("SearchActivity", "ClassNotFoundException:" + e);
        } catch (Exception e) {
            Log.e("SearchActivity", "Exception:" + e);
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SHOWRESULT: {
                    List<Note> notes = (List<Note>) msg.obj;

                        Log.d("MyHandler", "adapter.getCount():" + adapter.getCount());
                        adapter = new CommonAdapter<Note>(SearchActivity.this, R.layout.item_search, notes) {
                            @Override
                            public void onUpdate(BaseAdapterHelper helper, Note item, int position) {
                                helper.setText(R.id.search_tag, item.getTag())
                                        .setText(R.id.search_title, item.getTitle())
                                        .setText(R.id.search_content, item.getContent())
                                        .setText(R.id.search_count, item.getCount() + "");
                            }
                        };
                        searchListview.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                        searchLayoutHot.setVisibility(View.GONE);//隐藏热搜标签
                        searchLayoutHot2.setVisibility(View.GONE);//隐藏热搜标签
                        searchView.setIconifiedByDefault(true);//收起搜索框
                        getSupportActionBar().setTitle("搜索结果");
                }
                break;
                case Constant.SHOWNOTHING:{
                    initListView();

                    Toast.makeText(SearchActivity.this, "未找到符合条件的结果", Toast.LENGTH_SHORT).show();
                } break;
            }
        }
    }
}
