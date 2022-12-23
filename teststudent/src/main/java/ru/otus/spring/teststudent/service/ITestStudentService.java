package ru.otus.spring.teststudent.service;

public interface ITestStudentService {
    String getNextQuestion();
    void setStudent(String studentName);
    void checkAnswer(String answer);
    boolean getTestResult();
}
