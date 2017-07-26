package com.suramire.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.suramire.myapplication.R;
import com.suramire.myapplication.mvp.BaseView;
import com.suramire.myapplication.mvp.PresenterNotification;
import com.suramire.myapplication.util.DateUtil;
import com.suramire.myapplication.util.L;
import com.xmut.sc.entity.Note;

import java.util.List;

/**
 * Created by Suramire on 2017/6/20.
 */

public class FragmentNotification extends Fragment implements BaseView {
//    @Bind(R.id.tv_empty)
//    TextView tvEmtpy;
    private ListView listView;
    private View view;
    private PresenterNotification presenterNotification;
    //    private CommonAdapter<Note> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if ( view !=null) {
           return  view;
        }else {
            view = inflater.inflate(R.layout.activity_notification, container, false);
            listView = view.findViewById(R.id.notificationlistview);
            listView.setEmptyView(view.findViewById(R.id.tv_empty));
//        ButterKnife.bind(this, view);
            presenterNotification = new PresenterNotification(this);
            presenterNotification.getNotification();
            return view;
        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        //根据帖子编号随机查询帖子
//        super.setUserVisibleHint(isVisibleToUser);
//    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        L.e("推送onResume");
        //// TODO: 2017/6/28 在此加载通知推送
        super.onResume();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void cancelLoading() {

    }

    @Override
    public void showSuccessful(Object data) {
        L.e("showSuccessful:" + data);
        listView.setAdapter(new CommonAdapter<Note>(getActivity(), R.layout.activity_notification_item, (List<Note>) data) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, Note item, int position) {
                String[] spl = item.getType().split(",");
//                                        L.e(spl[0] + spl[1]);
                helper.setText(R.id.n_title, item.getTitle())
                        .setText(R.id.n_content, item.getContent())
                        .setText(R.id.n_username, spl[1])
                        .setText(R.id.n_publishtime, DateUtil.dateToString(item.getPublishtime()) + "")
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

    @Override
    public void showFail() {

    }
}
