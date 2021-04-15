package ru.fenix2k.springwebfluxdemo.Controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.fenix2k.springwebfluxdemo.Entity.Person;
import ru.fenix2k.springwebfluxdemo.Repository.PersonRepository;
import ru.fenix2k.springwebfluxdemo.Service.PersonService;

import java.util.Arrays;
import java.util.List;

//@ExtendWith(SpringExtension.class)
//@WebFluxTest(controllers = PersonController.class)
//@Import(PersonService.class)
class PersonControllerTest {

    //@MockBean
    private PersonRepository personRepository;

    @Autowired
    private WebTestClient client;

    private List<Person> people = Arrays.asList(
//            new User(null, "Vasya", 32),
//            new User(null, "Petya", 53),
//            new User(null, "Sasha", 24),
//            new User(null, "Roma", 26),
//            new User(null, "Dima", 45),
//            new User(null, "Pasha", 32)
    );


//    @BeforeEach
//    void setUp() {
//        //client = WebTestClient.bindToController(UserController.class).build();
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void list() {
//        Mockito
//                .when(personRepository.findAll())
//                .thenReturn(Flux.fromIterable(people));
//        client.get()
//                .uri("/users/")
//                .accept(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.ACCEPT, "application/json")
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON);
//    }

    //@Test
    void findById() {
    }

    //@Test
    void findByName() {
    }

    //@Test
    void save() {
    }

}