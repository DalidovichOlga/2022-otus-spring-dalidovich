package ru.otus.spring.teststudent.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionClassTest {

    @org.junit.jupiter.api.Test
    void getQuestionFullText() {
        Question q = new Question("Whats is your name?", "1.Masha;2.Sasha;3.Lena", "Lena");
        assertEquals(q.getQuestionText(), "Whats is your name?");
    }

}