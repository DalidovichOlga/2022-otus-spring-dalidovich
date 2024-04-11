package ru.otus.spring.booklib.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "authors")
@NoArgsConstructor
@Getter
@Setter
public class Author {
    @Column(name = "firstname" )
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "middlename")
    private String middleName;
    @Column(name = "shortname", unique = true)
    private String shortName;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public Author(String firstName, String lastName, String middleName) {
        this.firstName = ((firstName == null)) ? "" : firstName;
        this.lastName = lastName;
        this.middleName = ((middleName == null)) ? "" : middleName;
        shortName = lastName + ' ' + firstName.substring(0, 1) + "." + ((!"".equals(middleName)) ? middleName.substring(0, 1) + "." : "");
        this.id = 0;
    }

    public Author(String firstName, String lastName, String middleName, Long id) {
        this(firstName, lastName, middleName);
        this.id = id;
    }


    public Author(String firstName, String lastName, String middleName, String shortName, long id) {
        this(firstName, lastName, middleName, id);
        if (!((shortName == null) || "".equals(shortName)))
            this.shortName = shortName;
        else
            this.shortName = lastName + ' ' + firstName.substring(0, 1) + "." + ((!"".equals(middleName)) ? middleName.substring(0, 1) + "." : "");

    }

    public String getFullName() {
        return lastName + " " + firstName + " " + middleName;
    }

}
