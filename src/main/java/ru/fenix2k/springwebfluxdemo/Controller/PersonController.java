package ru.fenix2k.springwebfluxdemo.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.fenix2k.springwebfluxdemo.Components.ValidationUtils;
import ru.fenix2k.springwebfluxdemo.Entity.Person;
import ru.fenix2k.springwebfluxdemo.Entity.PersonJsonView;
import ru.fenix2k.springwebfluxdemo.Service.PersonService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(path = "api/v1/persons", produces = "application/json")
@AllArgsConstructor
public class PersonController {

    @NonNull
    private final PersonService personService;

    private ValidationUtils validator;

    @GetMapping("wellcome")
    public Mono<String> wellcome(Mono<Principal> principal) {
        return principal
                .map(Principal::getName)
                .map(name -> String.format("Welcome to our site, %s!", name));
    }

    @GetMapping("")
    public Flux<Person> pageableList(@RequestParam(value = "q", required = false) String q,
                                     @RequestParam(value = "page", defaultValue = "0") long page,
                                     @RequestParam(value = "size", defaultValue = "10") long size) {
        return personService.findAll().log()
                //.filter(p -> Optional.ofNullable(q).map(key -> p.getName().toLowerCase().startsWith(key.toLowerCase())).orElse(true))
                //.sort(Comparator.comparing(User::getName))
                .skip(page * size)
                .take(size);
    }

    @JsonView(PersonJsonView.Min.class)
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Person>> findByIdMin(@PathVariable Long id, Mono<Principal> principal) {
        return findByIdFull(id);
    }

    @JsonView(PersonJsonView.Middle.class)
    @GetMapping("/{id}/middle")
    public Mono<ResponseEntity<Person>> findByIdMiddle(@PathVariable Long id) {
        return findByIdFull(id);
    }

    @JsonView(PersonJsonView.Full.class)
    @GetMapping("/{id}/full")
    public Mono<ResponseEntity<Person>> findByIdFull(@PathVariable Long id) {
        return personService.findById(id)
                .map(it -> new ResponseEntity<>(it, HttpStatus.OK));
    }

    @GetMapping("/search")
    public Flux<Person> searchByCriteria(@RequestParam(value = "q") String criteria,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                         @RequestParam(value = "sort", defaultValue = "id,asc") String[] sort) {
        String sortBy = sort[0];
        Sort.Direction sortDirection;
        if (sort.length == 1) sortDirection = Sort.Direction.ASC;
        else sortDirection = sort[1].toLowerCase().equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        return personService.findByCriteria(criteria,
                PageRequest.of(page, size, Sort.by(sortDirection, sortBy))).log();
    }

    @PostMapping("")
    public Mono<ResponseEntity<Person>> save(@RequestBody Mono<@Valid Person> person) {
        return person
                .filter(validator::validate)
                .flatMap(personService::save)
                .map(it -> new ResponseEntity<>(it, HttpStatus.CREATED));
    }

    @PutMapping("")
    public Mono<ResponseEntity<Person>> update(@RequestBody Mono<@Valid Person> person) {
        return person
                .filter(validator::validate)
                .flatMap(personService::update)
                .map(it -> new ResponseEntity<>(it, HttpStatus.OK));
    }

    @PatchMapping("")
    public Mono<ResponseEntity<Person>> patch(@RequestBody Mono<@Valid Person> person) {
        return person
                .filter(validator::validate)
                .flatMap(personService::patch)
                .map(it -> new ResponseEntity<>(it, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return personService.delete(id)
                .map(it -> new ResponseEntity<>(it, HttpStatus.OK));
    }

}
