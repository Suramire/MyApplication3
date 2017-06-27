package com.suramire.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.suramire.myapplication.R;
import com.xmut.sc.entity.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suramire on 2017/6/20.
 */

public class NoteDetailActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private TranslateAnimation mShowAction;
    private TranslateAnimation mHiddenAction;
    private ListView receivelistview;
    private ObservableListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetail);
        final Note note = (Note)getIntent().getSerializableExtra("note");
        List<Note> notes = new ArrayList<>();
        notes.add(note);
        // TODO: 2017/6/26 判断该帖子是否有图片,有则显示图片
        Log.d("NoteDetailActivity", note.getContent());
        listView = (ObservableListView) findViewById(R.id.detail_listview);
        listView.setAdapter(new CommonAdapter<Note>(this,R.layout.item_detail,notes) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, Note item, int position) {
                helper.setText(R.id.detail_title, item.getTitle())
                        .setText(R.id.detail_content, item.getContent())
                        .setText(R.id.detail_tag, item.getTag());
            }
        });
        final View header = View.inflate(this, R.layout.header_notedetail, null);
        TextView textView = (TextView) findViewById(R.id.detail_empty);
        View footer = View.inflate(this, R.layout.footer_notedetail, null);
        listView.setEmptyView(textView);
        listView.addHeaderView(header);
        listView.addFooterView(footer);
        //评论按钮
        ImageButton imageButton = footer.findViewById(R.id.imageView8);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteDetailActivity.this,ReceiveActivity.class).putExtra("nid",note.getNid()));
            }
        });
        listView.setScrollViewCallbacks(this);
        actionBar = getSupportActionBar();
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        if (scrollState == ScrollState.UP) {
            if (actionBar.isShowing()) {
                actionBar.hide();
            }
        }else if (scrollState == ScrollState.DOWN){
            if (!actionBar.isShowing()) {
                actionBar.show();
            }
        }


    }
}
