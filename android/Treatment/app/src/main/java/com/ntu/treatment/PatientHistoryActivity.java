package com.ntu.treatment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.adapter.PatientHistoryAdapter;
import com.ntu.treatment.util.GetUrl;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PatientHistoryActivity extends AppCompatActivity {

    private TextView txt_name;
    private TextView txt_time;
    private TextView txt_kind;
    private TextView txt_content;
    private TextView txt_myDoctor;
    private Button bt_back;
    private String username;
    private String submitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);
        txt_name = findViewById(R.id.txt_name);
        txt_time = findViewById(R.id.txt_time);
        txt_kind = findViewById(R.id.txt_kind);
        txt_content = findViewById(R.id.txt_content);
        txt_myDoctor = findViewById(R.id.txt_myDoctor);
        bt_back = findViewById(R.id.bt_back);

        /**
         * 接收contactFragment
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle_pa");
        submitTime = bundle.getString("submitTime");
        username = bundle.getString("username");

        String url = GetUrl.url + "/user/patientConInfo";
        Android_Async_Http_Post(url);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                bundle.putString("flag","1");
                Intent intent = new Intent();
                intent.putExtra("bundles",bundle);
                intent.setClass(PatientHistoryActivity.this,FirstPageActivity.class);
                startActivity(intent);
            }
        });
    }

    //Post请求
    private void Android_Async_Http_Post(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",username);
        params.put("submitTime",submitTime);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                showResponse(response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //Toast.makeText(, "Post请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResponse(JSONObject response){
        //Activity不允许在子线程中进行UI操作
        //通过该方法可以将线程切换到主线程，然后再更新UI元素
        runOnUiThread(new Runnable() {
            String flag = null;
            @Override
            public void run() {
                try {
                    flag = response.getString("flag");
                    if (flag.equals("true")){
                        txt_name = findViewById(R.id.txt_name);
                        txt_time = findViewById(R.id.txt_time);
                        txt_kind = findViewById(R.id.txt_kind);
                        txt_content = findViewById(R.id.txt_content);
                        txt_myDoctor = findViewById(R.id.txt_myDoctor);
                        txt_name.setText("名称： "+response.getString("username"));
                        txt_time.setText("咨询时间： "+response.getString("submitTime"));
                        txt_kind.setText("病况类别： "+response.getString("patientKind"));
                        txt_content.setText("详细内容： "+response.getString("patientCondition"));
                        txt_myDoctor.setText("咨询医生： "+response.getString("myDoctor"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}