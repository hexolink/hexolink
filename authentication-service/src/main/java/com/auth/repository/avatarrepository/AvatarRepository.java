package com.auth.repository.avatarrepository;

import com.auth.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AvatarRepository extends JpaRepository<Avatar, Integer> {

    @Override
    @Transactional
    Avatar save(Avatar user);

    Optional<Avatar> findById(Integer userId);

    @Transactional
    @Modifying
    void deleteByUserId(Integer userId);
}
