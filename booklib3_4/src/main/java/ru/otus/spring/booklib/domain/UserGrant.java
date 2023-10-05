package ru.otus.spring.booklib.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "usergranttable")
public class UserGrant implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String userName;
    @Column(name = "authorities")
    private String authorities;
    @Column(name = "password")
    private String password;
    @Column(name = "accountNonExpired")
    private boolean accountNonExpired;
    @Column(name = "accountNonLocked")
    private boolean accountNonLocked;
    @Column(name = "credentialsNonExpired")
    private boolean credentialsNonExpired;
    @Column(name = "enabled")
    private boolean enabled;

    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        List<Role> arrayList =Arrays.stream(authorities.split(";")).map(
               t-> Role.valueOf(t)
        ).collect(Collectors.toList());
        return arrayList;
    }

    @Override
    public String getUsername() {
        return userName;
    }

}
