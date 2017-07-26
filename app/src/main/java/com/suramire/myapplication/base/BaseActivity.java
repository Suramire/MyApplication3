package com.suramire.myapplication.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.ButterKnife;

/**
 * Created by Suramire on 2017/6/23.
 * 带返回按钮的Activity
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        initView();
        initActionBar();
        super.onCreate(savedInstanceState);

    }
//    获取显示的布局id
    protected abstract int getContentViewId();
//    设置actionbar 带返回按钮
    protected  void initActionBar(){
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar!=null)
            supportActionBar.setDisplayHomeAsUpEnabled(true);
    }
//    进行控件初始化操作
    protected abstract void initView();

//    点击actionbar箭头返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
}
