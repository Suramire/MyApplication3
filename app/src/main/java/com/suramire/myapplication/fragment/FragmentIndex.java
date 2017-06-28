package com.suramire.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.classic.adapter.CommonRecyclerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.activity.NoteByTypeActivity;
import com.suramire.myapplication.R;
import com.suramire.myapplication.activity.NoteDetailActivity;
import com.suramire.myapplication.entity.Type;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.GlideImageLoader;
import com.suramire.myapplication.util.GsonUtil;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Note;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.suramire.myapplication.util.Constant.DELAY;
import static com.suramire.myapplication.util.Constant.indexCount;
import static com.suramire.myapplication.util.Constant.isDestory;
import static com.suramire.myapplication.util.Constant.notes;

/**
 * Created by Suramire on 2017/6/20.
 * 首页显示,点击帖子进入详情
 * 下拉刷新数据
 * 下拉隐藏actionbar与底部导航按钮
 */

public class FragmentIndex extends Fragment implements ObservableScrollViewCallbacks {

    private CommonAdapter<Note> adapter;
    private ObservableListView listView;
    private Banner banner;

    private AppCompatActivity activity;
    private ActionBar ab;
    private BottomNavigationView bottomnavigationview;
    private FloatingActionButton fab;

    private View headerBanner;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean bannerIsShow;
    private int uid;
    private Toolbar toolbar;
    private RelativeLayout bottomlayout;
    private View headerview;
    private RecyclerView recyclerView;

    // TODO: 2017/6/26 首页轮播图动态获取,没有则不显示
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_index, container, false);
        activity = (AppCompatActivity) getActivity();
        headerBanner = View.inflate(activity, R.layout.header_banner,null);
        ab = activity.getSupportActionBar();
        Log.d("FragmentIndex", "ab:" + ab);
//        headerview = View.inflate(activity, R.layout.header_blank, null);
        uid = (int) SPUtils.get(activity, "uid", 0);
        bottomnavigationview = (BottomNavigationView) activity.findViewById(R.id.bottomnavigationview);
        fab = (FloatingActionButton) activity.findViewById(R.id.fab);
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        bottomlayout = (RelativeLayout) activity.findViewById(R.id.bottomlayout);
        swipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getInitData("refresh&count="+ indexCount+"&uid="+uid);
                    }
                }).start();
            }
        });
        swipeRefreshLayout.setProgressViewOffset(true,0,160);//设置进度圈位置
        setupListView(swipeRefreshLayout);
        setupBanner(headerBanner);

