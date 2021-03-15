package com.ntu.treatment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntu.treatment.adapter.PatientHistoryAdapter;
import com.ntu.treatment.util.GetUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * @author Evan_zch
 * @date 2018/8/23 20:40
 */
public class ContactsFragment extends Fragment {

    /*private TextView username_patient;
    private TextView kind_b_patient;
    private TextView condition_b_patient;
    private TextView doctor_b_patient;*/
    private String username;
    private List<PatientHistory>  list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        /*username_patient = view.findViewById(R.id.username_patient);
        kind_b_patient = view.findViewById(R.id.kind_b_patient);
        condition_b_patient = view.findViewById(R.id.condition_b_patient);
        doctor_b_patient = view.findViewById(R.id.doctor_b_patient);*/
        String url = GetUrl.url + "/user/findAllPatientHistory";
        Android_Async_Http_Post(url);

        System.out.println("看着里"+list);
        //System.out.println("到这里了"+list.get(1));
        return  view;
    }

    //Post请求
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
                //Toast.makeText(, "Post请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResponse(JSONObject response){
        //Activity不允许在子线程中进行UI操作
        //通过该方法可以将线程切换到主线程，然后再更新UI元素
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //tv.setText(response);
                try {
                    String list_patient = response.getString("list_patient");
                    System.out.println(list_patient);
                    JSONArray jsonArray = JSONArray.parseArray(list_patient);
                    System.out.println("这是jsonArray"+jsonArray);
                    list = com.alibaba.fastjson.JSONObject.parseArray(com.alibaba.fastjson.JSONObject.toJSONString(jsonArray),PatientHistory.class);
                    PatientHistoryAdapter adapter = new PatientHistoryAdapter(getContext(),R.layout.patient_history_item,list);
                    ListView listView = (ListView)getView().findViewById(R.id.list_view);
                    listView.setAdapter(adapter);
                    // 为ListView注册一个监听器，当用户点击了ListView中的任何一个子项时，就会回调onItemClick()方法
                    // 在这个方法中可以通过position参数判断出用户点击的是那一个子项
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            PatientHistory patientHistory = list.get(position);
                            Bundle bundle = new Bundle();
                            bundle.putString("username",patientHistory.getUsername());
                            bundle.putString("submitTime",patientHistory.getSubmitTime());
                            Intent intent = new Intent();
                            intent.putExtra("bundle_pa",bundle);
                            intent.setClass(getContext(),PatientHistoryActivity.class);
                            startActivity(intent);
                            //Toast.makeText(getContext(),patientHistory.getUsername(),Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("这是list"+list);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        username = ((FirstPageActivity) context).getText();
        System.out.println("****"+username);
        super.onAttach(context);
    }
}
