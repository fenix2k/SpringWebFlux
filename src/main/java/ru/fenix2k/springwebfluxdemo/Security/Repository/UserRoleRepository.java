package ru.fenix2k.springwebfluxdemo.Security.Repository;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import ru.fenix2k.springwebfluxdemo.Security.Entity.UserRole;

public interface UserRoleRepository extends ReactiveSortingRepository<UserRole, Long> {
}
