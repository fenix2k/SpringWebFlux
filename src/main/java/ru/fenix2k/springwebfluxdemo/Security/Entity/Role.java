package ru.fenix2k.springwebfluxdemo.Security.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("roles")
@AllArgsConstructor
public class Role {

    @JsonView(JsonViewLevel.Min.class)
    @Id
    private Long id;
    @JsonView(JsonViewLevel.Min.class)
    private String name;
    @JsonView(JsonViewLevel.Middle.class)
    private String description;

}
