package ru.fenix2k.springwebfluxdemo.Security.Service;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.fenix2k.springwebfluxdemo.Components.CustomSqlQueryBuilder;
import ru.fenix2k.springwebfluxdemo.Exceptions.ResourceException;
import ru.fenix2k.springwebfluxdemo.Security.Entity.User;
import ru.fenix2k.springwebfluxdemo.Security.Repository.UserRepository;
import ru.fenix2k.springwebfluxdemo.Utils.EntityUtils;

import javax.validation.constraints.NotNull;
import java.util.function.BiFunction;

@Service
@AllArgsConstructor
public class UserService {

    private final String TABLE_NAME = "users";

    @NonNull
    private final UserRepository userRepository;

    @NotNull
    private final RoleService roleService;

    public static final BiFunction<Row, RowMetadata, User> MAPPING_FUNCTION = (row, rowMetaData) -> User.builder()
            .id(row.get("id", Long.class))
            .login(row.get("login", String.class))
            .encryptedPassword(row.get("encryptedPassword", String.class))
            .enabled(row.get("enabled", Boolean.class))
            .roles(rowMetaData.getColumnNames().contains("roles") ? row.get("roles", String.class) : "")
            .build();

    public Flux<User> findAll(Pageable pageable) {
        //return userRepository.findAll();
        CustomSqlQueryBuilder queryBuilder = CustomSqlQueryBuilder.builder()
                .select("users.*")
                .select("GROUP_CONCAT(ur.role_id SEPARATOR ',') roles")
                .from(TABLE_NAME)
                .leftJoin("users_roles ur on users.id = ur.user_id")
                .groupBy("users.id, users.login");
        return userRepository.execute(queryBuilder, MAPPING_FUNCTION, pageable)
                .flatMap(this::fetchChilds)
                .doOnError(err -> {
                    throw new ResourceException(HttpStatus.BAD_REQUEST, err.getMessage());
                });
    }

    public Flux<User> findByCriteria(String criteriaString, Pageable pageable) {
        CustomSqlQueryBuilder queryBuilder = CustomSqlQueryBuilder.builder()
                .select("users.*")
                .select("GROUP_CONCAT(ur.role_id SEPARATOR ',') roles")
                .from(TABLE_NAME)
                .leftJoin("users_roles ur on users.id = ur.user_id")
                .groupBy("users.id, users.login");

        return userRepository.execute(queryBuilder, criteriaString, MAPPING_FUNCTION, pageable)
                .flatMap(this::fetchChilds)
                .doOnError(err -> {
                    throw new ResourceException(HttpStatus.BAD_REQUEST, err.getMessage());
                });
    }

    public Mono<User> findById(Long id) {
        return userRepository.findOneById(id)
                .flatMap(this::fetchChilds)
                .switchIfEmpty(Mono.error(new ResourceException(HttpStatus.NOT_FOUND, "User not found with id [" + id + "]")));
    }

    public Mono<User> save(User user) {
        return this.validateAndSave(user, "save");
    }

    public Mono<User> update(User user) {
        return this.validateAndSave(user, "update");
    }

    public Mono<User> patch(User user) {
        return this.validateAndSave(user, "patch");
    }

    public Mono<Void> delete(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceException(HttpStatus.NOT_FOUND, "User not found with id [" + id + "]")))
                .then(userRepository.deleteById(id));
    }

    private Mono<User> fetchChilds(User user) {
        return Mono.just(user)
                .flatMap(it -> {
                    if (it.getRoles() == null || it.getRoles().isEmpty()) return Mono.just(it);
                    for (String roleId : it.getRoles().split(","))
                        it.getRoleList().add(roleService.findById(Long.parseLong(roleId.trim())).block());
                    return Mono.just(it);
                })
                .doOnError(ex -> {
                    throw new ResourceException(HttpStatus.NOT_FOUND, "Occurred some error");
                });
    }

    private Mono<User> validateAndSave(User user, String operation) {
        return Mono.just(user)
                .flatMap(it -> { // save or update/patch ?
                    if (it.getId() == null)
                        return Mono.just(it);
                    return userRepository.findById(it.getId())
                            .switchIfEmpty(Mono.error(new ResourceException(HttpStatus.NOT_FOUND, "User is not found with id: [" + user.getId() + "]")));
                })
                .flatMap(it -> Mono.just(it) // check than specified email is unique
                        .filter(usr -> user.getId() == null || user.getLogin() != null && !usr.getLogin().equals(user.getLogin())).log()
                        .flatMap(usr -> userRepository.findOneByLogin(user.getLogin())).log()
                        .flatMap(usr -> Mono.<User>error(new ResourceException(HttpStatus.CONFLICT, "User is already exists with login [" + user.getLogin() + "]")))
                        .switchIfEmpty(Mono.just(it))
                )
                .flatMap(it -> { // save operation
                    if (operation.equals("patch"))
                        EntityUtils.copyNonNullProperties(user, it);
                    if (operation.equals("update"))
                        return userRepository.save(user);
                    return userRepository.save(it);
                })
                .doOnError(e -> { // if get an error
                    if (!(e instanceof ResourceException))
                        throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error: cannot save user");
                });
    }

}
