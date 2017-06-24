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
import android.widget.ListView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.suramire.myapplication.R;
import com.suramire.myapplication.test.Student;
import com.suramire.myapplication.util.Constant;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Suramire on 2017/6/20.
 */

public class FragmentRecommend extends Fragment {
    @Bind(R.id.recommend_listview)
    ListView recommendListview;
    private CommonAdapter<Student> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_second, container, false);
        ButterKnife.bind(this, view);
        final MyHandler myHandler = new MyHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String query0 = URLEncoder.encode("2", "utf-8");//设置编码
                    URL url1 = new URL(Constant.BASEURL + "bbs/Guess?query="+query0);
                    String s = Constant.BASEURL + "bbs/Guess?query="+query0;
                    Log.d("FragmentRecommend", s);
                    HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
                    ObjectInputStream objectInputStream = new ObjectInputStream(urlConnection.getInputStream());
                    Object o = objectInputStream.readObject();//读取对象
                    List<Note> notes = (List<Note>) o;
                    if(notes.size()>0){
                        Message message = Message.obtain();
                        message.what = Constant.SHOWRESULT;
                        message.obj =notes;
                        myHandler.sendMessage(message);
                    }


                } catch (MalformedURLException e) {
                    Log.e("SearchActivity", "MalformedURLException:" + e);
                } catch (IOException e) {
                    Log.e("SearchActivity", "IOException:" + e);
                } catch (ClassNotFoundException e) {
                    Log.e("SearchActivity", "ClassNotFoundException:" + e);
                }catch (Exception e){
                    Log.e("SearchActivity", "Exception:" + e);
                }
            }
        }).start();

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
            switch (msg.what){
                case Constant.SHOWRESULT:{
                    final List<Note> notes = (List<Note>) msg.obj;
                    recommendListview.setAdapter(new CommonAdapter<Note>(getActivity(),R.layout.item_recommend,notes) {
                        @Override
                        public void onUpdate(BaseAdapterHelper helper, Note item, int position) {
                            helper.setText(R.id.recommend_type, item.getType())
                                    .setText(R.id.recommend_textView2, item.getTitle())
                                    .setText(R.id.recommend_textView4, item.getContent())
                                    .setText(R.id.recommend_textView5, item.getCount() + "");
                        }
                    });
                }break;
            }

        }
    }
}
