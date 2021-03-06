package com.ntu.treatment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.util.PrimitiveIterator;

import cz.msebera.android.httpclient.Header;

/**
 * @author Evan_zch
 * @date 2018/8/23 20:41
 */
public class DiscoverFragment extends Fragment {
    private TextView text;
    private ImageView bt_review_me;
    private TextView myPage_name;
    private TextView myPage_sex;
    private TextView myPage_age;
    private TextView myPage_contactNum;
    private TextView myPage_hobby;
    private Button bt_exit;
    private Button bt_delete;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        bt_review_me = view.findViewById(R.id.bt_review_me);
        myPage_age = view.findViewById(R.id.myPage_age);
        myPage_contactNum = view.findViewById(R.id.myPage_contactNum);
        myPage_hobby = view.findViewById(R.id.myPage_hobby);
        myPage_name = view.findViewById(R.id.myPage_name);
        myPage_sex = view.findViewById(R.id.myPage_sex);
        bt_exit = view.findViewById(R.id.bt_exit);
        bt_delete = view.findViewById(R.id.bt_delete);
        String url = GetUrl.url + "/user/patientAllInfo";
        Android_Async_Http_Post(url);

        bt_review_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                Intent intent = new Intent();
                intent.putExtra("bundle_editInfo",bundle);
                intent.setClass(getContext(),EditPatientInfoActivity.class);
                startActivity(intent);
            }
        });

        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = GetUrl.url+"/user/deletePatient";
                Android_Async_Http_Post_deletePatient(url);
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        username = ((FirstPageActivity) context).getText();
        System.out.println("****"+username);
        super.onAttach(context);
    }

    //Post??????
    private void Android_Async_Http_Post(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",username);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                showResponse(response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                /*Toast.makeText(, "Post???????????????", Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    private void showResponse(JSONObject response){
        //Activity??????????????????????????????UI??????
        //??????????????????????????????????????????????????????????????????UI??????
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*tv.setText(response);*/
                System.out.println(response);
                try {
                    myPage_name.setText(username);
                    myPage_sex.setText(" ????????? "+response.getString("myPage_sex"));
                    myPage_age.setText(" ????????? "+response.getString("myPage_age"));
                    myPage_contactNum.setText(" ????????? "+response.getString("myPage_contactNum"));
                    myPage_hobby.setText(" ????????? "+response.getString("myPage_hobby"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //Post??????
    private void Android_Async_Http_Post_deletePatient(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",username);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                showResponse_deletePatient(response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                /*Toast.makeText(, "Post???????????????", Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    private void showResponse_deletePatient(JSONObject response){
        //Activity??????????????????????????????UI??????
        //??????????????????????????????????????????????????????????????????UI??????
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*tv.setText(response);*/
                System.out.println("????????????"+response);
                String flag = null;
                try {
                    flag = response.getString("flag");
                    if (flag.equals("true")){
                        Toast.makeText(getContext(),"????????????",Toast.LENGTH_SHORT);
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
