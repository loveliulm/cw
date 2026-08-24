package com.qifu;

import com.cxytiandi.encrypt.springboot.annotation.EnableEncrypt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 */
@EnableEncrypt
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class QiFuApplication
{
    public static void main(String[] args)
    {
//        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(QiFuApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  项目启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
