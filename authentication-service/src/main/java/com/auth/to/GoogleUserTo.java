package com.auth.to;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoogleUserTo implements Serializable {

    private String name;
    private String email;
    private String pictureUrl;
    private String signInProvider;
    private Device device;
}
