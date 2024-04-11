package ru.otus.spring.booklib.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "genres")
@NoArgsConstructor
@Getter
@Setter
public class Genre {

    @Column(name = "genrename", unique = true)
    private String genreName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public Genre(String genreName, long id) {
        this.genreName = genreName;
        this.id = id;
    }

    public Genre(String genreName) {
        this.genreName = genreName;
        this.id = 0;
    }

}
