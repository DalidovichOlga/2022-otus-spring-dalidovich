package ru.otus.spring.service;

import ru.otus.spring.domain.Question;

public interface IQuestionProcessor {
    String getQuestionFullText(Question q);

    Boolean checkAnswer(Question q, String ans);
}
