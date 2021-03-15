package com.ntu.treatment.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/13 14:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientHistory implements Serializable {
    private String username;
    private String patientCondition;
    private String patientKind;
    private String myDoctor;
    private String submitTime;

}
