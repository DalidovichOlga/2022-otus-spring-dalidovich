package ru.otus.spring.booklib.domain;

public class Author {
    private String firstName;
    private final String lastName;
    private final String middleName;
    private String shortName;

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

    public String getShortName() {
        return shortName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return (id + "     ").substring(0, 5) + "|" + (shortName + " ".repeat(50)).substring(0, 55) + "|"
                + getFullName() + "\n\r";
    }

}
