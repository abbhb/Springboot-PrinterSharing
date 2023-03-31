package com.qc.printers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi//启动swaggerUI
@SpringBootApplication
public class PrintersApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrintersApplication.class, args);
    }

}
