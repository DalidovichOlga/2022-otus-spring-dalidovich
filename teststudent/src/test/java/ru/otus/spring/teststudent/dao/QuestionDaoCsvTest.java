package ru.otus.spring.teststudent.dao;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionDaoCsvTest {

    @Test
    void getQuestion() {
        QuestionDaoCsv daoCsv = new QuestionDaoCsv("questionlist.csv", "");

        assertThat(daoCsv.getCount()).isEqualTo(6);
        assertThat(daoCsv.getQuestion(3).getQuestionText()).startsWith("Calculate");
        assertThat(daoCsv.getQuestion(3).getTrueAnswer()).endsWith("110");
    }

}