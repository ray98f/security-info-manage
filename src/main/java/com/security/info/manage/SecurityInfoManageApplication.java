package com.security.info.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author frp
 */
@EnableSwagger2
@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
@MapperScan("com/security/info/manage/mapper")
@EntityScan("com/security/info/manage/entity")
@EnableAsync
@ServletComponentScan("com.security.info.manage.config.filter")
public class SecurityInfoManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityInfoManageApplication.class, args);
	}

}
