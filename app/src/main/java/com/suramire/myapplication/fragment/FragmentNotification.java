package com.suramire.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.L;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Suramire on 2017/6/20.
 */

public class FragmentNotification extends Fragment {
    @Bind(R.id.tv_empty)
    TextView tvEmtpy;
    private ListView listView;
    private CommonAdapter<Note> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notification, container, false);
        listView = view.findViewById(R.id.notificationlistview);
        listView.setEmptyView(view.findViewById(R.id.tv_empty));
        //根据帖子编号随机查询帖子
        double r = Math.random() * 100;
        try {
//            random((int)r);
            random(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * Created by Weeks Wei on 2017/6/26.
     * 通知随机显示一条帖子数据
     * random()向服务器请求查询一条数据
     *
     * @param random 帖子的ID
     */
    private void random(int random) throws IOException {
        //1.拿到okHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
//        Request request = builder.get().url(Constant.URLNOTIFICATION + "?nid=" + random + "&operation=randomQuery").build();
        Request request = builder.get().url(Constant.URL + "randomQuery&nid=" + random).build();
        //3.将Request封装为Call
        Call call = okHttpClient.newCall(request);
        L.e(Constant.URLNOTIFICATION + "?nid=" + random + "&operation=randomQuery");
        //4.执行call
//        Response response = call.execute();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
//                Toast.makeText(getApplicationContext(),"连接服务器失败，请重试",Toast.LENGTH_LONG).show();
                L.e("onFailure:" + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
//                Toast.makeText(getApplicationContext(),"获得数据："+response,Toast.LENGTH_LONG).show();
                L.e("onResponse:");
                String res = response.body().string();
//                L.e(res);


                if (res != null) {
                    //截取
                    String part1 = res.substring(0, res.indexOf("?"));
                    String part2 = res.substring(res.indexOf("?") + 1);
                    L.e("part1:" + part1);
                    L.e("part2:" + part2);
                    if (!part2.equals("{}")) {

                        // android 自带json
                        Note note = new Note();

                        Gson gson = new Gson();


                        note = gson.fromJson(part2, Note.class);

                        final List<Note> noteList = new ArrayList<Note>();
                        noteList.add(note);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setAdapter(new CommonAdapter<Note>(getActivity(), R.layout.activity_notification_item, noteList) {
                                    @Override
                                    public void onUpdate(BaseAdapterHelper helper, Note item, int position) {
                                        String[] spl = item.getType().split(",");
//                                        L.e(spl[0] + spl[1]);
                                        helper.setText(R.id.n_title, item.getTitle())
                                                .setText(R.id.n_content, item.getContent())
                                                .setText(R.id.n_username, spl[1])
                                                .setText(R.id.n_publishtime, item.getPublishtime() + "")
                                                .setText(R.id.n_count, "浏览量：" + item.getCount())
                                                .setText(R.id.n_type, spl[0] + " " + item.getTag());
                                        helper.setOnClickListener(R.id.n_content, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                L.e("点击了内容");
                                            }
                                        });
                                    }
                                });
                            }
                        });

                    }
                }

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        L.e("推送onResume");
        //// TODO: 2017/6/28 在此加载通知推送
        super.onResume();
    }
}
