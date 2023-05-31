package com.qc.printers;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qc.printers.common.MyString;
import com.qc.printers.mapper.PermissionMapper;
import com.qc.printers.pojo.entity.Permission;
import com.qc.printers.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.util.List;

//@EnableOpenApi//启动swaggerUI
@SpringBootApplication
@Slf4j
public class PrintersApplication implements CommandLineRunner {

    @Autowired
    private IRedisService iRedisService;

    @Autowired
    private PermissionMapper permissionMapper;
    public static void main(String[] args) {
        SpringApplication.run(PrintersApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //启动成功执行该方法
        log.info("启动");
        LambdaQueryWrapper<Permission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Permission> permissions = permissionMapper.selectList(lambdaQueryWrapper);
        for (Permission permission:
                permissions) {
            iRedisService.hashPut(MyString.permission_key, String.valueOf(permission.getId()),permission);
        }
    }



}
