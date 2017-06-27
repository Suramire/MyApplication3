package com.suramire.myapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.test.Student;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.JsonUtil;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Suramire on 2017/6/20.
 */

public class FragmentRecommend extends Fragment {
    @Bind(R.id.recommend_listview)
    ListView recommendListview;
    @Bind(R.id.recommend_empty)
    TextView recommendEmpty;
    private CommonAdapter<Student> adapter;
    String TAG = "FragmentRecommend";
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.activity_recommned, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        recommendListview.setEmptyView(recommendEmpty);
        int uid = (int)SPUtils.get(activity, "uid", 0);
        if (uid>0) {
            Log.d(TAG, "isLogin: ");
            // TODO: 2017/6/24 首先获取该用户的浏览记录,得知其常访问的帖子类型,根据类型进行推送

            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;chaset=utf-8"), "hhh");
            Request request = builder.post(requestBody).url(Constant.URLGUESS + "?uid=" + uid).build();
            // TODO: 2017/6/27 将用户名改为uid
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d(TAG, "onFailure: ");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Log.d(TAG, "onResponse: ");
//                    Log.d("FragmentRecommend", response.body().string());
                    final List<Note> notes = JsonUtil.jsonToList(response.body().string(), Note.class);
                    Log.d("FragmentRecommend", "notes.size():" + notes.size());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recommendListview.setAdapter(new CommonAdapter<Note>(getActivity(), R.layout.item_recommend, notes) {
                                @Override
                                public void onUpdate(BaseAdapterHelper helper, Note item, int position) {
                                    helper.setText(R.id.recommend_type, item.getType())
                                            .setText(R.id.recommend_title, item.getTitle())
                                            .setText(R.id.recommend_content, item.getContent())
                                            .setText(R.id.recommend_count, item.getCount() + "");
                                }
                            });
                        }
                    });
                }
            });

        } else {
            Log.d(TAG, "未登录状态");
        }


        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SHOWRESULT: {
                    final List<Note> notes = (List<Note>) msg.obj;
                    recommendListview.setAdapter(new CommonAdapter<Note>(getActivity(), R.layout.item_recommend, notes) {
                        @Override
                        public void onUpdate(BaseAdapterHelper helper, Note item, int position) {
                            helper.setText(R.id.recommend_type, item.getType())
                                    .setText(R.id.recommend_title, item.getTitle())
                                    .setText(R.id.recommend_content, item.getContent())
                                    .setText(R.id.recommend_count, item.getCount() + "");
                        }
                    });
                }
                break;
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
    }
}
