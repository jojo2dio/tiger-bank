package org.zoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
//@ServletComponentScan
public class TigerBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(TigerBankApplication.class, args);
    }

}
