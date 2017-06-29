package com.suramire.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.GsonUtil;
import com.suramire.myapplication.util.L;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Suramire on 2017/6/28.
 */

public class NoteByTypeActivity extends BaseActivity {
    @Bind(R.id.listview_bytype)
    ListView listviewBytype;
    @Bind(R.id.tv_empty)
    TextView tvEmpty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebytype);
        ButterKnife.bind(this);
        int index = getIntent().getIntExtra("index", 0);
        String[] stringArray = getResources().getStringArray(R.array.types);
        getSupportActionBar().setTitle(stringArray[index]);
        listviewBytype.setEmptyView(tvEmpty);
        //根据下标获得帖子数据
        HTTPUtil.getCall(Constant.URL + "getNoteByTypeId&typeid=" + index, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String string = response.body().string();
                if (!TextUtils.isEmpty(string)) {
                    try {
                        final List<Note> notes = GsonUtil.jsonToList(string, Note.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listviewBytype.setAdapter(new CommonAdapter<Note>(NoteByTypeActivity.this, R.layout.item_index, notes) {
                                    @Override
                                    public void onUpdate(BaseAdapterHelper helper, Note item, final int position) {
                                        helper.setText(R.id.index_tag, item.getTag())
                                                .setText(R.id.index_title, item.getTitle())
                                                .setText(R.id.index_content, item.getContent())
                                                .setText(R.id.index_count, item.getCount() + "");
                                        helper.setOnClickListener(R.id.index_title, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("note", notes.get(position));
                                                startActivity(new Intent(NoteByTypeActivity.this, NoteDetailActivity.class).putExtras(bundle));
                                            }
                                        }).setOnClickListener(R.id.index_content, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("note", notes.get(position));
                                                startActivity(new Intent(NoteByTypeActivity.this, NoteDetailActivity.class).putExtras(bundle));
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    } catch (Exception e) {
                        L.e("json转list失败" + e);
                    }
                }
            }
        });
    }
}
