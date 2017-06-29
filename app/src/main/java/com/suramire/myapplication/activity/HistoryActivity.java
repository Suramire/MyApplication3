package com.suramire.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.suramire.myapplication.util.DateUtil;
import com.suramire.myapplication.util.GsonUtil;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.L;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.suramire.myapplication.util.Constant.URL;

/**
 * Created by Suramire on 2017/6/29.
 */

public class HistoryActivity extends BaseActivity {
    @Bind(R.id.histoty_listview)
    SwipeMenuListView histotyListview;
    @Bind(R.id.recommend_empty)
    TextView recommendEmpty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        histotyListview.setEmptyView(recommendEmpty);
        //进入查询已登录用户的历史记录
        int uid = (int)SPUtils.get(this, "uid", 0);
        if(uid>0){
            L.e("浏览历史记录:" + URL + "getHistory&uid=" + uid);
            HTTPUtil.getCall(URL + "getHistory&uid=" + uid, new Callback() {

                private List<Note> mNotes;

                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String string = response.body().string();
                    if(!TextUtils.isEmpty(string)){
                        mNotes = GsonUtil.jsonToList(string, Note.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(mNotes.size()>0){
                                    histotyListview.setAdapter(new CommonAdapter<Note>(HistoryActivity.this,R.layout.item_index, mNotes) {
                                        @Override
                                        public void onUpdate(BaseAdapterHelper helper, Note item, final int position) {
                                            helper.setText(R.id.index_tag, item.getTag())
                                                    .setText(R.id.index_title,item.getTitle())
                                                    .setText(R.id.index_content,item.getContent())
                                                    .setText(R.id.textView3, DateUtil.dateToString(item.getViewtime()))
                                                    .setText(R.id.index_count,item.getCount()+"");
                                            helper.setOnClickListener(R.id.index_title, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("note",mNotes.get(position));
                                                    startActivity(new Intent(HistoryActivity.this, NoteDetailActivity.class).putExtras(bundle));
                                                }
                                            }).setOnClickListener(R.id.index_content, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("note",mNotes.get(position));
                                                    startActivity(new Intent(HistoryActivity.this, NoteDetailActivity.class).putExtras(bundle));
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });

                    }
                }
            });
        }
    }
}
