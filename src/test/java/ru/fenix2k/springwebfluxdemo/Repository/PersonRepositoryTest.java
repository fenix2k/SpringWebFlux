package ru.fenix2k.springwebfluxdemo.Repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.fenix2k.springwebfluxdemo.Entity.Person;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DatabaseClient databaseClient;

    private void createTable() {
        List<String> query = Arrays.asList(
                "DROP TABLE IF EXISTS users;",
                "CREATE TABLE users (" +
                        "  id LONG AUTO_INCREMENT  PRIMARY KEY,\n" +
                        "  name VARCHAR(250) NOT NULL,\n" +
                        "  age INT4 NOT NULL\n" +
                        ");"
        );

        query.forEach(it -> databaseClient.sql(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());
    }

    private void insertRows() {
        List<Person> people = Arrays.asList(
//                new User(null, "Vasya", 32),
//                new User(null, "Petya", 53),
//                new User(null, "Sasha", 24),
//                new User(null, "Roma", 26),
//                new User(null, "Dima", 45),
//                new User(null, "Pasha", 32)
        );

        personRepository.saveAll(people).subscribe();
    }

   // @BeforeEach
    void setUp() {
        Hooks.onOperatorDebug();
        createTable();
    }

    //@Test
    void whenDeleteAll_then0Expected() {
        insertRows();
        personRepository.deleteAll()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    //@Test
    void whenCreateOne_then1Expected() {
//        userRepository.save(new User(null, "Vova", 60))
//                .as(StepVerifier::create)
//                .expectNextCount(1)
//                .verifyComplete();
    }

    //@Test
    void whenFindByName_then1Expected() {
//        insertRows();
//        userRepository.findByName("Sasha")
//                .as(StepVerifier::create)
//                .expectNextCount(1)
//                .verifyComplete();
    }

    //@Test
    void whenFindAll_then6Expected() {
        insertRows();
        personRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(6)
                .verifyComplete();
    }

    void forTest() {
//        Mono<User> mono = Mono.just(
//                new User(null, "Vasya", 32)
//        );

        Flux<Person> flux = Flux.just(
//                new User(null, "Vasya", 32),
//                new User(null, "Petya", 53)
        );
        StepVerifier.create(flux)
                .assertNext(it -> it.getName().equals("swhite"))
                .assertNext(it -> it.getName().equals("swhite"))
                .verifyError(RuntimeException.class);

        StepVerifier.withVirtualTime(() -> Mono.delay(Duration.ofHours(3)))
                .expectSubscription()
                .expectNoEvent(Duration.ofHours(2))
                .thenAwait(Duration.ofHours(1))
                .expectNextCount(1)
                .expectComplete()
                .verify();

        StepVerifier.create(flux)
                .verifyTimeout(Duration.ofSeconds(1));

    }
}