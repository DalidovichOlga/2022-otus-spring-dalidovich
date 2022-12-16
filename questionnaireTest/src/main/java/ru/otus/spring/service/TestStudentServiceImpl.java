package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.IQuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;

@Service
public class TestStudentServiceImpl implements ITestStudentService {

    private final IQuestionDao questionDao;
    private final IQuestionProcessor questionProcessor;

    private Student student = null;
    private int passScope;
    private int currentQuestion = -1;

    @Autowired
    public TestStudentServiceImpl(IQuestionDao dao, IQuestionProcessor questionProc, @Value("${attempts.number}") int passScope) {
        questionDao = dao;
        questionProcessor = questionProc;
        this.passScope = passScope;
    }

    @Override
    public String getNextQuestion() {
        currentQuestion++;
        if (currentQuestion < questionDao.getCount()) {
            Question question = questionDao.getQuestion(currentQuestion);
            return questionProcessor.getQuestionFullText(question);
        } else return "";
    }

    @Override
    public void setStudent(String studentName) {
        currentQuestion = -1;
        student = new Student(studentName);
    }

    @Override
    public void checkAnswer(String answer) {
        Question question = questionDao.getQuestion(currentQuestion);

        if (questionProcessor.checkAnswer(question, answer))
            student.incrementScore();

    }

    @Override
    public String getTestResult() {
        if (student.getScore() >= passScope)
            return "Congratulation, " + student.getName();
        else
            return student.getName() + ", you failed the test.";
    }


}
