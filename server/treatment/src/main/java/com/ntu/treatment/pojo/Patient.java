package com.ntu.treatment.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/9 21:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient implements Serializable {
    private String username;
    private String password;
    private String patientCondition;
    private String patientKind;
    private String myDoctor;
    private String age;
    private String sex;
    private String phone;
    private String hobby;
    private String submitTime;
}
