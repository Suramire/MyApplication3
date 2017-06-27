package com.suramire.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.xmut.sc.entity.Note;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Suramire on 2017/6/20.
 */

public class NoteDetailActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    @Bind(R.id.detail_listview)
    ObservableListView detailListview;
    @Bind(R.id.detail_empty)
    TextView detailEmpty;
    private ActionBar actionBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetail);
        ButterKnife.bind(this);
        final Note note = (Note) getIntent().getSerializableExtra("note");
        List<Note> notes = new ArrayList<>();
        notes.add(note);
        // TODO: 2017/6/26 判断该帖子是否有图片,有则显示图片
        Log.d("NoteDetailActivity", note.getContent());
        detailListview.setAdapter(new CommonAdapter<Note>(this, R.layout.item_detail, notes) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, Note item, int position) {
                helper.setText(R.id.detail_title, item.getTitle())
                        .setText(R.id.detail_content, item.getContent())
                        .setText(R.id.detail_tag, item.getTag());
            }
        });
        final View header = View.inflate(this, R.layout.header_notedetail, null);
        View footer = View.inflate(this, R.layout.footer_notedetail, null);
        detailListview.setEmptyView(detailEmpty);
        detailListview.addHeaderView(header);
        detailListview.addFooterView(footer);
        //评论按钮
        ImageButton imageButton = footer.findViewById(R.id.imageView8);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteDetailActivity.this, ReceiveActivity.class).putExtra("nid", note.getNid()));
            }
        });
        detailListview.setScrollViewCallbacks(this);
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
        } else if (scrollState == ScrollState.DOWN) {
            if (!actionBar.isShowing()) {
                actionBar.show();
            }
        }


    }
}
