package com.suramire.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.suramire.myapplication.R;
import com.suramire.myapplication.test.Student;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Suramire on 2017/6/20.
 */

public class SearchActivity extends AppCompatActivity {
    @Bind(R.id.search_listview)
    ListView searchListview;
    private List<Student> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student stu = new Student("student"+i, 66);
            data.add(stu);
        }
        searchListview.setAdapter(new CommonAdapter<Student>(SearchActivity.this,R.layout.item_recommend, data) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, Student item, int position) {
                helper.setText(R.id.recommend_textView2, item.getName());
            }
        });
//        ImageView imageView = new ImageView(SearchActivity.this);
//        imageView.setImageResource(R.drawable.ic_menu_manage);
//        searchListview.setEmptyView(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_settings).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Student> mStudent = new ArrayList<Student>();
                for (int i = 0; i <data.size() ; i++) {
                    if(query.equals(data.get(i).getName())){
                        mStudent.add(data.get(i));
                    }
                }
                searchListview.setAdapter(new CommonAdapter<Student>(SearchActivity.this,R.layout.item_recommend,mStudent) {
                    @Override
                    public void onUpdate(BaseAdapterHelper helper, Student item, int position) {
                        helper.setText(R.id.recommend_textView2, item.getName());
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
