package com.auth.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class UserTo {
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    private String bio;
    @JsonProperty("birth_date")
    private LocalDate birthDate;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("temporary_avatar_before_saving")
    private byte[] temporaryAvatarBeforeSaving;
}
