package io.github.eltonhst.domain.service.useCase.user;

import io.github.eltonhst.domain.entity.UserEntity;
import io.github.eltonhst.infra.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserFindIdUseCase {

    private final UserRepository repository;

    protected UserFindIdUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public UserEntity findUserById(String id){
        return null;
    }
}
