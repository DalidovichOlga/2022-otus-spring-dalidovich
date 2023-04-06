package ru.otus.spring.teststudent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.teststudent.dao.IQuestionDao;
import ru.otus.spring.teststudent.domain.Question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Unit тест для сервиса тестирования студента")
@ExtendWith(MockitoExtension.class)
class UnitStudentServiceImplTest {

    private Question question1;
    private Question question2;
    private Question question3;

    @Mock
    private IQuestionDao questionDaoCsv;

    @Spy
    private QuestionProcessor questionProcessor;


    private TestStudentServiceImpl service;

    @BeforeEach
    void setAllQuestion() {
        service = new TestStudentServiceImpl(questionDaoCsv, questionProcessor, 3);
        question1 = new Question("Calculate 7+8 ", "", "15");
        question2 = new Question("what is the capital of the Russia?", "1.Vasuki;2.Leningrad;3.Moskow",
                "3.Moskow");
        question3 = new Question("Calculate 2 + 2", "", "5");

        Mockito.when(questionDaoCsv.getCount()).thenReturn(3);
        Mockito.when(questionDaoCsv.getQuestion(0)).thenReturn(question1);
        Mockito.when(questionDaoCsv.getQuestion(1)).thenReturn(question2);
        Mockito.when(questionDaoCsv.getQuestion(2)).thenReturn(question3);
        service.setStudent("Sidorov Sidor Sidorovich");
    }

    @DisplayName("Студент прошел успешный тест")
    @Test
    void passSuccessfulTest() {
        service.getNextQuestion();
        verify(questionDaoCsv, times(1)).getQuestion(eq(0));
        verify(questionDaoCsv, times(1)).getCount();
        service.checkAnswer("15");

        service.getNextQuestion();
        service.checkAnswer("3");
        service.getNextQuestion();
        service.checkAnswer("5");
        verify(questionProcessor, times(3)).checkAnswer(any(), any());
        assertThat(service.getTestResult()).isEqualTo(true);
    }

    @DisplayName("Студент провали тест")
    @Test
    void passUnsuccessfulTest() {
        service.getNextQuestion();
        verify(questionDaoCsv, times(1)).getQuestion(eq(0));
        verify(questionDaoCsv, times(1)).getCount();
        service.checkAnswer("1");

        service.getNextQuestion();
        service.checkAnswer("1");
        service.getNextQuestion();
        service.checkAnswer("4");
        verify(questionProcessor, times(3)).checkAnswer(any(), any());

        assertThat(service.getTestResult()).isEqualTo(false);
    }


}