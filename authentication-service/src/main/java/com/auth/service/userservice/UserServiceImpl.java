package com.auth.service.userservice;

import com.auth.entity.Avatar;
import com.auth.entity.User;
import com.auth.repository.avatarrepository.AvatarRepository;
import com.auth.repository.userrepository.UserRepository;
import com.auth.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.auth.util.converters.UserConverter.updateFromTo;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final AvatarRepository avatarRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository, AvatarRepository avatarRepository) {
        this.repository = repository;
        this.avatarRepository = avatarRepository;
    }

    @Override
    public User create(User user) {
        User existedUser = repository.getByEmail(user.getEmail());
        if (existedUser == null) {
            existedUser = repository.save(user);
        }
        return existedUser;
    }

    @Override
    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    @Override
    public User getById(Integer id) {
        return repository.getById(id);
    }

    @Override
    public User update(User currentUserFromHeader, UserTo updatedFields) {
        if (updatedFields.getTemporaryAvatarBeforeSaving() != null) {
            avatarRepository.deleteByUserId(currentUserFromHeader.getId());
            avatarRepository.save(
                    new Avatar(
                            currentUserFromHeader.getId(),
                            updatedFields.getTemporaryAvatarBeforeSaving()
                    )
            );
            updatedFields.setAvatarUrl("http://192.168.99.104:8060/uaa/rest/users/avatar");
        }
        User user = updateFromTo(currentUserFromHeader, updatedFields);
        return repository.update(user);
    }
}
