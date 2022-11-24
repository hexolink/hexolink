package com.auth.repository.userrepository;

import com.auth.entity.User;
import com.auth.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DataJpaUserRepositoryImpl implements UserRepository {

    private final CrudUserRepository repository;

    @Autowired
    public DataJpaUserRepositoryImpl(CrudUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User getByEmail(String email) throws NotFoundException {
        return repository.getByEmail(email);
    }

    @Override
    public User getById(Integer id) {
        return repository.getById(id);
    }

    @Override
    public User update(User user) {
        return repository.save(user);
    }
}
