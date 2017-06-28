package com.suramire.myapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.activity.NoteDetailActivity;
import com.suramire.myapplication.test.Student;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.GsonUtil;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.L;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import static com.suramire.myapplication.util.Constant.URL;



/**
 * Created by Suramire on 2017/6/20.
 */

public class FragmentRecommend extends Fragment {
    @Bind(R.id.recommend_listview)
    SwipeMenuListView recommendListview;
    @Bind(R.id.recommend_empty)
    TextView recommendEmpty;
    private CommonAdapter<Student> adapter;
    String TAG = "FragmentRecommend";
    private Activity activity;
    private int uid;
    private List<Note> mnotes;//存放推荐帖子

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.activity_recommned, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        uid = (int) SPUtils.get(activity, "uid", 0);
        recommendListview.setEmptyView(recommendEmpty);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
//                 create "open" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(270);
                // set item title
                deleteItem.setTitle("不喜欢");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);

                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(
//                        getActivity());
//                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                        0x3F, 0x25)));
//                // set item width
//
//                deleteItem.setWidth(270);
//                // set a icon
//                deleteItem.setIcon(R.mipmap.ic_launcher);
//                // add to menu
//                menu.addMenuItem(deleteItem);
            }
        };
        recommendListview.setMenuCreator(creator);
        recommendListview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        L.e("这里响应删除操作: index:" + index + " position:" + position);
                        // TODO: 2017/6/27 添加不喜欢 发送给服务器
                        HTTPUtil.getCall(URL + "dislike&uid=" + uid + "&nid=" + mnotes.get(position).getNid(), new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {

                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                L.e("不喜欢帖子结果:" + response.body().string());
                            }
                        });
                        Toast.makeText(activity, "已为您减少该类型内容", Toast.LENGTH_SHORT).show();
                        mnotes.remove(position);
                        updateListview();
                        break;

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        if (uid >0) {
            //滑动菜单操作
            recommendEmpty.setText("暂时没有推荐内容,多看帖已便我们推荐");
            // FIXME: 2017/6/24 对获取的帖子数量进行限制
            HTTPUtil.getCall(Constant.URL + "guess&uid=" + uid,new Callback() {



                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d(TAG, "onFailure: ");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Log.d(TAG, "onResponse: ");
//                    Log.d("FragmentRecommend", response.body().string());
                    mnotes = GsonUtil.jsonToList(response.body().string(), Note.class);
                    if(mnotes.size()>0){
                        Log.d("FragmentRecommend", "mnotes.size():" + mnotes.size());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateListview();

//
                                // TODO: 2017/6/27 帖子是否分享判断
                            }
                        });
                    }

                }


            });


        } else {
            recommendEmpty.setText("登录以便查看推荐内容");
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

    private void updateListview() {
        recommendListview.setAdapter(new CommonAdapter<Note>(getActivity(), R.layout.item_recommend, mnotes) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, Note item, final int position) {
                helper.setText(R.id.recommend_type, item.getType())
                        .setText(R.id.recommend_title, item.getTitle())
                        .setText(R.id.recommend_content, item.getContent())
                        .setText(R.id.recommend_count, item.getCount() + "");
                helper.setOnClickListener(R.id.recommend_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("note", mnotes.get(position));
                        startActivity(new Intent(activity, NoteDetailActivity.class).putExtras(bundle));
                    }
                }).setOnClickListener(R.id.recommend_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("note", mnotes.get(position));
                        startActivity(new Intent(activity, NoteDetailActivity.class).putExtras(bundle));
                    }
                });
            }

        });
    }
}
