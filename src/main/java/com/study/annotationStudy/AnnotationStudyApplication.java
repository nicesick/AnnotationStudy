package com.study.annotationStudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AnnotationStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnnotationStudyApplication.class, args);
	}

}
