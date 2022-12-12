package ru.otus.spring.domain;

public class Student {
    private String name;
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
