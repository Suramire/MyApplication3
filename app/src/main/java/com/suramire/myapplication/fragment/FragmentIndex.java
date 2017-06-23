package com.suramire.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.suramire.myapplication.NoteDetail;
import com.suramire.myapplication.R;
import com.suramire.myapplication.util.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;


/**
 * Created by Suramire on 2017/6/20.
 */

public class FragmentIndex extends Fragment {

    private ArrayList<String> items;
    private CommonAdapter<String> adapter;
    private ListView listView;
    private Banner banner;
    private boolean flag = false;
    private int firstVisiable = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_index, container, false);
        banner = inflate.findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_menu_camera);
        images.add(R.drawable.ic_menu_gallery);
        images.add(R.drawable.ic_menu_send);
        images.add(R.drawable.ic_menu_manage);
        images.add(R.drawable.ic_menu_share);
        banner.setImages(images);
        banner.start();
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
        swipeRefreshLayout.setProgressViewOffset(true,0,160);
        listView = (ListView) swipeRefreshLayout.findViewById(R.id.index_listview);

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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                int position = listView.getLastVisiblePosition();
//                Log.d("FragmentIndex", "position:" + position);
                if(i>firstVisiable){
//                    firstVisiable = i;
                                        banner.setVisibility(View.GONE);

                }
//                else if(i<firstVisiable){
//                    banner.setVisibility(View.VISIBLE);
//                    firstVisiable = i;
//                }
//                Log.d("FragmentIndex", "i:" + i);
//                Log.d("FragmentIndex", "i1:" + i1);
//                Log.d("FragmentIndex", "i2:" + i2);
//                if(position== (i2-1)&&!flag){
//                    flag = true;
//                    Log.d("FragmentIndex", "true");
//                    banner.setVisibility(View.GONE);
//                }
            }
        });

        return inflate;
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay();
    }
}
