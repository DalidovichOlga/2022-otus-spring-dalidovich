package ru.otus.spring.dao;

import ru.otus.spring.domain.Question;

public interface IQuestionDao {
    Question getQuestion(int index);
    int getCount();
}
