package io.github.eltonhst.domain.useCase.owner;

import io.github.eltonhst.domain.entity.OwnerEntity;
import io.github.eltonhst.domain.entity.UserEntity;
import io.github.eltonhst.domain.enums.AuthEnum;
import io.github.eltonhst.domain.exception.BadRequestException;
import io.github.eltonhst.domain.repository.OwnerRepository;
import io.github.eltonhst.domain.useCase.auth.AuthAddRoleUseCase;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class OwnerCreateUseCase {

    private final OwnerRepository repository;
    private final AuthAddRoleUseCase addRoleToUser;

    public OwnerCreateUseCase(OwnerRepository repository, AuthAddRoleUseCase addRoleToUser) {
        this.repository = repository;
        this.addRoleToUser = addRoleToUser;
    }

    @Transactional
    public Either<RuntimeException, UUID> execute(UserEntity user) {
        try {
            log.info("[Service] Iniciando a criação do usuário {}", user.getUsername());
            addRoleToUser.execute(
                    user.getUserId(),
                    AuthEnum.OWNER.getRole()
            );
            final OwnerEntity owner = getOwnerEntity(user);
            return repository.createOwner(owner);
        } catch (RuntimeException e) {
            log.error("[Service] Erro ao tentar criar um novo Owner");
            return Either.left(new BadRequestException(e.getMessage()));
        }
    }

    private static OwnerEntity getOwnerEntity(UserEntity user) {
        return OwnerEntity.builder()
                .id(UUID.randomUUID())
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
