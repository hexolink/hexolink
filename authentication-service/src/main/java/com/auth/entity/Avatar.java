package com.auth.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "avatars")
@Access(AccessType.FIELD)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Avatar {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @NotNull(message = "attachment must be present")
    @Column(name = "avatar")
    private byte[] avatar;
}
