package ru.otus.spring.teststudent.service;

import ru.otus.spring.teststudent.domain.Question;

public interface IQuestionProcessor {
    String getQuestionFullText(Question q);

    Boolean checkAnswer(Question q, String ans);
}
