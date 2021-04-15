package ru.fenix2k.springwebfluxdemo.Service;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.fenix2k.springwebfluxdemo.Components.CustomSqlQueryBuilder;
import ru.fenix2k.springwebfluxdemo.Entity.Person;
import ru.fenix2k.springwebfluxdemo.Exceptions.ResourceException;
import ru.fenix2k.springwebfluxdemo.Repository.PersonRepository;
import ru.fenix2k.springwebfluxdemo.Security.Entity.User;
import ru.fenix2k.springwebfluxdemo.Utils.EntityUtils;

import java.time.LocalDate;
import java.util.function.BiFunction;


@Service
@AllArgsConstructor
public class PersonService {

    private final String TABLE_NAME = "persons";

    @NonNull
    private final PersonRepository personRepository;

    public static final BiFunction<Row, RowMetadata, Person> MAPPING_FUNCTION = (row, rowMetaData) -> Person.builder()
            .id(row.get("id", Long.class))
            .name(row.get("name", String.class))
            .email(row.get("email", String.class))
            .city(row.get("city", String.class))
            .phone(row.get("phone", String.class))
            .birthday(row.get("birthday", LocalDate.class))
            .build();

    public Flux<Person> findAll() {
        return personRepository.findAll();
    }

    public Mono<Person> findById(Long id) {
        return personRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceException(HttpStatus.NOT_FOUND, "Person not found with id [" + id + "]")));
    }

    public Flux<Person> findByCriteria(String criteria, Pageable pageable) {
        CustomSqlQueryBuilder queryBuilder = CustomSqlQueryBuilder.builder()
                .from(TABLE_NAME);
        return personRepository.execute(queryBuilder, criteria, MAPPING_FUNCTION, pageable)
                .doOnError(err -> {
                    throw new ResourceException(HttpStatus.BAD_REQUEST, err.getMessage());
                });
    }

    public Mono<Person> save(Person person) {
        return this.validateAndSave(person, "save");
    }

    public Mono<Person> update(Person person) {
        return this.validateAndSave(person, "update");
    }

    public Mono<Person> patch(Person person) {
        return this.validateAndSave(person, "patch");
    }

    public Mono<Void> delete(Long id) {
        return personRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceException(HttpStatus.NOT_FOUND, "Person not found with id [" + id + "]")))
                .then(personRepository.deleteById(id));
    }

    private Mono<Person> validateAndSave(Person person, String operation) {
        return Mono.just(person)
                .flatMap(it -> { // save or update/patch ?
                    if (it.getId() == null)
                        return Mono.just(it);
                    return personRepository.findById(it.getId())
                            .switchIfEmpty(Mono.error(new ResourceException(HttpStatus.NOT_FOUND, "Person is not found with id: [" + person.getId() + "]")));
                })
                .flatMap(it -> Mono.just(it) // check than specified email is unique
                        .filter(usr -> person.getId() == null || person.getEmail() != null && !usr.getEmail().equals(person.getEmail())).log()
                        .flatMap(usr -> personRepository.findByEmail(person.getEmail())).log()
                        .flatMap(usr -> Mono.<Person>error(new ResourceException(HttpStatus.CONFLICT, "Person is already exists with email [" + person.getEmail() + "]")))
                        .switchIfEmpty(Mono.just(it))
                )
                .flatMap(it -> { // save operation
                    if (operation.equals("patch"))
                        EntityUtils.copyNonNullProperties(person, it);
                    if (operation.equals("update"))
                        return personRepository.save(person);
                    return personRepository.save(it);
                })
                .doOnError(e -> { // if get an error
                    if (!(e instanceof ResourceException))
                        throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error: cannot save person");
                });
    }

}
