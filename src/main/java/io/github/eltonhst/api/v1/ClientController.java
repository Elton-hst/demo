package io.github.eltonhst.api.v1;

import io.github.eltonhst.api.dto.UserDTO;
import io.github.eltonhst.domain.entity.ClientEntity;
import io.github.eltonhst.domain.useCase.client.ClientCreateUseCase;
import io.github.eltonhst.domain.useCase.client.ClientSearchUseCase;
import io.github.eltonhst.infra.mapper.MapperUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientSearchUseCase searchClient;
    private final ClientCreateUseCase createClient;

    public ClientController(ClientSearchUseCase searchClient, ClientCreateUseCase createClient) {
        this.searchClient = searchClient;
        this.createClient = createClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> searchClient(@PathVariable UUID userId) {
        final var result = searchClient.execute(userId)
                .getOrElseThrow(error -> error);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<ClientEntity> createClient(@RequestBody UserDTO userDTO, UriComponentsBuilder uriBuilder) {
        final var result = createClient.execute(MapperUser.toEntity(userDTO, UUID.randomUUID()))
                .getOrElseThrow(error -> error);
        final var url = "/api/client{id}";
        final var uri = uriBuilder.path(url).buildAndExpand(result).toUri();
        return ResponseEntity.created(uri).build();
    }

}
