package com.ntu.treatment;

import com.ntu.treatment.utils.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SpringUtil.class)
/*@MapperScan(basePackages = "com.ntu.treatment.dao")*/
public class TreatmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(TreatmentApplication.class, args);
    }

}
