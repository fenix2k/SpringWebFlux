package ru.fenix2k.springwebfluxdemo.Security.Controller;

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
import ru.fenix2k.springwebfluxdemo.Security.Entity.Role;
import ru.fenix2k.springwebfluxdemo.Security.Service.RoleService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/security/roles", produces = "application/json")
@AllArgsConstructor
public class RoleController {

    @NonNull
    private final RoleService roleService;

    private ValidationUtils validator;

    @GetMapping("")
    public Flux<Role> pageableList(@RequestParam(value = "page", defaultValue = "0") long page,
                                   @RequestParam(value = "size", defaultValue = "10") long size) {
        return roleService.findAll()
                //.filter(p -> Optional.ofNullable(q).map(key -> p.getName().toLowerCase().startsWith(key.toLowerCase())).orElse(true))
                //.sort(Comparator.comparing(Role::getName))
                .skip(page * size)
                .take(size);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Role>> findByIdFull(@PathVariable Long id) {
        return roleService.findById(id)
                .map(it -> new ResponseEntity<>(it, HttpStatus.OK));
    }

    @GetMapping("/search")
    public Flux<Role> searchByCriteria(@RequestParam(value = "q") String criteria,
                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "size", defaultValue = "10") int size,
                                       @RequestParam(value = "sort", defaultValue = "id,asc") String[] sort) {
        String sortBy = sort[0];
        Sort.Direction sortDirection;
        if (sort.length == 1) sortDirection = Sort.Direction.ASC;
        else sortDirection = sort[1].toLowerCase().equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        return roleService.findByCriteria(criteria,
                PageRequest.of(page, size, Sort.by(sortDirection, sortBy)));
    }

    @PostMapping("")
    public Mono<ResponseEntity<Role>> save(@RequestBody Mono<@Valid Role> role) {
        return role
                .filter(validator::validate)
                .flatMap(roleService::save)
                .map(it -> new ResponseEntity<>(it, HttpStatus.CREATED));
    }

    @PutMapping("")
    public Mono<ResponseEntity<Role>> update(@RequestBody Mono<@Valid Role> role) {
        return role
                .filter(validator::validate)
                .flatMap(roleService::update)
                .map(it -> new ResponseEntity<>(it, HttpStatus.OK));
    }

    @PatchMapping("")
    public Mono<ResponseEntity<Role>> patch(@RequestBody Mono<@Valid Role> role) {
        return role
                .filter(validator::validate)
                .flatMap(roleService::patch)
                .map(it -> new ResponseEntity<>(it, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return roleService.delete(id)
                .map(it -> new ResponseEntity<>(it, HttpStatus.OK));
    }

}
