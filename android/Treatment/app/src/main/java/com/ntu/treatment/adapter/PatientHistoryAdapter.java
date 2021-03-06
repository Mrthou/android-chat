package com.ntu.treatment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntu.treatment.PatientHistory;
import com.ntu.treatment.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class PatientHistoryAdapter extends ArrayAdapter<PatientHistory> {
    private  int resourceId;
    public PatientHistoryAdapter(Context context, int textViewResourceId, List<PatientHistory> objects){
        super(context,textViewResourceId,objects);
        resourceId =textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PatientHistory patientHistory = getItem(position);
        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        View view;
        ViewHolder viewHolder;
        if (convertView==null){

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder=new ViewHolder();
            viewHolder.patientImage=view.findViewById(R.id.patient_image);
            viewHolder.patientKind=view.findViewById(R.id.kind);
            viewHolder.SubmitTime = view.findViewById(R.id.patient_submit_time);

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        // 获取控件实例，并调用set...方法使其显示出来
        viewHolder.patientImage.setImageResource(R.drawable.img_patient);
        viewHolder.patientKind.setText(patientHistory.getPatientKind());
        viewHolder.SubmitTime.setText(patientHistory.getSubmitTime());
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        ImageView patientImage;
        TextView patientKind;
        TextView SubmitTime;
    }
}
