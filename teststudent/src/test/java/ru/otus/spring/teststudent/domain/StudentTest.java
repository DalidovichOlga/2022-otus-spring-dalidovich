package ru.otus.spring.teststudent.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @DisplayName("Проверить увеличения счетчика баллов")
    @Test
    void incrementScore() {
        Student student = new Student("Petrov Petr Petrovich");

        student.incrementScore();

        assertThat(student.getScore()).isEqualTo(1);
    }


    @DisplayName("Проверить создание объекта")
    @Test
    void createStudentTest() {
        Student student = new Student("Petrov Petr Petrovich");

        assertAll("student create" ,
                ()->assertThat(student.getName()).isEqualTo("Petrov Petr Petrovich"),
                ()->assertEquals(0, student.getScore()));
    }
}