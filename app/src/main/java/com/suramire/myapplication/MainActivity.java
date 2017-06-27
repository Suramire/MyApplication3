package com.suramire.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.activity.PhotoSelectActicity;
import com.suramire.myapplication.activity.SearchActivity;
import com.suramire.myapplication.activity.SettingsActivity;
import com.suramire.myapplication.activity.SystemSettingsActivity;
import com.suramire.myapplication.activity.TestActivity;
import com.suramire.myapplication.fragment.FragmentIndex;
import com.suramire.myapplication.fragment.FragmentNotification;
import com.suramire.myapplication.fragment.FragmentRecommend;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.JsonUtil;
import com.suramire.myapplication.util.L;
import com.suramire.myapplication.util.SPUtils;
import com.suramire.myapplication.view.MyViewPager;
import com.xmut.sc.entity.User;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.suramire.myapplication.util.Constant.URL;
import static com.suramire.myapplication.util.Constant.userName;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbarLayout)
    AppBarLayout appbarLayout;
    @Bind(R.id.viewpager)
    MyViewPager viewpager;
    @Bind(R.id.bottomnavigationview)
    BottomNavigationView bottomnavigationview;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private View view;
    private TextView username_textview;
    private ImageView user_img;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        String ip =(String) SPUtils.get(this,"ip","10.0.2.2");
        String port = (String) SPUtils.get(this,"port","8080");
        Constant.BASEURL ="http://"+ip+":"+port+"/";
        L.e("ip"+Constant.BASEURL);
        //为抽屉菜单添加头部
        view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        username_textview = view.findViewById(R.id.index_username_tv);
        user_img = view.findViewById(R.id.imageView);
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "未登录跳转到登录界面,已登录弹出头像选择框", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, PhotoSelectActicity.class));
            }
        });
//        SPUtils.put(this,"uid",1);
        //先判断是否登录
        uid = (int) SPUtils.get(this, "uid", 0);
        if (uid > 0) {
            L.e("已登录@Main");
            //读取用户在线信息并显示
            // TODO: 2017/6/26  本地存入的是  uid
            // FIXME: 2017/6/26  用户表username字段唯一约束
            HTTPUtil.getCall(URL + "getUser&uid=" + uid, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String jsonString = response.body().string();
                    if(!jsonString.isEmpty()){
                        User user = (User) JsonUtil.jsonToObject(jsonString, User.class);
                        userName = user.getUsername();
                        username_textview.setText(userName);
                        username_textview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //todo 点击用户名跳转到个人中心
                            }
                        });
                        Bitmap bitmap = BitmapFactory.decodeFile(Constant.PICTUREPATH + "default.png");//todo 默认头像
                        user_img.setImageBitmap(bitmap);
                    }
                    //// TODO: 获取结果为空时的异常处理
                }
            });

            //获取发帖数与回帖数 根据uid
            HTTPUtil.getCall(URL + "getUserReceiveCount&uid=" + uid, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String string = response.body().string();
                    //这里获取登录用户的发帖数
                    if(!string.isEmpty()){
                        String[] split = string.split(Constant.SPLIT);
                        Menu menu = navigationView.getMenu();
                        menu.getItem(0).setTitle("发帖数:"+split[0]);
                        menu.getItem(1).setTitle("回帖数:"+split[1]);
                    }


                }
            });

        } else {
            username_textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo 跳转到登录界面
                }
            });
            L.e("未登录@Main");
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "这里响应发帖操作", Toast.LENGTH_SHORT).show();
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

// TODO: 2017/6/25 只查询分享的帖子

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
                switch (item.getItemId()) {
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
     *
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
        } else if (id == R.id.nav_replycount) {
            Toast.makeText(this, "跳转到个人回复帖子列表", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_test) {
            startActivity(new Intent(MainActivity.this, TestActivity.class));
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_systemsetting) {
            startActivity(new Intent(MainActivity.this, SystemSettingsActivity.class));
        } else if (id == R.id.nav_signout) {
            if(uid ==0){
                //未登录
                Toast.makeText(this, "还未登录", Toast.LENGTH_SHORT).show();
            }else{
                SPUtils.remove(this,"uid");
                Constant.isDestory = true;
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }

        } else if (id == R.id.nav_quit) {
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
