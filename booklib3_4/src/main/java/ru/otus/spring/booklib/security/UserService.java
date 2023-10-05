package ru.otus.spring.booklib.security;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.otus.spring.booklib.dao.UserGrantRepository;
import ru.otus.spring.booklib.domain.Role;
import ru.otus.spring.booklib.domain.UserGrant;

import javax.annotation.PostConstruct;
import java.util.ArrayList;


public class UserService implements UserDetailsService {
    @Autowired
    private UserGrantRepository userRepository;

    @PostConstruct
    public void init() {

        ArrayList<Role> roleArrayList = new ArrayList<Role>();
        roleArrayList.add(Role.USER);
        if (!userRepository.findByUserName(("user")).isPresent()) {
            userRepository.save(UserGrant.builder()
                    .userName("user")
                    .password(new BCryptPasswordEncoder().encode("123456"))
                    .authorities("USER")
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build());
        }
        if (!userRepository.findByUserName("vasya").isPresent()) {
            userRepository.save(UserGrant.builder()
                    .userName("vasya")
                    .password(new BCryptPasswordEncoder().encode("123456"))
                    .authorities("USER")
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build());
        }
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username).orElseThrow(() ->
                new UsernameNotFoundException("user " + username + " was not found!"));
    }

}
