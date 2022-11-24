package com.auth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import static com.auth.entity.Color.randomColor;

@Getter
@NoArgsConstructor
@Entity
@Setter
@ToString
@Table(name = "users")
public class User extends AbstractUserSequenceEntity {

    @Column(name = "first_name", nullable = false)
    @JsonProperty("first_name")
    @NotBlank
    private String firstName;

    @Column(name = "last_name", nullable = false, unique = true)
    @JsonProperty("last_name")
    @NotBlank
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(name = "avatar_url")
    @JsonProperty("avatar_url")
    private String avatarUrl;

    @Column(name = "registered", columnDefinition = "timestamp default now()")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime registered;

    @Column(name = "updated", columnDefinition = "timestamp default now()")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updated;

    @Column(name = "color")
    private String color;

    @Column(name = "bio")
    private String bio;

    @Column(name = "birth_date")
    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @Column(name = "gender")
    private String gender;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User(
            String firstName,
            String lastName,
            String email,
            String avatarUrl,
            String bio,

            LocalDateTime registered,
            LocalDateTime updated,
            LocalDate birthDate,

            String gender,
            Collection<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.registered = registered;
        this.updated = updated;
        this.color = randomColor();
        this.bio = bio;
        this.birthDate = birthDate;
        this.gender = gender;
        setRoles(roles);
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }
}
