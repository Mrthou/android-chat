package com.ntu.treatment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.util.GetUrl;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * @author Evan_zch
 * @date 2018/8/23 20:38
 */
public class MessageFragment extends Fragment{

    private Spinner mySpinner;
    private ArrayAdapter<String> arr_adapter;
    private EditText ed_message;
    private Button btn_message_submit;
    private static String category;
    private String username;
    private static final String[] data_list1 = {"耳鼻喉科","眼科","皮肤科","口腔科","外伤"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        mySpinner = (Spinner)view.findViewById(R.id.message_spinner);
        btn_message_submit = (Button)view.findViewById(R.id.btn_submit_patient);
        ed_message = view.findViewById(R.id.ed_patient_condition);
        //适配器
        arr_adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data_list1);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        mySpinner.setAdapter(arr_adapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(data_list1[position]);
                category = data_list1[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("没反应");
            }
        });

        btn_message_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = GetUrl.url + "/user/addPatientCondition";
                Android_Async_Http_Post(url);
            }
        });
        return view;
    }

    //Post请求
    private void Android_Async_Http_Post(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("patientKind",category);
        params.put("username",username);
        params.put("patientCondition",ed_message.getText().toString());
        System.out.println("病况"+ed_message.getText().toString());
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*tv.setText(response);*/
                System.out.println(response);
                String flag = null;
                String toUserName = null;
                try {
                    flag = response.getString("flag");
                    toUserName = response.getString("toUserName");
                    System.out.println("这是后台"+flag);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (flag.equals("true")){
                    System.out.println("哈哈哈哈哈哈哈");
                    /**
                     * 传到ChatActivity
                     */
                    Bundle bundle = new Bundle();
                    bundle.putString("username",username);
                    bundle.putString("toUserName",toUserName);
                    Intent intent = new Intent();
                    intent.putExtra("bundle_chat",bundle);
                    intent.setClass(getContext(),ChatActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        username = ((FirstPageActivity) context).getText();
        System.out.println("****"+username);
        super.onAttach(context);
    }
}