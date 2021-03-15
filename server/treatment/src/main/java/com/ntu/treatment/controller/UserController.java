package com.ntu.treatment.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.ntu.treatment.pojo.Doctor;
import com.ntu.treatment.pojo.Patient;
import com.ntu.treatment.pojo.PatientHistory;
import com.ntu.treatment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/9 21:11
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/doctorLogin")
    public String checkDoctorLogin(Doctor doctor){
        System.out.println(doctor.getUsername());
        System.out.println("密码："+doctor.getPassword());
        Boolean flag = userService.checkDoctorLogin(doctor.getUsername(),doctor.getPassword());
        if (flag){
            return "true";
        }else {
            return "false";
        }
    }

    @RequestMapping("/patientLogin")
    public String checkPatientLogin(Patient patient){
        System.out.println("密码："+patient.getPassword());
        Boolean flag = userService.checkPatientLogin(patient.getUsername(),patient.getPassword());
        if (flag){
            return "true";
        }else {
            return "false";
        }
    }

    @RequestMapping("/doctorRegister")
    public String registerDoctor(Doctor doctor){
        Boolean flag = userService.registerDoctor(doctor);
        if (flag){
            return "true";
        }else {
            return "false";
        }
    }

    @RequestMapping("/patientRegister")
    public String registerPatient(Patient patient){
        System.out.println(patient.getUsername()+"密码"+patient.getPassword());
        Boolean flag = userService.registerPatient(patient);
        if (flag){
            return "true";
        }else {
            return "false";
        }
    }

    @RequestMapping("/addPatientCondition")
    public JSONObject addPatientCondition(Patient patient){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = df.format(System.currentTimeMillis());
        System.out.println(patient.getPatientCondition());
        String toUserName = userService.getToUserName(patient.getPatientKind());
        patient.setMyDoctor(toUserName);
        patient.setSubmitTime(datetime);
        boolean flag = userService.addPatientCondition(patient);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("flag", flag);
        jsonObject.put("toUserName", toUserName);
        return jsonObject;
    }


    @RequestMapping("/patientInfo")
    public JSONObject findAllPatient(String username){
        Patient patient = userService.findAllPatient(username);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("kind_b_patient", patient.getPatientKind());
        jsonObject.put("condition_b_patient", patient.getPatientCondition());
        jsonObject.put("doctor_b_patient", patient.getMyDoctor());
        System.out.println(patient.getPatientCondition());
        return jsonObject;
    }

    @RequestMapping("/patientAllInfo")
    public JSONObject findAllPatientInfo(String username){
        Patient patient = userService.findAllPatient(username);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("myPage_sex", patient.getSex());
        jsonObject.put("myPage_age", patient.getAge());
        jsonObject.put("myPage_contactNum", patient.getPhone());
        jsonObject.put("myPage_hobby", patient.getHobby());

        return jsonObject;
    }


    @RequestMapping("deletePatient")
    public JSONObject deletePatient(String username){
        System.out.println("这是删除"+username);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("flag", userService.deletePatient(username).toString());
        return jsonObject;
    }

    @RequestMapping("/reviewPatientInfo")
    public JSONObject updatePatientInfo(String password,String username,String sex,String age,String hobby,String phone){
        Patient patient = new Patient();
        patient.setPassword(password);
        patient.setUsername(username);
        patient.setSex(sex);
        patient.setAge(age);
        patient.setHobby(hobby);
        patient.setPhone(phone);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("flag", userService.updatePatientInfo(patient));
        return jsonObject;
    }

    @RequestMapping("/findAllPatientHistory")
    public JSONObject findAllPatientHistory(String username){
        List<PatientHistory> list = userService.findAllPatientHistory(username);
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));

        System.out.println(username);
        jsonObject.put("list_patient", jsonArray.toString());
        System.out.println(jsonArray.toString());
        /*jsonObject.put("list_history",jsonArray);*/
        return jsonObject;
    }

    @RequestMapping("/patientConInfo")
    public JSONObject patientConInfo(String username,String submitTime){
        PatientHistory patientHistory = userService.findOnePatientHistory(username,submitTime);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("flag","true");
        jsonObject.put("username",patientHistory.getUsername());
        jsonObject.put("submitTime",patientHistory.getSubmitTime());
        jsonObject.put("patientKind", patientHistory.getPatientKind());
        jsonObject.put("patientCondition",patientHistory.getPatientCondition());
        jsonObject.put("myDoctor",patientHistory.getMyDoctor());
        return jsonObject;
    }
}
