package ru.otus.spring.teststudent.domain;

public class Student {
    private final String name;
    private int  score;

    public Student(String name) {
        this.name = name;
        score = 0;
    }

    public void incrementScore() {
        this.score++;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
