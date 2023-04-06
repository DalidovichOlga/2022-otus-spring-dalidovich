package ru.otus.spring.teststudent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.teststudent.dao.QuestionDaoCsv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Интеграционный тест c поднятием контекста")
@SpringBootTest
public class SpringStudentServiceTest {

    @Autowired
    private ITestStudentService studentService;

    @DisplayName("Сохранение студента")
    @Test
    void setStudent() {
        studentService.setStudent("Ivanov Ivan Ivanovich");
        assertFalse(studentService.getTestResult());
    }


    @DisplayName("Студент правильно отвечает на минимальное количество вопросов для зачета")
    @Test
    void getTwoTrueAnswer() {
        studentService.setStudent("Ivanov Ivan Ivanovich");
        String questionText = studentService.getNextQuestion();
        assertEquals(questionText, "what year is it now?");
        studentService.checkAnswer("2022");
        questionText = studentService.getNextQuestion();
        studentService.checkAnswer("3");
        assertTrue(studentService.getTestResult());
    }


    @DisplayName("Студент провалился")
    @Test
    void getFalseAnswer() {
        studentService.setStudent("Ivanov Ivan Ivanovich");
        String questionText = studentService.getNextQuestion();
        assertEquals(questionText, "what year is it now?");
        studentService.checkAnswer("2023");
        questionText = studentService.getNextQuestion();
        studentService.checkAnswer("1");
        assertFalse(studentService.getTestResult());
    }


}
