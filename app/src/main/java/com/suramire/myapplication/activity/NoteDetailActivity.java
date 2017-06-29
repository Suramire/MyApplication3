package com.suramire.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.L;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.suramire.myapplication.util.Constant.DELAY;

/**
 * Created by Suramire on 2017/6/20.
 */

public class NoteDetailActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    @Bind(R.id.detail_listview)
    ObservableListView detailListview;
    @Bind(R.id.detail_empty)
    TextView detailEmpty;
    @Bind(R.id.detail_botton)
    BottomNavigationView detailBotton;
    @Bind(R.id.toolbar2)
    Toolbar toolbar2;
    private ActionBar actionBar;
    private boolean isHide;
    private ImageView imageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final View picture = View.inflate(this, R.layout.header_notedetail, null);
        imageView = picture.findViewById(R.id.imageView4);
        final Note note = (Note) getIntent().getSerializableExtra("note");
        HTTPUtil.getCall(Constant.URL + "xijiayi&nid=" + note.getNid(), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
        int uid = (int)SPUtils.get(this,"uid",0);
        if(uid>0)
        HTTPUtil.getCall(Constant.URL + "setHistory&nid=" + note.getNid()+"&uid="+uid, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
        List<Note> notes = new ArrayList<>();
        notes.add(note);
        // TODO: 2017/6/26 判断该帖子是否有图片,有则显示图片
        Log.d("NoteDetailActivity", note.getContent());
        detailListview.setAdapter(new CommonAdapter<Note>(this, R.layout.item_detail, notes) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, Note item, int position) {
                helper.setText(R.id.detail_title, item.getTitle())
                        .setText(R.id.detail_content, item.getContent())
                        .setText(R.id.detail_tag, "标签:"+item.getTag());
                toolbar2.setTitle(item.getTitle());
                String img = item.getImg();
                if(!TextUtils.isEmpty(img)){
                    Picasso.with(NoteDetailActivity.this).load(Constant.BASEURL + "bbs/upload/" + img).into(imageView);
                    L.e("该帖子有配图");
                }else{
                    imageView.setVisibility(View.GONE);
                }
            }
        });
        final View header = View.inflate(this, R.layout.header_blank, null);

//        View footer = View.inflate(this, R.layout.footer_notedetail, null);
        detailListview.setEmptyView(detailEmpty);
        detailListview.addHeaderView(header);
        detailListview.addHeaderView(picture);

//        detailBotton.setSelectedItemId(R.id.navigation_notifications);
        detailBotton.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("MainActivity", "item:" + item);
                switch (item.getItemId()) {
                    case R.id.navigation_home:
//                        viewPager.setCurrentItem(0);
                        return false;
                    case R.id.navigation_dashboard:
                        Toast.makeText(NoteDetailActivity.this, "这里响应收藏操作", Toast.LENGTH_SHORT).show();
                        return false;
                    case R.id.navigation_notifications:
//                        viewPager.setCurrentItem(3);
                        startActivity(new Intent(NoteDetailActivity.this, ReceiveActivity.class).putExtra("nid", note.getNid()));
                        return false;
                }
                return true;
            }
        });
//        detailListview.addFooterView(footer);
        //评论按钮
//        ImageButton imageButton = footer.findViewById(R.id.imageView8);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(NoteDetailActivity.this, ReceiveActivity.class).putExtra("nid", note.getNid()));
//            }
//        });
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
        L.e("详情页 滚动");
        if (scrollState == ScrollState.UP) {
            if (!isHide) {
                hide(toolbar2,detailBotton);
                isHide = true;
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (isHide) {
                show(toolbar2,detailBotton);
                isHide = false;
            }
        }


    }

    private void show(final View top, final View bottom) {
//        fab.show();
        ValueAnimator animator = ValueAnimator.ofFloat(top.getHeight(), 0).setDuration(DELAY);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(top, -value);
            }
        });
        animator.start();
        ValueAnimator animator2 = ValueAnimator.ofFloat(bottom.getHeight(), 0).setDuration(DELAY);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(bottom, value);
            }
        });
        animator2.start();
    }

    private void hide(final View top, final View bottom) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, top.getHeight()).setDuration(DELAY);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(top, -value);
            }
        });
        animator.start();
        ValueAnimator animator2 = ValueAnimator.ofFloat(0, bottom.getHeight()).setDuration(DELAY);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(bottom, value);
            }
        });
        animator2.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
}
