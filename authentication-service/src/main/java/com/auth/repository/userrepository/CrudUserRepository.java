package com.auth.repository.userrepository;

import com.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Override
    @Transactional
    User save(User user);

    User getByEmail(String email);

    User getById(Integer id);
}
