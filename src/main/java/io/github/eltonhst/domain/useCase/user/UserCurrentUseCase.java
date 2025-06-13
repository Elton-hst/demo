package io.github.eltonhst.domain.useCase.user;

import io.github.eltonhst.domain.entity.UserEntity;
import io.github.eltonhst.domain.enums.AuthEnum;
import io.github.eltonhst.domain.exception.NotFoundException;
import io.github.eltonhst.domain.useCase.client.ClientSearchUseCase;
import io.github.eltonhst.domain.useCase.owner.OwnerSearchUseCase;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserCurrentUseCase {

    private final UserSearchUseCase searchUser;
    private final OwnerSearchUseCase searchOwner;
    private final ClientSearchUseCase searchClient;

    public UserCurrentUseCase(UserSearchUseCase searchUser, OwnerSearchUseCase searchOwner, ClientSearchUseCase searchClient) {
        this.searchUser = searchUser;
        this.searchOwner = searchOwner;
        this.searchClient = searchClient;
    }

    public Either<RuntimeException, UserEntity> execute() {
        log.info("[Service] Iniciando a busca do usuário logado");
        final var result = currentUser();

        if (result.isLeft()) {
            log.error("[Service] Erro ao tentar encontrar o usuário atual");
            return Either.left(result.getLeft());
        }

        final UserEntity userEntity = result.get();

        if (userEntity.getRoles().contains(AuthEnum.OWNER)) {
            final var owner = searchOwner.execute(userEntity.getUserId());
            if (owner.isLeft()) {
                log.error("[Service] Erro ao tentar encontrar o usuário atual");
                return Either.left(owner.getLeft());
            }

            userEntity.setUserId(owner.get().getId());
            log.info("[Service] Sucesso na busca do usuário logado atual");
            return Either.right(userEntity);
        }

        final var client = searchClient.execute(userEntity.getUserId());
        if (client.isLeft()) {
            log.error("[Service] Erro ao tentar encontrar o usuário atual");
            return Either.left(client.getLeft());
        }

        userEntity.setUserId(client.get().getId());

        log.info("[Service] Sucesso na busca do usuário logado atual");
        return Either.right(userEntity);
    }

    private Either<RuntimeException, UserEntity> currentUser() {
        final var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal == null) {
            return Either.left(new NotFoundException("Usuário não encontrado"));
        }

        final var jwt = (org.springframework.security.oauth2.jwt.Jwt) principal;
        final var realmAccess = jwt.getClaimAsMap("realm_access");

        @SuppressWarnings("unchecked") final var roles = (List<String>) realmAccess.get("roles");

        final var userId = UUID.fromString(jwt.getSubject());
        return searchUser.findById(userId).map(user -> {
            user.setRoles(roles);
            return user;
        });
    }

}
