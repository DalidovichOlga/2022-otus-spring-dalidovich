package ru.otus.spring.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class QuestionClassTest {

    @org.junit.jupiter.api.Test
    void getQuestionFullText() {
        Question q = new Question("Whats is your name?" , "1.Masha;2.Sasha;3.Lena" , "Lena");
        assertEquals(q.getQuestionFullText(), "Whats is your name? Variants: 1.Masha;2.Sasha;3.Lena");
    }

    @org.junit.jupiter.api.Test
    void checkAnswer() {
        Question q = new Question("Whats is your name?" , "Masha;Sasha;Lena" , "Lena");
        assertEquals(q.checkAnswer("3") , true);
        assertEquals(q.checkAnswer("Lena") , true);
        assertEquals(q.checkAnswer("Olena") , false);
    }
}