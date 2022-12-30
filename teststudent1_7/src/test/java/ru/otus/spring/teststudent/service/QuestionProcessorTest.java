package ru.otus.spring.teststudent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.teststudent.domain.Question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Проверка вспомогательного сервиса по обработке вопросов")
class QuestionProcessorTest {

    Question question;

    @BeforeEach
    void createQuestion() {
        question = new Question("Where is the nile river?", "1.Europe;2.America;3.Australia;4.Africa", "4.Africa");
    }


    @DisplayName("Проверка формирования текста запроса")
    @Test
    void getQuestionFullText() {
        QuestionProcessor questionProcessor = new QuestionProcessor();
        assertThat(questionProcessor.getQuestionFullText(question))
                .startsWith("Where is the nile river?")
                .endsWith("1.Europe;2.America;3.Australia;4.Africa");
    }

    @DisplayName("Проверка ответа")
    @Test
    void checkAnswer() {
        QuestionProcessor questionProcessor = new QuestionProcessor();
        assertTrue(questionProcessor.checkAnswer(question, "4"));
        assertFalse(questionProcessor.checkAnswer(question, "2"));
    }
}