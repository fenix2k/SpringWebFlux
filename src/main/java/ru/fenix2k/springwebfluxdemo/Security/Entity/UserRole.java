package ru.fenix2k.springwebfluxdemo.Security.Entity;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Value
@AllArgsConstructor
@Table("users_roles")
public class UserRole {
    @Id
    private Long user_id;
    private Long role_id;

}
