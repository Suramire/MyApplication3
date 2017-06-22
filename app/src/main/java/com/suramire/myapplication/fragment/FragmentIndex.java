package com.suramire.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.suramire.myapplication.NoteDetail;
import com.suramire.myapplication.R;



import java.util.ArrayList;


/**
 * Created by Suramire on 2017/6/20.
 */

public class FragmentIndex extends Fragment {

    private ArrayList<String> items;
    private CommonRecyclerAdapter<String> adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_index, container, false);
        final SwipeRefreshLayout swipeRefreshLayout = inflate.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //模拟刷新数据 2017-6-20 11:13:20

                            items.add(0,"new item " + 00);
                             adapter.notifyDataSetChanged();
//                            adapter.notifyItemInserted(2);
//                            adapter.notifyItemRangeChanged(2,items.size()-2);
                        //通知数据更新
                        Toast.makeText(getActivity(), "找到1条内容", Toast.LENGTH_SHORT).show();

                        adapter = new CommonRecyclerAdapter<String>(getActivity(),R.layout.item_index,items) {
                            @Override
                            public void onUpdate(BaseAdapterHelper helper, String item, int position) {
                                helper.setText(R.id.textView2, item);

                            }
                        };
                        recyclerView.setAdapter(adapter);
//                        recyclerView.setAdapter(adapter);
                        //取消进度圈
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });
        swipeRefreshLayout.setProgressViewOffset(true,0,160);
        recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add("item "+ i);
        }
        adapter = new CommonRecyclerAdapter<String>(getActivity(), R.layout.item_index, items) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, String item, int position) {
                helper.setText(R.id.textView2, item);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
                startActivity(new Intent(getActivity(), NoteDetail.class));
            }
        });
        return inflate;
    }
}
