package ru.otus.spring.commentservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.otus.spring.commentservice.domain.UserGrant;

import java.util.List;
import java.util.Optional;

public interface UserGrantRepository extends JpaRepository<UserGrant, Long> {
    List<UserGrant> findAll();

    Optional<UserGrant> findByUserName(String userName );
}
