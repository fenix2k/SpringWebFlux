package ru.fenix2k.springwebfluxdemo.Security.Repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;
import ru.fenix2k.springwebfluxdemo.Repository.CustomQueryRepository;
import ru.fenix2k.springwebfluxdemo.Security.Entity.User;

public interface UserRepository extends ReactiveSortingRepository<User, Long>, CustomQueryRepository {
    @Query("SELECT u.*, GROUP_CONCAT(ur.role_id SEPARATOR ',') roles " +
            "FROM users u " +
            "LEFT JOIN users_roles ur on u.id = ur.user_id " +
            "WHERE u.id=$1 " +
            "GROUP BY u.id, u.login")
    Mono<User> findOneById(Long id);

    @Query("SELECT u.id,u.login,u.encryptedPassword,u.enabled,r.roles FROM users u JOIN roles r on user_id = r.id where u.login = :login LIMIT 1")
    Mono<User> findOneByLogin(String login);
}
