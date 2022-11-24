package com.auth.repository.tokenrepository;

import com.auth.entity.JwtModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DataJpaTokenRepositoryImpl implements TokenRepository {

    private final CrudTokenRepository tokenRepository;

    @Autowired
    public DataJpaTokenRepositoryImpl(CrudTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public JwtModel save(JwtModel model) {
        return tokenRepository.save(model);
    }

    @Override
    public void deleteByDeviceId(String deviceId) {
        tokenRepository.deleteByDeviceId(deviceId);
    }

    @Override
    public void deleteByClientId(String clientId) {
        tokenRepository.deleteByUserId(clientId);
    }

    @Override
    public JwtModel getByClientId(String clientId) {
        return tokenRepository.getByUserId(clientId);
    }

    @Override
    public JwtModel getByToken(String token) {
        return tokenRepository.getByUserId(token);
    }
}