//        if((boolean) SPUtils.get(activity,"banner",true)){
//
//        }else {
//            removeBanner(headerBanner);
//
//        }


        //初次加载
        getInitData("index");

        return view;
    }

    private void getInitData(String what) {
        Log.d("FragmentIndex", "重新启动fragment时判断"+isDestory);
        if(isDestory){
            //如果重启应用之前被销毁则先加载销毁前的数据
            updateListView(notes);
            isDestory = false;
        }else{
            HTTPUtil.getCall(Constant.URL +what+"&uid="+uid,new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d("FragmentIndex", "onFailure");
                }
                @Override
                public void onResponse(Response response) throws IOException {
                    Log.d("FragmentIndex", "onResponse");
                    try {
                        String string = response.body().string();
                        final List<Note> mNotes = GsonUtil.jsonToList(string, Note.class);
                        indexCount += mNotes.size();//记录帖子数
                        final int mcount =mNotes.size();//本次刷新的数量
                        if (mcount==0){
                            SystemClock.sleep(1000);
                            handler.sendEmptyMessage(mcount);//取消下拉进度圈
                        }else {
                            //保留已加载内容
                            if(notes==null){
                                notes = new ArrayList<Note>();
                            }else{
//                                Collections.reverse(mNotes);
                                for(Note n:notes){
                                    mNotes.add(n);
                                }
                            }
                            notes = mNotes;
                            SystemClock.sleep(1000);
                            handler.sendEmptyMessage(mcount);//取消下拉进度圈
                        }

                    } catch (IOException e) {
                        Toast.makeText(activity, "未发现新内容", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void setupListView(SwipeRefreshLayout swipeRefreshLayout) {
        listView =  swipeRefreshLayout.findViewById(R.id.index_listview);
        listView.setScrollViewCallbacks(this);
    }

    private void updateListView(final List<Note> notes){
        adapter = new CommonAdapter<Note>(activity, R.layout.item_index, notes) {

            @Override
            public void onUpdate(BaseAdapterHelper helper, Note item, final int position) {
                // TODO: 2017/6/25 首页item显示评论数
                helper.setText(R.id.index_tag, item.getTag())
                        .setText(R.id.index_title,item.getTitle())
                        .setText(R.id.index_content,item.getContent())
                        .setText(R.id.index_count,item.getCount()+"");
                helper.setOnClickListener(R.id.index_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("note",notes.get(position));
                        startActivity(new Intent(activity, NoteDetailActivity.class).putExtras(bundle));
                    }
                }).setOnClickListener(R.id.index_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("note",notes.get(position));
                        startActivity(new Intent(activity, NoteDetailActivity.class).putExtras(bundle));
                    }
                });
            }
        };
        swipeRefreshLayout.setRefreshing(false);//fixme 注销后重新启动应用第一次下拉进度圈不会消失
        listView.setAdapter(adapter);
    }



    private void setupBanner(final View headerBanner) {
        listView.removeHeaderView(headerBanner);
        Boolean show = (Boolean) SPUtils.get(activity, "banner", true);
        banner = headerBanner.findViewById(R.id.banner);
        recyclerView = headerBanner.findViewById(R.id.listview_type);
        List<Type> types = new ArrayList<>();
        for(int i=0;i<6;i++){
            Type t = new Type(R.drawable.a4x,"类型"+(i+1));
            types.add(t);
        }
        recyclerView.setAdapter(new CommonRecyclerAdapter<Type>(activity,R.layout.item_type,types) {

            @Override
            public void onUpdate(BaseAdapterHelper helper, Type item, final int position) {
                helper.setImageResource(R.id.img_type, item.getImage())
                        .setText(R.id.textview_type, item.getName());
                helper.setOnClickListener(R.id.img_type, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: 2017/6/28 这里跳转到对应分类的帖子
                        startActivity(new Intent(activity, NoteByTypeActivity.class).putExtra("index",position));
                    }
                })
                        .setOnClickListener(R.id.textview_type, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(activity, NoteByTypeActivity.class).putExtra("index",position));
                            }
                        });
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(activity,3));
        if(show){

            banner.setImageLoader(new GlideImageLoader());
            ArrayList<Integer> images = new ArrayList<>();
            images.add(R.drawable.imga);
            images.add(R.drawable.imgb);
            images.add(R.drawable.imgc);
            images.add(R.drawable.imgd);
            images.add(R.drawable.imge);

            List<String> stringList = new ArrayList<>();
            for(int i=0;i<images.size();i++){
                stringList.add("这是是图片对于的标题"+(i+1));
            }

            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                    .setImages(images)
                    .setBannerTitles(stringList)
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            Toast.makeText(getActivity(),"点击了第" +( position+1)+"张图片", Toast.LENGTH_SHORT).show();
                        }
                    }).start();
            banner.setVisibility(View.VISIBLE);
        }else{
            banner.setVisibility(View.GONE);
        }
        listView.addHeaderView(headerBanner);
//            bannerIsShow = true;
//        if ((Boolean) SPUtils.get(activity,"banner",true)) {
//
//            bannerIsShow = true;
//        }else{
//            listView.removeHeaderView(headerBanner);
//            bannerIsShow = false;
//        }

        // TODO: 2017/6/25 设置项加入 显示banner开关

    }

//    public void removeBanner(View headerBanner){
//        listView.removeHeaderView(headerBanner);
//        bannerIsShow = false;
//    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
//        boolean banner = (boolean) SPUtils.get(activity, "banner", true);
////        L.e("banner:"+banner+" bannerIsShow:"+bannerIsShow);
//        if(banner&&!bannerIsShow){
//            //之前没有banner
//            setupBanner(headerBanner);
//        }else if(!banner && bannerIsShow){
////            之前有banner
//            removeBanner(headerBanner);
//        }
        setupBanner(headerBanner);

        super.onResume();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {



        if (activity == null) {
            return;
        }
        if (ab == null) {
            return;
        }
                if (scrollState == ScrollState.UP) {
            if (fab.isShown()) {
//                listView.removeHeaderView(headerview);
                hide(toolbar, bottomlayout);
            }

        } else if (scrollState == ScrollState.DOWN) {
            if (!fab.isShown()) {
//                listView.addHeaderView(headerview);
                show(toolbar, bottomlayout);
            }

        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(false);
            }
            if(message.what>0){
                Toast.makeText(activity,"发现"+message.what+"条内容",Toast.LENGTH_SHORT).show();
                updateListView(notes);
            }else{
                Toast.makeText(activity,"未发现新内容",Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    @Override
    public void onDestroyView() {
        //在被销毁之前先保存已经加载的notes
        Log.d("FragmentIndex", "onDestroyView");
        isDestory = true;
        super.onDestroyView();
    }
    private void hide(final View top, final View bottom) {
        fab.hide();
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

    private void show(final View top, final View bottom) {
        fab.show();
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

}
