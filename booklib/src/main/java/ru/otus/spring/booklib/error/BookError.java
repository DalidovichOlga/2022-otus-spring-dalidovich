package ru.otus.spring.booklib.error;

public class BookError extends Exception {
    private final String errorText;
    private final String details;

    public BookError(String message, String details, Throwable cause) {
        super(message, cause);
        errorText = message;
        this.details = details;
    }

    public BookError(String errorText, String details) {
        super(errorText);
        this.errorText = errorText;
        this.details = details;
    }

    public String getErrorText() {
        return errorText;
    }

    public String getDetails() {
        return details;
    }
}
