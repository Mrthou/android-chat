package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.util.GetUrl;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private RadioButton radio_patient;
    private RadioButton radio_doctor;
    private Button btn_register;
    private RadioGroup radioGroup_register;
    private static String checked_kind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.ReUserName);
        password = findViewById(R.id.RePassword);
        btn_register = findViewById(R.id.btn_register);
        radio_patient = findViewById(R.id.kind_patient);
        radio_doctor = findViewById(R.id.kind_doctor);
        radioGroup_register = findViewById(R.id.rg_kind);
        radioGroup_register.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radio_patient.getId() == checkedId){
                    checked_kind = radio_patient.getText().toString();
                }else {
                    checked_kind = radio_doctor.getText().toString();
                }
            }
        });
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register: {
                if (checked_kind.equals("患者")){
                    System.out.println("这是huanzhe："+checked_kind);
                    System.out.println("这是用户名和密码："+password.getText().toString());
                    String url = GetUrl.url + "/user/patientRegister";
                    Android_Async_Http_Post(url);
                    break;
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("username",username.getText().toString());
                    bundle.putString("password",password.getText().toString());
                    Intent intent = new Intent();
                    intent.putExtra("bundle",bundle);
                    intent.setClass(RegisterActivity.this,RegisterDoctorActivity.class);
                    startActivity(intent);
                    break;
                }

            }
        }
    }

    //Post请求
    private void Android_Async_Http_Post(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());
        System.out.println("这是用户名和密码："+username.getText().toString());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                showResponse(new String(responseBody));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(RegisterActivity.this, "失败！", Toast.LENGTH_SHORT).show();
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
                    System.out.println("哈哈哈哈哈哈哈");
                    /***
                     * 发送到FirstPageActivity
                     */
                    Bundle bundle = new Bundle();
                    bundle.putString("username",username.getText().toString());
                    bundle.putString("checked_kinds", checked_kind);
                    bundle.putString("flag","0");
                    Intent intent = new Intent();
                    intent.putExtra("bundles",bundle);
                    intent.setClass(RegisterActivity.this,FirstPageActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(RegisterActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}