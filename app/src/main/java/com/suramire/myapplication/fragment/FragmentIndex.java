package com.suramire.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.suramire.myapplication.NoteDetail;
import com.suramire.myapplication.R;
import com.suramire.myapplication.util.GlideImageLoader;
import com.suramire.myapplication.view.MyViewPager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Suramire on 2017/6/20.
 */

public class FragmentIndex extends Fragment implements ObservableScrollViewCallbacks {

    private ArrayList<String> items;
    private CommonAdapter<String> adapter;
    private ObservableListView listView;
    private Banner banner;
    private boolean flag = false;
    private AppCompatActivity activity;
    private ActionBar ab;
    private BottomNavigationView bottomnavigationview;
    private TranslateAnimation mShowAction;
    private FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_index, container, false);
        activity = (AppCompatActivity) getActivity();
        ab = activity.getSupportActionBar();
        bottomnavigationview = (BottomNavigationView) activity.findViewById(R.id.bottomnavigationview);
        floatingActionButton = (FloatingActionButton) activity.findViewById(R.id.fab);
        banner = inflate.findViewById(R.id.banner);
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
        final SwipeRefreshLayout swipeRefreshLayout = inflate.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //模拟刷新数据 2017-6-20 11:13:20

                            items.add(0,"new item " + 00);
//                             adapter.notifyDataSetChanged();
//                            adapter.notifyItemInserted(2);
//                            adapter.notifyItemRangeChanged(2,items.size()-2);
                        //通知数据更新
                        Toast.makeText(getActivity(), "找到1条内容", Toast.LENGTH_SHORT).show();

                        adapter = new CommonAdapter<String>(getActivity(),R.layout.item_index,items) {
                            @Override
                            public void onUpdate(BaseAdapterHelper helper, String item, int position) {
                                helper.setText(R.id.textView2, item);
                            }
                        };
                        listView.setAdapter(adapter);
//                        recyclerView.setAdapter(adapter);
                        //取消进度圈
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        swipeRefreshLayout.setProgressViewOffset(true,0,160);
        listView =  swipeRefreshLayout.findViewById(R.id.index_listview);


        items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add("item "+ i);
        }
        adapter = new CommonAdapter<String>(getActivity(), R.layout.item_index, items) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, String item, int position) {
                helper.setText(R.id.textView2, item);
                helper.setOnClickListener(R.id.textView2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), NoteDetail.class));
                    }
                });
                helper.setOnClickListener(R.id.textView4, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), NoteDetail.class));
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        listView.setScrollViewCallbacks(this);

//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
////                int position = listView.getLastVisiblePosition();
////                Log.d("FragmentIndex", "position:" + position);
//                if(i>firstVisiable){
//                    firstVisiable = i;
//                                        banner.setVisibility(View.GONE);
//                    activity.getSupportActionBar().hide();
//                    BottomNavigationView bottomnavigationview = (BottomNavigationView) activity.findViewById(R.id.bottomnavigationview);
//                    bottomnavigationview.setVisibility(View.GONE);
//                }else if(i<firstVisiable){
//                    firstVisiable = i;
//                    banner.setVisibility(View.VISIBLE);
//                    activity.getSupportActionBar().show();
//                    BottomNavigationView bottomnavigationview = (BottomNavigationView) activity.findViewById(R.id.bottomnavigationview);
//                    bottomnavigationview.setVisibility(View.VISIBLE);
//
//                }
//
////                else if(i<firstVisiable){
////                    banner.setVisibility(View.VISIBLE);
////                    firstVisiable = i;
////                }
////                Log.d("FragmentIndex", "i:" + i);
////                Log.d("FragmentIndex", "i1:" + i1);
////                Log.d("FragmentIndex", "i2:" + i2);
////                if(position== (i2-1)&&!flag){
////                    flag = true;
////                    Log.d("FragmentIndex", "true");
////                    banner.setVisibility(View.GONE);
////                }
//            }
//        });

        return inflate;
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        Log.d("FragmentIndex", "listView.getFirstVisiblePosition():" + listView.getFirstVisiblePosition());

        if (activity == null) {
            return;
        }

        if (ab == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.setShowHideAnimationEnabled(true);
                ab.hide();
                bottomnavigationview.setVisibility(View.GONE);
                banner.setVisibility(View.GONE);
                floatingActionButton.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
                bottomnavigationview.setVisibility(View.VISIBLE);
                floatingActionButton.show();
            }
        }
    }
}
