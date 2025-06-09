package io.github.eltonhst.api.v1;

import io.github.eltonhst.api.dto.UserDTO;
import io.github.eltonhst.domain.entity.UserEntity;
import io.github.eltonhst.domain.useCase.user.UserCreateUseCase;
import io.github.eltonhst.domain.useCase.user.UserCurrentUseCase;
import io.github.eltonhst.domain.useCase.user.UserSearchUseCase;
import io.github.eltonhst.infra.mapper.MapperUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserCurrentUseCase currentUser;
    private final UserCreateUseCase createUseCase;
    private final UserSearchUseCase searchUser;

    public UserController(UserCurrentUseCase currentUser, UserCreateUseCase createUseCase, UserSearchUseCase searchUser) {
        this.currentUser = currentUser;
        this.createUseCase = createUseCase;
        this.searchUser = searchUser;
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<UUID> createUser(@RequestBody UserDTO userDTO, UriComponentsBuilder uriBuilder) {
        final UUID userId = createUseCase.execute(userDTO).getOrElseThrow(error -> error);
        final var url = userDTO.isOwner() ? "api/owner/{id}" : "api/client/{id}";
        final var uri = uriBuilder.path(url).buildAndExpand(userId).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<UserEntity> getUserById(Principal principal) {
        final var result = searchUser.findById(principal).getOrElseThrow(error -> error);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/current")
    public ResponseEntity<UserDTO> findUserCurrent() {
        var result = currentUser.execute().getOrElseThrow(error -> error);
        return ResponseEntity.ok(MapperUser.toDTO(result));
    }

}
