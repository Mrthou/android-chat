package com.ntu.treatment.service.Impl;


import com.ntu.treatment.dao.UserDao;
import com.ntu.treatment.pojo.Doctor;
import com.ntu.treatment.pojo.Patient;
import com.ntu.treatment.pojo.PatientHistory;
import com.ntu.treatment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/9 21:20
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public Boolean checkDoctorLogin(String username,String password){
        Doctor doctor = userDao.findByDoctorName(username);
        System.out.println(username+"***service**"+password);
        System.out.println("返回的"+doctor.getPassword());
        if (doctor.getPassword().equals(password)){
            return true;
        }else {
            return false;
        }
    }

    public Boolean checkPatientLogin(String username, String password){
        Patient patient = userDao.findByPatientName(username);
        if (patient.getPassword().equals(password)){
            return true;
        }else {
            return false;
        }
    }

    public Boolean registerDoctor(Doctor doctor){
        int flag = userDao.registerDoctor(doctor);
        if (flag == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean registerPatient(Patient patient){
        int flag = userDao.registerPatient(patient);
        System.out.println(patient.getUsername()+"***service**"+patient.getPassword());
        System.out.println("返回的"+flag);
        if (flag == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean addPatientCondition(Patient patient){
        int flag = userDao.addPatientCondition(patient);
        int flag2 = userDao.updatePatientCondition(patient);
        int flag3 = userDao.updateDoctorToPatient(patient.getUsername(),patient.getMyDoctor());
        if (flag == 1 && flag2 == 1 && flag3 == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public String getToUserName(String category){
        List<Doctor> list = userDao.getToUserName(category);
        String toUserName = "";
        for (Doctor doctor : list){
            toUserName = doctor.getUsername();
            break;
        }
        return toUserName;
    }

    public String findChatPatient(String username){
        Doctor doctor = userDao.findChatPatient(username);
        return doctor.getChatPatient();
    }

   public Patient findAllPatient(String username){
        Patient patient = userDao.findAllPatient(username);
        return patient;
    }

    public Boolean deletePatient(String username){
        int flag = userDao.deletePatient(username);
        if (flag == 1){
            return true;
        }else {
            return false;
        }
    }

    public Boolean updatePatientInfo(Patient patient){
        int flag = userDao.updatePatientInfo(patient);
        if (flag == 1){
            return true;
        }else {
            return false;
        }
    }

    public List<PatientHistory> findAllPatientHistory(String username){
        return userDao.findAllPatientHistory(username);
    }

    public PatientHistory findOnePatientHistory(String username, String submitTime){
        PatientHistory patientHistory = userDao.findOnePatientHistory(username,submitTime);
        return patientHistory;
    }
}
