package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.util.GetUrl;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class EditPatientInfoActivity extends AppCompatActivity {

    private EditText ed_review_password;
    private EditText ed_review_sex;
    private EditText ed_review_age;
    private EditText ed_review_hobby;
    private EditText ed_review_phone;
    private Button ed_review_submit;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_info);
        ed_review_password = findViewById(R.id.ed_review_password);
        ed_review_sex = findViewById(R.id.ed_review_sex);
        ed_review_age = findViewById(R.id.ed_review_age);
        ed_review_hobby = findViewById(R.id.ed_review_hobby);
        ed_review_phone = findViewById(R.id.ed_review_phone);
        ed_review_submit = findViewById(R.id.ed_review_submit);

        /**
         * 接收username
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle_editInfo");
        username = bundle.getString("username");
        System.out.println(username);

        ed_review_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = GetUrl.url+"/user/reviewPatientInfo";
                Android_Async_Http_Post(url);
            }
        });
    }

    //Post请求
    private void Android_Async_Http_Post(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",username);
        params.put("password",ed_review_password.getText().toString());
        params.put("sex",ed_review_sex.getText().toString());
        params.put("age",ed_review_age.getText().toString());
        params.put("hobby",ed_review_hobby.getText().toString());
        params.put("phone",ed_review_phone.getText().toString());
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                showResponse(response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                /*Toast.makeText(, "Post请求失败！", Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    private void showResponse(JSONObject response){
        //Activity不允许在子线程中进行UI操作
        //通过该方法可以将线程切换到主线程，然后再更新UI元素
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*tv.setText(response);*/
                System.out.println(response);
                String flag = null;
                try {
                    flag = response.getString("flag");
                    if (flag.equals("true")){
                        Bundle bundle = new Bundle();
                        bundle.putString("username",username);
                        bundle.putString("flag","2");
                        Intent intent = new Intent();
                        intent.putExtra("bundles",bundle);
                        intent.setClass(EditPatientInfoActivity.this,FirstPageActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}