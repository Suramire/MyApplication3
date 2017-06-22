package com.suramire.myapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.suramire.myapplication.R;
import com.suramire.myapplication.test.Student;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Suramire on 2017/6/20.
 */

public class FragmentSecond extends Fragment {
    @Bind(R.id.recommend_listview)
    ListView recommendListview;
    private CommonAdapter<Student> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_second, container, false);
        ButterKnife.bind(this, view);
        ArrayList<Student> students = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student student = new Student("学生"+i,i+20);
            students.add(student);
        }
//        TextView textView = new TextView(getActivity());
//        textView.setText("没有更多内容咯");
//        recommendListview.setEmptyView(textView);
        adapter = new CommonAdapter<Student>(getActivity(), R.layout.item_recommend, students) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, Student item, final int position) {
                helper.setText(R.id.recommend_textView2, item.getName());
                helper.setText(R.id.recommend_textView5, item.getAge() + "");
                helper.setOnClickListener(R.id.recommend_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyHandler myHandler = new MyHandler();
                        Message message = Message.obtain();
                        message.arg1 = position;
                        myHandler.sendMessage(message);
                    }
                });
                helper.setOnClickListener(R.id.recommend_textView5, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("FragmentSecond", "t5 clicked");

                    }
                });
            }
        };

        recommendListview.setAdapter(adapter);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    class  MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            adapter.remove(msg.arg1);
            adapter.notifyDataSetChanged();
        }
    }
}
