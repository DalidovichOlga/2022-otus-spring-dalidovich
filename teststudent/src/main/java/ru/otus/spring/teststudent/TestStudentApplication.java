package ru.otus.spring.teststudent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.spring.teststudent.service.IRunTestStudent;

@SpringBootApplication
public class TestStudentApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(TestStudentApplication.class, args);

        IRunTestStudent service = context.getBean(IRunTestStudent.class);
        service.TestRun();

    }

}
