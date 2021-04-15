package ru.fenix2k.springwebfluxdemo.Security.Service;

import lombok.Data;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import ru.fenix2k.springwebfluxdemo.Security.Repository.UserRepository;

@Data
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String s) {
        return userRepository.findOneByLogin(s).map(user ->  user);
    }
}
