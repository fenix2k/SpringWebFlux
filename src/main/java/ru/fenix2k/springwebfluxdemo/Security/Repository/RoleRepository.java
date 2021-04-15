package ru.fenix2k.springwebfluxdemo.Security.Repository;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;
import ru.fenix2k.springwebfluxdemo.Repository.CustomQueryRepository;
import ru.fenix2k.springwebfluxdemo.Security.Entity.Role;

public interface RoleRepository extends ReactiveSortingRepository<Role, Long>, CustomQueryRepository {
    Mono<Role> findOneByName(String name);
}
