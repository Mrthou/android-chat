package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import com.loopj.android.http.*;
import com.ntu.treatment.util.GetUrl;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences mContextSp;
    private Context mContext;
    private RelativeLayout rl_user;
    private Button mLoginButton;
    private Button mRegisterButton;
    private EditText mAccount;
    private EditText mPassword;
    private HashMap<String, String> stringHashMap;
    private RadioGroup radioGroup_login;
    private RadioButton radio_login_doctor;
    private RadioButton radio_login_patient;
    private String checked_kinds;

    String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        rl_user = (RelativeLayout) findViewById(R.id.rl_user);
        mLoginButton = (Button) findViewById(R.id.login);
        mRegisterButton = (Button) findViewById(R.id.register);
        mAccount = findViewById(R.id.account);
        mPassword = (EditText) findViewById(R.id.password);
        radioGroup_login = findViewById(R.id.login_radio);
        radio_login_doctor = findViewById(R.id.radio_login_doctor);
        radio_login_patient = findViewById(R.id.radio_login_patient);

        stringHashMap = new HashMap<>();
        mLoginButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
        radioGroup_login.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radio_login_doctor.getId() == checkedId){
                    checked_kinds = radio_login_doctor.getText().toString();
                }else {
                    checked_kinds = radio_login_patient.getText().toString();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login: {
                if (checked_kinds.equals("??????")){
                    String url = GetUrl.url + "/user/doctorLogin";
                    Doctor_Android_Async_Http_Post(url);
                    break;
                }else if (checked_kinds.equals("??????")){
                    String url = GetUrl.url + "/user/patientLogin";
                    Android_Async_Http_Post(url);
                    break;
                }
            }
            case R.id.register: {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            }
        }
        System.out.println("????????????" + mAccount.getText().toString());

    }

    //Post??????
    private void Doctor_Android_Async_Http_Post(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", mAccount.getText().toString());
        params.put("password", mPassword.getText().toString());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                doctor_showResponse(new String(responseBody));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Post???????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doctor_showResponse(final String response){
        //Activity??????????????????????????????UI??????
        //??????????????????????????????????????????????????????????????????UI??????
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*tv.setText(response);*/
                System.out.println(response);
                if (response.equals("true")){
                    System.out.println("?????????????????????");
                    /**
                     * ??????ChatActivity
                     */
                    Bundle bundle = new Bundle();
                    bundle.putString("username",mAccount.getText().toString());
                    bundle.putString("checked_kinds",checked_kinds);
                    Intent intent = new Intent();
                    intent.putExtra("doctor_bundle_chat",bundle);
                    intent.setClass(MainActivity.this,ChatDoctorActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Post??????
    private void Android_Async_Http_Post(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", mAccount.getText().toString());
        params.put("password", mPassword.getText().toString());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                showResponse(new String(responseBody));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Post???????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResponse(final String response){
        //Activity??????????????????????????????UI??????
        //??????????????????????????????????????????????????????????????????UI??????
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*tv.setText(response);*/
                System.out.println(response);
                if (response.equals("true")){
                    System.out.println("?????????????????????");
                    /***
                     * ?????????FirstPageActivity
                     */
                    Bundle bundle = new Bundle();
                    bundle.putString("username",mAccount.getText().toString());
                    bundle.putString("checked_kinds", checked_kinds);
                    bundle.putString("flag","0");
                    Intent intent = new Intent();
                    intent.putExtra("bundles",bundle);
                    intent.setClass(MainActivity.this,FirstPageActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}