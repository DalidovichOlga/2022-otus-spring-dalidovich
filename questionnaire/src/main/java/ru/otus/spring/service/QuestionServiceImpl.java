package ru.otus.spring.service;

import ru.otus.spring.dao.IQuestionDao;
import ru.otus.spring.domain.Question;

import java.util.Scanner;

public class QuestionServiceImpl implements IQuestionService {

    private final IQuestionDao questionDao;

    public QuestionServiceImpl(IQuestionDao dao) {
        questionDao = dao;
    }

    @Override
    public void askQuestion() {
        int count = questionDao.getCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                Question question = questionDao.getQuestion(i);
                System.out.println(getQuestionFullText(question));
                Scanner scanner = new Scanner(System.in);
                String answer = scanner.nextLine();
                if (checkAnswer(question,answer))
                    System.out.println("All right");
                else
                    System.out.println("Wrong");

            }
        }
    }

    private String getQuestionFullText(Question q) {
        if ("".equals(q.getVariants()))
            return q.getQuestionText();
        else
            return q.getQuestionText() + " Variants: " + q.getVariants();
    }

    private Boolean checkAnswer(Question q , String ans) {
        if ("".equals(q.getVariants())) {
            return q.getTrueAnswer().equalsIgnoreCase(ans);
        } else {
            if (q.getTrueAnswer().equalsIgnoreCase(ans)) return Boolean.TRUE;
            if ("12345".contains(ans)) {
                String[] arrayAnsw = q.getVariants().split(";");
                int i = 1;
                for (String str : arrayAnsw) {
                    if (str.equalsIgnoreCase(q.getTrueAnswer()))
                        break;
                    i++;
                }
                return ans.equals(String.valueOf(i));

            }
            return false;

        }
    }
}
