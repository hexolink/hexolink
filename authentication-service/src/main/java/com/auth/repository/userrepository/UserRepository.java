package com.auth.repository.userrepository;

import com.auth.entity.User;
import com.auth.to.UserTo;

public interface UserRepository {
    User save(User user);

    User getByEmail(String email);

    User getById(Integer id);

    User update(User user);
}
