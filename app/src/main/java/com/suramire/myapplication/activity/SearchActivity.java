package com.suramire.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.base.App;
import com.suramire.myapplication.base.BaseActivity;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.GsonUtil;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.L;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.suramire.myapplication.util.Constant.URL;

/**
 * Created by Suramire on 2017/6/20.
 */

public class SearchActivity extends BaseActivity {
    @Bind(R.id.search_listview)
    ListView searchListview;
    @Bind(R.id.search_hot_recyclerview)
    RecyclerView searchHotRecyclerview;
    @Bind(R.id.search_layout_hot)
    LinearLayout searchLayoutHot;
    @Bind(R.id.search_layout_hot2)
    LinearLayout searchLayoutHot2;

    private SearchView searchView;
    CommonAdapter<Note> adapter;
    private App mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



//        ImageView imageView = new ImageView(SearchActivity.this);
//        imageView.setImageResource(R.drawable.ic_menu_manage);
//        searchListview.setEmptyView(imageView);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        mContext = App.getContext();
        // TODO: 2017/6/26 这里读取服务类的热门分类或关键字
        ArrayList<String> strings = new ArrayList<>();
        strings.add("为什么");
        strings.add("怎样");
        strings.add("学会");
        initListView();

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
                                searchBySomething(trim);
                            }
                        }).start();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconifiedByDefault(false);//默认展开
        searchView.setIconified(false);//默认焦点在搜索框上
        searchView.setQueryHint("搜索标题,内容,标签...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        searchBySomething(query);
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
        final ArrayList<Note> notes = new ArrayList<>();
        adapter = new CommonAdapter<Note>(SearchActivity.this, R.layout.item_search, notes) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, Note item, final int position) {
                helper.setText(R.id.search_tag, item.getTag())
                        .setText(R.id.search_title, item.getTitle())
                        .setText(R.id.search_content, item.getContent())
                        .setText(R.id.search_count, item.getCount() + "");
                helper.setOnClickListener(R.id.search_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(SearchActivity.this, NoteDetailActivity.class).putExtra("note",notes.get(position)));
                    }
                })
                .setOnClickListener(R.id.search_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(SearchActivity.this, NoteDetailActivity.class).putExtra("note",notes.get(position)));

                    }
                })
                ;
            }
        };
        searchListview.setAdapter(adapter);
    }


    /**
     * 根据关键字模糊查询
     *
     * @param query     关键字
     *
     */
    public void searchBySomething(String query) {
        try {
            String query0 = URLEncoder.encode(query, "utf-8");//设置编码
            URL url1 = new URL(Constant.BASEURL + "bbs/GetResult?query=" + query0);
            String url = URL + "search&query=" + query;
            Log.d("SearchActivity", url);
            HTTPUtil.getCall(url, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String string = response.body().string();
                    L.e("搜索结果" + string);
                    if(!TextUtils.isEmpty(string)){
                        final List<Note> mnotes = GsonUtil.jsonToList(string, Note.class);
                        if(mnotes.size()>0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new CommonAdapter<Note>(SearchActivity.this, R.layout.item_search, mnotes) {
                                        @Override
                                        public void onUpdate(BaseAdapterHelper helper, Note item, final int position) {
                                            helper.setText(R.id.search_tag, item.getTag())
                                                    .setText(R.id.search_title, item.getTitle())
                                                    .setText(R.id.search_content, item.getContent())
                                                    .setText(R.id.search_count, item.getCount() + "");
                                            helper.setOnClickListener(R.id.search_content, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    startActivity(new Intent(SearchActivity.this, NoteDetailActivity.class).putExtra("note",mnotes.get(position)));
                                                }
                                            })
                                                    .setOnClickListener(R.id.search_title, new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            startActivity(new Intent(SearchActivity.this, NoteDetailActivity.class).putExtra("note",mnotes.get(position)));

                                                        }
                                                    })
                                            ;
                                        }
                                    };
                                    searchListview.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                                    searchLayoutHot.setVisibility(View.GONE);//隐藏热搜标签
                                    searchLayoutHot2.setVisibility(View.GONE);//隐藏热搜标签
                                    searchView.setIconifiedByDefault(true);//收起搜索框
                                    getSupportActionBar().setTitle("搜索结果");
                                }
                            });
                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "未找到符合条件的结果", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });


        } catch (MalformedURLException e) {
            Log.e("SearchActivity", "MalformedURLException:" + e);
        } catch (IOException e) {
            Log.e("SearchActivity", "IOException:" + e);
        } catch (Exception e) {
            Log.e("SearchActivity", "Exception:" + e);
        }
    }

}
