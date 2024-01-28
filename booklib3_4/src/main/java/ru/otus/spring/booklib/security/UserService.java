package ru.otus.spring.booklib.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.spring.booklib.dao.UserGrantRepository;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserGrantRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username).orElseThrow(() ->
                new UsernameNotFoundException("user " + username + " was not found!"));
    }

}
