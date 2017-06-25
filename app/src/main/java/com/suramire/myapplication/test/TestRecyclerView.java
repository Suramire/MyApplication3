package com.suramire.myapplication.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.suramire.myapplication.R;

import java.util.ArrayList;

/**
 * Created by Suramire on 2017/6/20.
 */

public class TestRecyclerView extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_recyclerview);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview212);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add("item "+ i);
        }
        recyclerView.setAdapter(new CommonRecyclerAdapter<String>(TestRecyclerView.this,R.layout.item_index,items) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, String item, int position) {
                helper.setText(R.id.index_title, item);
            }
        });

    }
}
