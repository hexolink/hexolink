package com.auth.service.userservice;

import com.auth.entity.User;
import com.auth.to.UserTo;

public interface UserService {

    User create(User user);

    User getByEmail(String email);

    User getById(Integer id);

    User update(User currentUserFromHeader, UserTo updatedFields);
}
