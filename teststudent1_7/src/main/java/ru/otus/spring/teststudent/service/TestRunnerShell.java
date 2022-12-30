package ru.otus.spring.teststudent.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Locale;

@ShellComponent
public class TestRunnerShell {

    private final ITestStudentService service;
    private final MessageSource messageSource;
    private final Locale locale;
    String studentName = null;


    public TestRunnerShell(ITestStudentService service, MessageSource msg, @Value("${settings.local}") String local) {
        this.service = service;
        messageSource = msg;
        locale = Locale.forLanguageTag(local);
    }

    @ShellMethod(value = "Enter your first and last name", key = {"l", "login"})
    public String login(String userFirstName, String userLastName) {
        studentName = userFirstName + ' ' + userLastName;
        service.setStudent(studentName);
        String questionText = service.getNextQuestion();
        return messageSource.getMessage("invitation",
                new String[]{studentName}, locale)
                + System.lineSeparator() +
                questionText
                ;

    }

    @ShellMethod(value = "Set answer", key = {"a", "answer"})
    @ShellMethodAvailability(value = "isTestsAvailable")
    public String answer(String answer) {
        String msgText = "";
        service.checkAnswer(answer);
        String questionText = service.getNextQuestion();
        if ("".equals(questionText)) {
            if (service.getTestResult())
                msgText = messageSource.getMessage("resultpass", new String[]{studentName}, locale);
            else
                msgText = messageSource.getMessage("resultfail", new String[]{studentName}, locale);
            // ожидаем нового студента
            studentName = null;
        } else
            msgText = questionText;

        return msgText;
    }

    @ShellMethod(value = "Break test", key = {"b", "break"})
    @ShellMethodAvailability(value = "isTestsAvailable")
    public String breaktest() {
        String msgText = "";
        if (service.getTestResult())
            msgText = messageSource.getMessage("resultpass", new String[]{studentName}, locale);
        else
            msgText = messageSource.getMessage("resultfail", new String[]{studentName}, locale);
        // ожидаем нового студента
        studentName = null;

        return msgText;
    }

    private Availability isTestsAvailable() {
        return (studentName == null) ? Availability.unavailable("Сначала введите свое имя с помощью команды login") : Availability.available();
    }
}
