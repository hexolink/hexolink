package com.auth.repository.tokenrepository;

import com.auth.entity.JwtModel;

public interface TokenRepository {

    JwtModel save(JwtModel model);

    void deleteByDeviceId(String deviceId);

    void deleteByClientId(String clientId);

    JwtModel getByClientId(String clientId);

    JwtModel getByToken(String token);
}
