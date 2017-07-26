package com.suramire.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.suramire.myapplication.R;
import com.suramire.myapplication.activity.NoteByTypeActivity;
import com.suramire.myapplication.activity.NoteDetailActivity;
import com.suramire.myapplication.base.App;
import com.suramire.myapplication.entity.Type;
import com.suramire.myapplication.mvp.BasePresenter;
import com.suramire.myapplication.mvp.BaseView;
import com.suramire.myapplication.util.GlideImageLoader;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Note;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import static com.suramire.myapplication.util.AnimationUtil.hide;
import static com.suramire.myapplication.util.AnimationUtil.show;
import static com.suramire.myapplication.util.Constant.currentCount;
import static com.suramire.myapplication.util.Constant.indexCount;
import static com.suramire.myapplication.util.Constant.notes;
import static com.suramire.myapplication.util.L.e;

/**
 * Created by Suramire on 2017/6/20.
 * 首页显示,点击帖子进入详情
 * 下拉刷新数据
 * 下拉隐藏actionbar与底部导航按钮
 */

public class FragmentIndex extends Fragment implements ObservableScrollViewCallbacks,BaseView {

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
    private int[] images;
    private List<Note> mNotes;
    private View view;
    private BasePresenter basePresenter;
    private Context mContext;
    private boolean isHide;


    // TODO: 2017/6/26 首页轮播图动态获取,没有则不显示
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view != null) {
            return view;
        }else{
            view = inflater.inflate(R.layout.activity_index, container, false);
            setupView();
            uid = (int) SPUtils.get("uid", 0);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            basePresenter.show("refresh&count="+ indexCount+"&uid="+uid);
                        }
                    }).start();
                }
            });

            setupListView(swipeRefreshLayout);
            setupBanner(headerBanner);
            basePresenter = new BasePresenter(this);
            basePresenter.show("index&uid="+uid);
            return view;
        }

    }

    private void setupView() {
        mContext = App.getContext();
        activity = (AppCompatActivity) getActivity();

        headerBanner = View.inflate(activity, R.layout.header_banner,null);
        ab = activity.getSupportActionBar();
        images = new int[]{R.drawable.a4x, R.drawable.a4y, R.drawable.a4z, R.drawable.a5a, R.drawable.a5b, R.drawable.a5_};
        bottomnavigationview = (BottomNavigationView) activity.findViewById(R.id.bottomnavigationview);
        fab = (FloatingActionButton) activity.findViewById(R.id.fab);
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        bottomlayout = (RelativeLayout) activity.findViewById(R.id.bottomlayout);
        swipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setProgressViewOffset(true,0,160);//设置进度圈位置
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
        listView.setAdapter(adapter);
    }



    private void setupBanner(final View headerBanner) {
        listView.removeHeaderView(headerBanner);
        Boolean show = (Boolean) SPUtils.get("banner", true);
        e("banner show:" + show);
        banner = headerBanner.findViewById(R.id.banner);
        recyclerView = headerBanner.findViewById(R.id.listview_type);
        List<Type> types = new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.types);
        for (int i = 0; i < stringArray.length; i++) {
            Type t = new Type(images[i],stringArray[i]);
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

            List<String> stringList = new ArrayList<>();
            for(int i=0;i<images.size();i++){
                try{
                    stringList.add("热门内容"+(i+1));
                }catch (Exception e){

                }

            }

            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                    .setImages(images)
                    .setBannerTitles(stringList)
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            startActivity(new Intent(activity,NoteDetailActivity.class).putExtra("note",notes.get(position)));
                        }
                    }).start();
            banner.setVisibility(View.VISIBLE);
        }else{
            banner.setVisibility(View.GONE);
        }
        listView.addHeaderView(headerBanner);


    }



    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        setupBanner(headerBanner);
        e("onResume f1");
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
            if (!isHide) {
                hide(toolbar, bottomlayout);
                fab.hide();
                isHide = true;
            }

        } else if (scrollState == ScrollState.DOWN) {
            if (isHide) {
                show(toolbar, bottomlayout);
                fab.show();
                isHide = false;
            }
        }
    }


    @Override
    public void onDestroyView() {
        //在被销毁之前先保存已经加载的notes
        e("f1 onDestroyView");
        super.onDestroyView();
    }


    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void cancelLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showSuccessful(Object data) {
        List<Note> notes = (List<Note>) data;
        Toast.makeText(mContext,"发现"+currentCount+"条内容",Toast.LENGTH_SHORT).show();
        updateListView(notes);
    }

    @Override
    public void showFail() {
        Toast.makeText(mContext, "加载数据失败", Toast.LENGTH_SHORT).show();
    }
}
