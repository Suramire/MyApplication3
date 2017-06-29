package com.zlw;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suramire.myapplication.R;
import com.xmut.sc.entity.Note;

import java.util.ArrayList;

/**
 * Created by zlw on 2017/6/28.
 */

public class MyBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Note> list;
    public MyBaseAdapter(Context context, ArrayList<Note> list){

        this.context=context;
        this.list=list;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {


        ViewHolder viewHolder=null;
        if(convertView==null){

            viewHolder=new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_posted,null);
            viewHolder.title= (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);

        }
        if(convertView!=null){
            Log.i("abc",list.get(i).getTitle().toString());
            viewHolder= (ViewHolder) convertView.getTag();

            viewHolder.title.setText(list.get(i).getTitle().toString());


        }


        return convertView;
    }



static class ViewHolder{

    public TextView title;

}

}
