package com.auth.service.token;

import com.auth.entity.JwtModel;
import com.auth.repository.tokenrepository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
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
    public void deleteByClientId(String id) {
        tokenRepository.deleteByClientId(id);
    }

    public JwtModel getByClientId(String clientId) {
        return tokenRepository.getByClientId(clientId);
    }

    @Override
    public JwtModel getByToken(String token) {
        return tokenRepository.getByToken(token);
    }
}
