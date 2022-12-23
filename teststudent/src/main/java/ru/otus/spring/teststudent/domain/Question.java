package ru.otus.spring.teststudent.domain;

public class Question {

    private final String questionText;
    private final String variants;
    private final String trueAnswer;

    public Question(String questionText, String variants, String trueAnswer) {
        this.questionText = questionText;
        this.variants = variants;
        this.trueAnswer = trueAnswer;
    }


    public String getQuestionText() {
        return questionText;
    }

    public String getVariants() {
        return variants;
    }

    public String getTrueAnswer() {
        return trueAnswer;
    }

}
