package com.ntu.treatment.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/9 21:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor implements Serializable {
    private String username;
    private String password;
    private String category;
    private String chatPatient;
    private Integer isChatting;
}
