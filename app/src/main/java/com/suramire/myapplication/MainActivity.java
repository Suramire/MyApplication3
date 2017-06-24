package com.suramire.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.suramire.myapplication.activity.PhotoSelectActicity;
import com.suramire.myapplication.activity.SearchActivity;
import com.suramire.myapplication.activity.SettingsActivity;
import com.suramire.myapplication.activity.SystemSettingsActivity;
import com.suramire.myapplication.activity.TestActivity;
import com.suramire.myapplication.fragment.FragmentIndex;
import com.suramire.myapplication.fragment.FragmentNotification;
import com.suramire.myapplication.fragment.FragmentRecommend;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.MyDataBase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //先判断是否登录
        MyDataBase myDataBase = new MyDataBase(this,null);
//        long hello = myDataBase.insert("user");
//        Log.d("MainActivity", "hello:" + hello);

//        int delete = myDataBase.delete();
//        Log.d("MainActivity", "delete:" + delete);
        Cursor cursor = myDataBase.selectAll();
        int count =  cursor.getCount();
        if(count>0){
            Constant.isLogin = true;
            while (cursor.moveToNext()){
                Constant.userName = cursor.getString(cursor.getColumnIndex("username"));
            }
        }
        Log.d("MainActivity", "myDataBase.selectAll().getCount():" + count);
        myDataBase.close();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        );
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Toast.makeText(MainActivity.this, "这里响应发帖操作", Toast.LENGTH_SHORT).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
//        toggle.setDrawerIndicatorEnabled(false);


        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView textView =  view.findViewById(R.id.index_username_tv);
        ImageView imageview = view.findViewById(R.id.imageView);
        if(Constant.isLogin){
            Bitmap bitmap = BitmapFactory.decodeFile(Constant.PICTUREPATH + "username.png");
            imageview.setImageBitmap(bitmap);
            textView.setText(Constant.userName);
        }
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "未登录跳转到登录界面,已登录弹出头像选择框", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, PhotoSelectActicity.class));
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        final Fragment fragmentindex = new FragmentIndex();
        Fragment fragment = new FragmentRecommend();
        Fragment fragment3 = new FragmentNotification();
        final ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(fragmentindex);
        fragments.add(fragment);
        fragments.add(fragment3);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        //底部导航事件
        BottomNavigationView bottomnavigationview = (BottomNavigationView) findViewById(R.id.bottomnavigationview);
        bottomnavigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("MainActivity", "item:" + item);
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_dashboard:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_notifications:
                        viewPager.setCurrentItem(3);
                        return true;
                }
                return true;
            }
        });
    }

    /**
     * 按返回键收起左边抽屉导航
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //跳转到搜索页
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 抽屉菜单的item点击事件
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation MyViewPager item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_sentcount) {
            Toast.makeText(this, "跳转到个人发表的帖子列表", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_replycount){
            Toast.makeText(this, "跳转到个人回复帖子列表", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_test) {
            startActivity(new Intent(MainActivity.this, TestActivity.class));
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_systemsetting) {
            startActivity(new Intent(MainActivity.this, SystemSettingsActivity.class));
        }else if(id == R.id.nav_signout){
            System.exit(0);
        }
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
