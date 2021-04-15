package ru.fenix2k.springwebfluxdemo.Repository;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;
import ru.fenix2k.springwebfluxdemo.Entity.Person;

public interface PersonRepository extends ReactiveSortingRepository<Person, Long>, CustomQueryRepository {

    Mono<Person> findByEmail(String s);

}
