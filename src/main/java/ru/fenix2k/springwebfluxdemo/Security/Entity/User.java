package ru.fenix2k.springwebfluxdemo.Security.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@Table("users")
@AllArgsConstructor
public class User implements UserDetails {

    @JsonView(JsonViewLevel.Min.class)
    @Id
    private Long id;
    @JsonView(JsonViewLevel.Min.class)
    private String login;
    private String encryptedPassword;
    @JsonView(JsonViewLevel.Min.class)
    @Builder.Default
    private List<Role> roleList = new ArrayList<>();
    @JsonView(JsonViewLevel.Middle.class)
    @Builder.Default
    private String roles = "";
    @JsonView(JsonViewLevel.Min.class)
    private Boolean enabled;

    @JsonView({JsonViewLevel.Full.class})
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return encryptedPassword;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
