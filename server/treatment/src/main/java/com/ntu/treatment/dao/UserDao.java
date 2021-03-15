package com.ntu.treatment.dao;

import com.ntu.treatment.pojo.Doctor;
import com.ntu.treatment.pojo.Patient;
import com.ntu.treatment.pojo.PatientHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/9 21:12
 */
@Mapper
@Repository
public interface UserDao {

    Doctor findByDoctorName(String username);

    Patient findByPatientName(String username);

    Integer registerDoctor(Doctor doctor);

    Integer registerPatient(Patient patient);

    Integer updatePatientCondition(Patient patient);

    Integer addPatientCondition(Patient patient);

    List<Doctor> getToUserName(String category);

    Doctor findChatPatient(String username);

    Patient findAllPatient(String username);

    int deletePatient(String username);

    Integer updatePatientInfo(Patient patient);

    List<PatientHistory> findAllPatientHistory(String username);

    Integer updateDoctorToPatient(@Param("username") String username, @Param("myDoctor")String myDoctor);

    PatientHistory findOnePatientHistory(@Param("username")String username,@Param("submitTime")String submitTime);
}
