package com.auth.util.converters;

import com.auth.entity.Role;
import com.auth.entity.User;
import com.auth.to.GoogleUserTo;
import com.auth.to.UserTo;

import java.time.LocalDateTime;
import java.util.Set;

public class UserConverter {

    public static User createNewFromTo(GoogleUserTo googleUserTo, Set<Role> role) {
        String[] firstLastName = googleUserTo.getName().split(" ");
        LocalDateTime updatedRegistered = LocalDateTime.now();
        return new User(firstLastName[0],
                firstLastName[1],
                googleUserTo.getEmail(),
                googleUserTo.getPictureUrl(),
                null,
                updatedRegistered,
                updatedRegistered,
                null,
                null,
                role);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setEmail(userTo.getEmail());
        user.setFirstName(userTo.getFirstName());
        user.setLastName(userTo.getLastName());
        user.setAvatarUrl(userTo.getAvatarUrl());
        user.setUpdated(LocalDateTime.now());
        user.setBio(userTo.getBio());
        user.setBirthDate(userTo.getBirthDate());
        user.setGender(userTo.getGender());
        return user;
    }
}

