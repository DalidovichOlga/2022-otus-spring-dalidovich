package ru.otus.spring.domain;

public class Question {

    private String questionText;
    private String variants;
    private String trueAnswer;

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
