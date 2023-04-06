package ru.otus.spring.teststudent.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Scanner;

@Service
public class TestStudentServiceRunner implements IRunTestStudent {

    private final ITestStudentService service;

    private final MessageSource msg;

    private final String localStr;

    private final Locale locale;

    public TestStudentServiceRunner(ITestStudentService service, MessageSource msg, @Value("${settings.local}") String local) {
        this.service = service;
        this.msg = msg;
        localStr = local;
        locale = Locale.forLanguageTag(localStr);
    }


    @Override
    public void TestRun() {

        String msgText = msg.getMessage("wtsyourname",
                new String[]{}, locale);
        System.out.println(msgText);
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        msgText = msg.getMessage("invitation",
                new String[]{name}, locale);
        System.out.println(msgText);

        service.setStudent(name);

        while (true) {
            String questionText = service.getNextQuestion();
            if ("".equals(questionText))
                break;

            System.out.println(questionText);
            String answer = scanner.nextLine();
            service.checkAnswer(answer);
        }
        if (service.getTestResult())
            msgText = msg.getMessage("resultpass", new String[]{name}, locale);
        else
            msgText = msg.getMessage("resultfail", new String[]{name}, locale);

        System.out.println(msgText);

    }
}
