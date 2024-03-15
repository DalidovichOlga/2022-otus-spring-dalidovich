package ru.otus.spring.booklib.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.spring.booklib.dao.BookRepository;

@Component
public class LibraryEmptyHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    public LibraryEmptyHealthIndicator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Health health() {
        if (bookRepository.existsBy())
            return Health.up().withDetail("message", "Библиотека работает.").build();
        else
            return Health.down().withDetail("message", "Пропали все книги.").build();
    }
}
