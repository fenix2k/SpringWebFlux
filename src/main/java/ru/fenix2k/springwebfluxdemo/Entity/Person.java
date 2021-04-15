package ru.fenix2k.springwebfluxdemo.Entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("persons")
public class Person
{

    @JsonView(PersonJsonView.Min.class)
    @Id
    private Long id;

    @JsonView(PersonJsonView.Min.class)
    @Size(min = 5, max = 255, message = "Name length must be between 2 and 255 characters")
    private String name;

    @JsonView(PersonJsonView.Middle.class)
    @Email
    @Size(max = 255)
    private String email;

    @JsonView(PersonJsonView.Full.class)
    @JsonFilter("")
    private LocalDate birthday;

    @JsonView(PersonJsonView.Full.class)
    @Size(min = 9, max = 20)
    private String phone;

    @JsonView(PersonJsonView.Middle.class)
    @Size(min = 2, max = 255)
    private String city;

}
