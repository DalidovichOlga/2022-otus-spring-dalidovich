package ru.otus.spring.teststudent.dao;

import ru.otus.spring.teststudent.domain.Question;

public interface IQuestionDao {
    Question getQuestion(int index);
    int getCount();
}
