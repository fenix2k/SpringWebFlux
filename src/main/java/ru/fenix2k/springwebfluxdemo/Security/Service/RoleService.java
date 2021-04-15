package ru.fenix2k.springwebfluxdemo.Security.Service;

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
import ru.fenix2k.springwebfluxdemo.Exceptions.ResourceException;
import ru.fenix2k.springwebfluxdemo.Security.Entity.Role;
import ru.fenix2k.springwebfluxdemo.Security.Repository.RoleRepository;
import ru.fenix2k.springwebfluxdemo.Utils.EntityUtils;

import java.util.function.BiFunction;


@Service
@AllArgsConstructor
public class RoleService {

    private final String TABLE_NAME = "roles";

    @NonNull
    private final RoleRepository roleRepository;

    public static final BiFunction<Row, RowMetadata, Role> MAPPING_FUNCTION = (row, rowMetaData) -> Role.builder()
            .id(row.get("id", Long.class))
            .name(row.get("name", String.class))
            .description(row.get("description", String.class))
            .build();

    public Flux<Role> findAll() {
        return roleRepository.findAll();
    }

    public Mono<Role> findById(Long id) {
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceException(HttpStatus.NOT_FOUND, "Role not found with id [" + id + "]")));
    }

    public Flux<Role> findByCriteria(String criteria, Pageable pageable) {
        CustomSqlQueryBuilder queryBuilder = CustomSqlQueryBuilder.builder()
                .from(TABLE_NAME);
        return roleRepository.execute(queryBuilder, criteria, MAPPING_FUNCTION, pageable)
                .doOnError(err -> {
                    throw new ResourceException(HttpStatus.BAD_REQUEST, err.getMessage());
                });
    }

    public Mono<Role> save(Role role) {
        return this.validateAndSave(role, "save");
    }

    public Mono<Role> update(Role role) {
        return this.validateAndSave(role, "update");
    }

    public Mono<Role> patch(Role role) {
        return this.validateAndSave(role, "patch");
    }

    public Mono<Void> delete(Long id) {
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceException(HttpStatus.NOT_FOUND, "Role not found with id [" + id + "]")))
                .then(roleRepository.deleteById(id));
    }

    private Mono<Role> validateAndSave(Role role, String operation) {
        return Mono.just(role)
                .flatMap(it -> { // save or update/patch ?
                    if (it.getId() == null)
                        return Mono.just(it);
                    return roleRepository.findById(it.getId())
                            .switchIfEmpty(Mono.error(new ResourceException(HttpStatus.NOT_FOUND, "Role is not found with id: [" + role.getId() + "]")));
                })
                .flatMap(it -> Mono.just(it) // check than specified email is unique
                        .filter(usr -> role.getId() == null || role.getName() != null && !usr.getName().equals(role.getName())).log()
                        .flatMap(usr -> roleRepository.findOneByName(role.getName())).log()
                        .flatMap(usr -> Mono.<Role>error(new ResourceException(HttpStatus.CONFLICT, "Role is already exists with login [" + role.getName() + "]")))
                        .switchIfEmpty(Mono.just(it))
                )
                .flatMap(it -> { // save operation
                    if (operation.equals("patch"))
                        EntityUtils.copyNonNullProperties(role, it);
                    if (operation.equals("update"))
                        return roleRepository.save(role);
                    return roleRepository.save(it);
                })
                .doOnError(e -> { // if get an error
                    if (!(e instanceof ResourceException))
                        throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error: cannot save role");
                });
    }

}
