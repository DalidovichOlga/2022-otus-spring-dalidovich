package ru.otus.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.service.ITestStudentService;

import java.util.Scanner;

@ComponentScan
@PropertySource("classpath:application.properties")
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Main.class);

        System.out.println("START TEST");
        System.out.println("Hello, what is your name?");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        System.out.println("Please, answer a few questions, " + name);
        ITestStudentService service = context.getBean(ITestStudentService.class);
        service.setStudent(name);

        while (true)
        {
            String questionText = service.getNextQuestion();
            if ("".equals(questionText))
                break;

            System.out.println(questionText);
            String answer = scanner.nextLine();
            service.checkAnswer(answer);
        }
        String testResult = service.getTestResult();
        System.out.println(testResult);

        System.out.println("END TEST");
        // Данная операция, в принципе не нужна.
        // Мы не работаем пока что с БД, а Spring Boot сделает закрытие за нас
        // Подробности - через пару занятий
        context.close();
    }
}
