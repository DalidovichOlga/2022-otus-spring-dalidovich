package ru.otus.spring.teststudent.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.teststudent.domain.Question;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuestionDaoCsv implements IQuestionDao {
    private List<Question> questionList = new ArrayList();
    private boolean initiated = false;
    private static String fileName;
    private static String originFileName;

    private final String localStr;


    public QuestionDaoCsv(@Value("${csvfile.path}") String fileName, @Value("${settings.local}") String local) {
        this.localStr = local;
        originFileName = fileName;
        if (!localStr.equals("")) {
            int posPoint = fileName.lastIndexOf(".");
            fileName = fileName.substring(0, posPoint) + "-" + localStr + fileName.substring(posPoint);
        }
        this.fileName = fileName;
    }

    void readFileScv() {
        if (initiated) return;

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream stream;
        URL resource = classLoader.getResource(fileName);

        if (resource == null)// не умеем в такой язык переводить
            stream = classLoader.getResourceAsStream(originFileName);
        else
            stream = classLoader.getResourceAsStream(fileName);

        Stream<String> streamString = null;
        try {
            streamString = new BufferedReader(new InputStreamReader(stream ,  "UTF-8"))
                    .lines();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
