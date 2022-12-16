package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.QuestionDaoCsv;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Интеграционный тест")
class TestStudentServiceImplTest {


    private ITestStudentService studentService;

    @BeforeEach
    void setStudentService() {
        studentService = new TestStudentServiceImpl(new QuestionDaoCsv("questionlist.csv"), new QuestionProcessor(), 2);
    }

    @DisplayName("Сохранение студента")
    @Test
    void setStudent() {
        studentService.setStudent("Ivanov Ivan Ivanovich");
        assertEquals(studentService.getTestResult(), "Ivanov Ivan Ivanovich, you failed the test.");
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
        assertEquals(studentService.getTestResult(), "Congratulation, Ivanov Ivan Ivanovich");
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
        assertEquals(studentService.getTestResult(), "Ivanov Ivan Ivanovich, you failed the test.");
    }

}