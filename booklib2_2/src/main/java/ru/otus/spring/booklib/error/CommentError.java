package ru.otus.spring.booklib.error;

public class CommentError extends Exception {
    private final String code;
    private final String details;

    public CommentError(String code, String details) {
        super(code);
        this.code = code;
        this.details = details;
    }

    public String getCode() {
        return code;
    }

    public String getDetails() {
        return details;
    }

}
