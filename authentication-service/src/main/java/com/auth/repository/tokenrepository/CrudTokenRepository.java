package com.auth.repository.tokenrepository;

import com.auth.entity.JwtModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudTokenRepository extends JpaRepository<JwtModel, Integer> {

    @Override
    @Transactional
    JwtModel save(JwtModel model);

    @Modifying
    @Transactional
    @Query("DELETE FROM JwtModel m WHERE m.deviceId=:deviceId")
    void deleteByDeviceId(String deviceId);

    @Modifying
    @Transactional
    @Query("DELETE FROM JwtModel m WHERE m.userId=:userId")
    void deleteByUserId(String userId);

    JwtModel getByUserId(String userId);

    JwtModel getByToken(String token);
}
