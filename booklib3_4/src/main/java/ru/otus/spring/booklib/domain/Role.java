package ru.otus.spring.booklib.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "usergrandid")
    Long userGrandId;

    @Column(name = "name")
    String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
