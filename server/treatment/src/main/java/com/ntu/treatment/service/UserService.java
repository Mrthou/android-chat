package com.ntu.treatment.service;

import com.ntu.treatment.pojo.Doctor;
import com.ntu.treatment.pojo.Patient;
import com.ntu.treatment.pojo.PatientHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/9 21:20
 */
public interface UserService {

    Boolean checkDoctorLogin(String username,String password);

    Boolean checkPatientLogin(String username, String password);

    Boolean registerDoctor(Doctor doctor);

    Boolean registerPatient(Patient patient);

    Boolean addPatientCondition(Patient patient);

    String getToUserName(String category);

    String findChatPatient(String username);

    Patient findAllPatient(String username);

    Boolean deletePatient(String username);

    Boolean updatePatientInfo(Patient patient);

    List<PatientHistory> findAllPatientHistory(String username);

    PatientHistory findOnePatientHistory(String username, String submitTime);
}
