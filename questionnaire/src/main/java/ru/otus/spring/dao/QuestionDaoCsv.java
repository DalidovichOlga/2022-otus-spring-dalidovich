package ru.otus.spring.dao;

import ru.otus.spring.domain.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuestionDaoCsv implements IQuestionDao {
    private List<Question> questionList = new ArrayList<>();
    private boolean initiated = false;
    private static String fileName; /// "questionlist.csv"

    public QuestionDaoCsv(String fileName)
    {
        this.fileName = fileName;
    }

    void readFileScv() {
        if (initiated) return;
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream stream = classLoader.getResourceAsStream(fileName);
        Stream<String> streamString = new BufferedReader(new InputStreamReader(stream))
                .lines();
        questionList = streamString.map(str -> {
            String[] stringQuestion = str.split(",");
            return new Question(stringQuestion[0], stringQuestion[1], stringQuestion[2]);
        }).collect(Collectors.toList());
        initiated = true;

    }

    @Override
    public Question getQuestion(int index) {
        readFileScv();
        if (index < getCount())
            return questionList.get(index);

        return null;
    }

    @Override
    public int getCount() {
        readFileScv();
        return questionList.size();
    }
}
