package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.util.GetUrl;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class RegisterDoctorActivity extends AppCompatActivity {

    private Spinner spinner;
    private static final String[] data_list = {"耳鼻喉科","眼科","皮肤科","口腔科","外伤"};
    private ArrayAdapter<String> arr_adapter;
    private Button btn_doctor_register;
    private String username;
    private String password;
    private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);
        spinner = (Spinner) findViewById(R.id.spinner);
        btn_doctor_register = findViewById(R.id.btn_submit_kind);
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        username = bundle.getString("username");
        password = bundle.getString("password");
        System.out.println("Yonghu"+bundle.getString("username"));
        System.out.println("密码"+bundle.getString("password"));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(data_list[position]);
                category = data_list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("没反应");
            }
        });

        btn_doctor_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_submit_kind: {
                        String url = GetUrl.url + "/user/doctorRegister";
                        Android_Async_Http_Post(url);
                        break;
                    }
                }
            }
        });
    }

    //Post请求
    private void Android_Async_Http_Post(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);
        params.put("category",category);
        System.out.println("这是用户名和密码："+username);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                showResponse(new String(responseBody));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(RegisterDoctorActivity.this, "失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResponse(final String response){
        //Activity不允许在子线程中进行UI操作
        //通过该方法可以将线程切换到主线程，然后再更新UI元素
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*tv.setText(response);*/
                System.out.println(response);
                if (response.equals("true")){
                    Intent intent = new Intent(RegisterDoctorActivity.this,MainActivity.class);
                    startActivity(intent);
                    System.out.println("哈哈哈哈哈哈哈");
                }else {
                    Toast.makeText(RegisterDoctorActivity.this, "请求错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}