package com.auth.service.avatar;

import com.auth.entity.Avatar;
import com.auth.repository.avatarrepository.AvatarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Slf4j
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository repository;

    @Autowired
    public AvatarServiceImpl(AvatarRepository repository) {
        this.repository = repository;
    }

    @Override
    public Avatar create(Avatar attachment) {
        Assert.notNull(attachment, "user must not be null");
        return repository.save(attachment);
    }

    @Override
    public Avatar get(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteByUserId(id);
    }
}
