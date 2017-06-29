package com.suramire.myapplication;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.squareup.picasso.Picasso;
import com.suramire.myapplication.activity.PhotoSelectActicity;
import com.suramire.myapplication.activity.SearchActivity;
import com.suramire.myapplication.activity.SettingsActivity;
import com.suramire.myapplication.activity.SystemSettingsActivity;
import com.suramire.myapplication.fragment.FragmentIndex;
import com.suramire.myapplication.fragment.FragmentNotification;
import com.suramire.myapplication.fragment.FragmentRecommend;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.GsonUtil;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.L;
import com.suramire.myapplication.util.SPUtils;
import com.suramire.myapplication.view.MyViewPager;
import com.xmut.sc.entity.User;
import com.zjw.user.LoginActivity;
import com.zxf.scode.Information;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.suramire.myapplication.util.Constant.BASEURL;
import static com.suramire.myapplication.util.Constant.URL;
import static com.suramire.myapplication.util.Constant.userName;
import static com.suramire.myapplication.util.L.e;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int REQUESTCODE =0x0;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
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
    private int index;
    private int[] ids;
    private SearchView searchView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUESTCODE && resultCode==PhotoSelectActicity.CODESUCCESS){
            //这里更新图片
            e("返回结果为success,更新头像");
            user_img.setImageBitmap(BitmapFactory.decodeFile(Constant.PICTUREPATH+ userName+".png"));
        }
        else if(requestCode ==REQUESTCODE && resultCode ==Constant.UPDATESUCCESS){
            e("更新用户信息后重启activity,重新登录");
            SPUtils.put(this,"uid",0);
            Constant.isDestory = true;
            finish();
            startActivity(getIntent());

        }else if(requestCode ==REQUESTCODE && resultCode == Constant.LOGINSUCCESS){
            //登录成功 重启activity
            Constant.isDestory = true;
            finish();
            startActivity(getIntent());
        }
        else{
            e("返回结果为fail,什么都不做");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ids = new int[]{R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications};
        //表示打开应用时该显示哪页 接收推送时值为2即显示通知页
        L.e("viewpager下标:" + index);
        setSupportActionBar(toolbar);
        String ip =(String) SPUtils.get(this,"ip","10.0.2.2");
        String port = (String) SPUtils.get(this,"port","8080");
        BASEURL ="http://"+ip+":"+port+"/";
        URL = BASEURL+"bbs/GetResult?do=";
        e("service ip:" + BASEURL);
        //为抽屉菜单添加头部
        view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        username_textview = view.findViewById(R.id.index_username_tv);
        user_img = view.findViewById(R.id.imageView);

        // TODO: 2017/6/27 添加网络连接判断
//        SPUtils.put(this,"uid",1);
        //先判断是否登录
        uid = (int) SPUtils.get(this, "uid", 0);
        if (uid > 0) {
            e("已登录@Main");
            //读取用户在线信息并显示
            user_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "未登录跳转到登录界面,已登录弹出头像选择框", Toast.LENGTH_SHORT).show();
                    startActivityForResult(new Intent(MainActivity.this, PhotoSelectActicity.class),REQUESTCODE);
                }
            });
            HTTPUtil.getCall(URL + "getUser&uid=" + uid, new Callback() {

                private String userImg;

                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String jsonString = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!TextUtils.isEmpty(jsonString)){
                                final User user = (User) GsonUtil.jsonToObject(jsonString, User.class);
                                Constant.user = user;
                                userName = user.getUsername();
                                userImg = user.getImg();
                                username_textview.setText(userName);
                                username_textview.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("MainActivity", "已登录状态,跳转到个人中心");
                                    }
                                });
                                // TODO: 2017/6/27 若用户有头像则显示用户头像
                                if(!"".equals(user.getImg())){
                                    //有头像,连接服务器获取
                                    e("该用户有头像");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            L.e("图片路径:"+Constant.BASEURL + "bbs/upload/" + userImg);
                                            Picasso.with(MainActivity.this)
                                                    .load(Constant.BASEURL + "bbs/upload/" + userImg)
                                                    .placeholder(R.drawable.def)
                                                    .resize(90,90)
                                                    .centerCrop()
                                                    .into(user_img);
                                        }
                                    });

//                            HTTPUtil.getCall(Constant.BASEURL + "bbs/upload/" + userImg, new Callback() {
//                                @Override
//                                public void onFailure(Request request, IOException e) {
//
//                                }
//
//                                @Override
//                                public void onResponse(Response response) throws IOException {
//                                    InputStream inputStream1 = response.body().byteStream();
//                                    //FIXME 头像获取有问题
//                                    L.e("获取的头像字节流:" + inputStream1);
//                                    if(inputStream1!=null){
//                                        //请求成功时
//                                        File file = new File(Constant.PICTUREPATH,userImg);
//                                        FileOutputStream fileOutputStream = new FileOutputStream(file);
//                                        byte[] buffer = new byte[1024];
//                                        int len =0;
//                                        while ((len=inputStream1.read(buffer))!=-1){
//                                            fileOutputStream.write(buffer,0,len);
//                                        }
//                                        fileOutputStream.flush();
//                                        fileOutputStream.close();
//                                        inputStream1.close();
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Bitmap bitmap = BitmapFactory.decodeFile(Constant.PICTUREPATH +userImg);
////                                                L.e("读取本地图片 高度:"+bitmap.getHeight());
//
//                                                user_img.setImageBitmap(bitmap);
//                                            }
//                                        });
//                                    }
//
//
//                                }
//                            });
                                }else{
                                    e("该用户没有头像");
//                            Bitmap bitmap = BitmapFactory.decodeFile(Constant.PICTUREPATH + "default.png);" +
//                                            "//todo 默认头像
//                            user_img.setImageBitmap(bitmap);

                                }

                            }
                        }
                    });

                    // TODO: 获取结果为空时的异常处理
                }
            });

            //获取发帖数与回帖数 根据uid
            HTTPUtil.getCall(URL + "getUserReceiveCount&uid=" + uid, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String string = response.body().string();
                    //这里获取登录用户的发帖数
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!TextUtils.isEmpty(string)){
                                String[] split = string.split(Constant.SPLIT);
                                Menu menu = navigationView.getMenu();
                                menu.getItem(0).setTitle("发帖数:"+split[0]);
                                menu.getItem(1).setTitle("回帖数:"+split[1]);
                            }
                        }
                    });


                }
            });

        } else {
            //未登录状态
            username_textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class),MainActivity.REQUESTCODE);
//                    Toast.makeText(MainActivity.this, "跳转到登录页面", Toast.LENGTH_SHORT).show();

                }
            });
            e("未登录@Main");
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
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
                        viewpager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_dashboard:
                        viewpager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_notifications:
                        viewpager.setCurrentItem(3);
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
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSelected(false);
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, SearchActivity.class));
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
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
        } else if (id == R.id.nav_history) {
            Toast.makeText(this, "这里跳转到历史记录", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(MainActivity.this, TestActivity.class));
        } else if (id == R.id.nav_share) {
            startActivityForResult(new Intent(MainActivity.this, Information.class),MainActivity.REQUESTCODE);
        } else if (id == R.id.nav_systemsetting) {
            startActivityForResult(new Intent(MainActivity.this, SystemSettingsActivity.class),REQUESTCODE);
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

    @Override
    protected void onPostResume() {
        L.e("onPostResume");
        index = getIntent().getIntExtra("index", 0);
        L.e("index:" + index);
        viewpager.setCurrentItem(index);
        bottomnavigationview.setSelectedItemId(ids[index]);
        super.onPostResume();
    }
}
