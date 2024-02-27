package ru.otus.spring.booklib.error;

public class LibraryError extends Exception {
    private final String code;
    private final String details;

    public LibraryError(String code, String details) {
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
