package ru.fenix2k.springwebfluxdemo.Security.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.fenix2k.springwebfluxdemo.Components.ValidationUtils;
import ru.fenix2k.springwebfluxdemo.Security.Entity.JsonViewLevel;
import ru.fenix2k.springwebfluxdemo.Security.Entity.User;
import ru.fenix2k.springwebfluxdemo.Security.Service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/security/users", produces = "application/json")
@AllArgsConstructor
@JsonView(JsonViewLevel.Min.class)
public class UserController {

    @NonNull
    private final UserService userService;

    private ValidationUtils validator;

    @JsonView(JsonViewLevel.Middle.class)
    @GetMapping("")
    public Flux<User> pageableList(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @JsonView(JsonViewLevel.Full.class)
    @GetMapping("/search")
    public Flux<User> searchByCriteria(@RequestParam(value = "q") String criteria, Pageable pageable) {
        return userService.findByCriteria(criteria, pageable);
    }

    @JsonView(JsonViewLevel.Full.class)
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> findById(@PathVariable Long id) {
        return userService.findById(id)
                .map(it -> new ResponseEntity<>(it, HttpStatus.OK));
    }

    @JsonView(JsonViewLevel.Min.class)
    @PostMapping("")
    public Mono<ResponseEntity<User>> save(@RequestBody Mono<@Valid User> user) {
        return user
                .filter(validator::validate)
                .flatMap(userService::save)
                .map(it -> new ResponseEntity<>(it, HttpStatus.CREATED));
    }

    @JsonView(JsonViewLevel.Min.class)
    @PutMapping("")
    public Mono<ResponseEntity<User>> update(@RequestBody Mono<@Valid User> user) {
        return user
                .filter(validator::validate)
                .flatMap(userService::update)
                .map(it -> new ResponseEntity<>(it, HttpStatus.OK));
    }

    @JsonView(JsonViewLevel.Min.class)
    @PatchMapping("")
    public Mono<ResponseEntity<User>> patch(@RequestBody Mono<@Valid User> user) {
        return user
                .filter(validator::validate)
                .flatMap(userService::patch)
                .map(it -> new ResponseEntity<>(it, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return userService.delete(id)
                .map(it -> new ResponseEntity<>(it, HttpStatus.OK));
    }

}
