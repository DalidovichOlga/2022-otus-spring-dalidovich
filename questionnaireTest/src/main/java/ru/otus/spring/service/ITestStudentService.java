package ru.otus.spring.service;

public interface ITestStudentService {
    String getNextQuestion();
    void setStudent(String studentName);
    void checkAnswer(String answer);
    String getTestResult();
}
