package ru.otus.spring.teststudent.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.teststudent.domain.Question;

@Service
public class QuestionProcessor implements IQuestionProcessor {

    @Override
    public String getQuestionFullText(Question q) {
        if ("".equals(q.getVariants()))
            return q.getQuestionText();
        else
            return q.getQuestionText() + System.lineSeparator() + q.getVariants();
    }

    @Override
    public Boolean checkAnswer(Question q, String ans) {
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
