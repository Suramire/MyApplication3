package com.suramire.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.suramire.myapplication.util.FileUtil;

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
        setContentView(R.layout.activity_detail);
        listView = (ObservableListView) findViewById(R.id.detail_listview);
        listView.setAdapter(new CommonAdapter<String>(this,R.layout.item_index, FileUtil.getStrings(10)) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, String item, int position) {
                helper.setText(R.id.index_title,item);
            }
        });
        final View header = View.inflate(this, R.layout.header_notedetail, null);
        TextView textView = (TextView) findViewById(R.id.detail_empty);
        View footer = View.inflate(this, R.layout.footer_notedetail, null);
        listView.setEmptyView(textView);
        listView.addHeaderView(header);
        listView.addFooterView(footer);

        ImageButton imageButton = footer.findViewById(R.id.imageView8);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteDetailActivity.this,ReceiveActivity.class));
            }
        });
        listView.setScrollViewCallbacks(this);
//        listView.addFooterView();
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
