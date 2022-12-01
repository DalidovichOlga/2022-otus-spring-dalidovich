package ru.otus.spring.service;

import ru.otus.spring.dao.IQuestionDao;
import ru.otus.spring.domain.Question;

import java.util.Scanner;

public class QuestionServiceImpl implements IQuestionService {

    private IQuestionDao questionDao;

    public QuestionServiceImpl(IQuestionDao dao) {
        questionDao = dao;
    }

    @Override
    public void askQuestion() {
        int count = questionDao.getCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                Question q = questionDao.getQuestion(i);
                System.out.println(q.getQuestionFullText());
                Scanner scanner = new Scanner(System.in);
                String answer = scanner.nextLine();
                if (q.checkAnswer(answer))
                    System.out.println("All right");
                else
                    System.out.println("Wrong");

            }
        }
    }
}
