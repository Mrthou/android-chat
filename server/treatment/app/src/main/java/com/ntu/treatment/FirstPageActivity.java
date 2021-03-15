package com.ntu.treatment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.util.GetUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FirstPageActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavigationView;
    private String username;

    private List<PatientHistory>  list = new ArrayList<>();

    //先实例化控件
    //来自main_title_bar.xml
    private TextView tv_main_title;//标题
    private TextView tv_back;//返回按钮
    private RelativeLayout title_bar;
    //来自activity_main.xml
    private RelativeLayout ll_frameLayout;
    private TextView bottom_bar_text_1;
    private ImageView bottom_bar_image_1;
    private TextView bottom_bar_text_2;
    private ImageView bottom_bar_image_2;
    private TextView bottom_bar_text_3;
    private ImageView bottom_bar_image_3;
    private LinearLayout main_bottom_bar;
    private RelativeLayout bottom_bar_1_btn;
    private RelativeLayout bottom_bar_2_btn;
    private RelativeLayout bottom_bar_3_btn;

    private int lastIndex;
    List<Fragment> mFragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        initView();
        //initBottomNavigation();
        initData();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundles");
        String flag = bundle.getString("flag");
        if (flag.equals("2")){
            setFragmentPosition(2);
            setSelectStatus(2);
            //R.id.bv_bottomNavigation
        }else if (flag.equals("1")){
            setFragmentPosition(1);
            setSelectStatus(1);
        }/*else if (flag.equals("0")){
            setFragmentPosition(0);
            setSelectStatus(0);
        }*/
    }

    public void initView() {
        mToolbar = findViewById(R.id.toolbar);
        //底部导航栏
        bottom_bar_text_1=findViewById(R.id.bottom_bar_text_1);
        bottom_bar_image_1=findViewById(R.id.bottom_bar_image_1);
        bottom_bar_text_2=findViewById(R.id.bottom_bar_text_2);
        bottom_bar_image_2=findViewById(R.id.bottom_bar_image_2);
        bottom_bar_text_3=findViewById(R.id.bottom_bar_text_3);
        bottom_bar_image_3=findViewById(R.id.bottom_bar_image_3);
        //包含底部 android:id="@+id/main_bottom_bar"
        main_bottom_bar=findViewById(R.id.main_bottom_bar);
        //private RelativeLayout bottom_bar_1_btn;
        bottom_bar_1_btn=findViewById(R.id.bottom_bar_1_btn);
        bottom_bar_2_btn=findViewById(R.id.bottom_bar_2_btn);
        bottom_bar_3_btn=findViewById(R.id.bottom_bar_3_btn);
        //添加点击事件
        bottom_bar_1_btn.setOnClickListener(this);
        bottom_bar_2_btn.setOnClickListener(this);
        bottom_bar_3_btn.setOnClickListener(this);
        //技巧
        //tv_back.setVisibility(View.GONE);
        //tv_main_title.setText("课程");
        //title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));

    }

    private void setSelectStatus(int index) {
        switch (index){
            case 0:
                //图片点击选择变换图片，颜色的改变，其他变为原来的颜色，并保持原有的图片
                bottom_bar_image_1.setImageResource(R.drawable.tabbar_home_press);
                bottom_bar_text_1.setTextColor(Color.parseColor("#0097F7"));
                //其他的文本颜色不变
                bottom_bar_text_2.setTextColor(Color.parseColor("#666666"));
                bottom_bar_text_3.setTextColor(Color.parseColor("#666666"));
                //图片也不变
                bottom_bar_image_2.setImageResource(R.drawable.tabbar_pay_normal);
                bottom_bar_image_3.setImageResource(R.drawable.tabbar_me_normal);

                break;
            case 1://同理如上
                //图片点击选择变换图片，颜色的改变，其他变为原来的颜色，并保持原有的图片
                bottom_bar_image_1.setImageResource(R.drawable.tabbar_home_normal);
                bottom_bar_text_1.setTextColor(Color.parseColor("#666666"));
                //其他的文本颜色不变
                bottom_bar_text_2.setTextColor(Color.parseColor("#0097F7"));
                bottom_bar_text_3.setTextColor(Color.parseColor("#666666"));
                //图片也不变
                bottom_bar_image_2.setImageResource(R.drawable.tabbar_pay_press);
                bottom_bar_image_3.setImageResource(R.drawable.tabbar_me_normal);
                /*setFragmentPosition(1);*/
                break;
            case 2://同理如上
                //图片点击选择变换图片，颜色的改变，其他变为原来的颜色，并保持原有的图片
                bottom_bar_image_1.setImageResource(R.drawable.tabbar_home_normal);
                bottom_bar_text_1.setTextColor(Color.parseColor("#666666"));
                //其他的文本颜色不变
                bottom_bar_text_2.setTextColor(Color.parseColor("#666666"));
                bottom_bar_text_3.setTextColor(Color.parseColor("#0097F7"));
                //图片也不变
                bottom_bar_image_2.setImageResource(R.drawable.tabbar_pay_normal);
                bottom_bar_image_3.setImageResource(R.drawable.tabbar_me_press);
               /* setFragmentPosition(2);*/
                break;
        }
    }

    public void initData() {
        setSupportActionBar(mToolbar);
        mFragments = new ArrayList<>();
        mFragments.add(new MessageFragment());
        mFragments.add(new ContactsFragment());
        mFragments.add(new DiscoverFragment());

        // 初始化展示MessageFragment
        setSelectStatus(0);
        setFragmentPosition(0);
    }

    public void initBottomNavigation() {
        //mBottomNavigationView = findViewById(R.id.bv_bottomNavigation);
        // 解决当item大于三个时，非平均布局问题
        //BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_message:
                        setFragmentPosition(0);
                        break;
                    case R.id.menu_contacts:{
                        setFragmentPosition(1);
                        break;
                    }

                    case R.id.menu_discover:
                        setFragmentPosition(2);
                        break;

                    default:
                        break;
                }
                // 这里注意返回true,否则点击失效
                return true;
            }
        });
    }


    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.ll_frameLayout, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    public String getText(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundles");
        username = bundle.getString("username");
        return username;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottom_bar_1_btn:
                setFragmentPosition(0);
                setSelectStatus(0);
                break;
            case R.id.bottom_bar_2_btn:
                setFragmentPosition(1);
                setSelectStatus(1);
                break;
            case R.id.bottom_bar_3_btn:
                setFragmentPosition(2);
                setSelectStatus(2);
                break;
        }
    }
}