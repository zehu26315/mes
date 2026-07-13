package com.example.mes;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({
    "com.example.mes.modules.system.mapper",
    "com.example.mes.modules.masterdata.mapper",
    "com.example.mes.modules.process.mapper",
    "com.example.mes.modules.production.mapper",
    "com.example.mes.modules.plan.mapper",
    "com.example.mes.modules.material.mapper",
    "com.example.mes.modules.warehouse.mapper",
    "com.example.mes.modules.equipment.mapper",
    "com.example.mes.modules.quality.mapper",
    "com.example.mes.modules.andon.mapper",
    "com.example.mes.modules.barcode.mapper",
    "com.example.mes.modules.trace.mapper",
    "com.example.mes.modules.wages.mapper",
    "com.example.mes.modules.erp.mapper"
})
public class MesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MesApplication.class, args);
    }

}
