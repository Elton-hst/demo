package io.github.eltonhst.api.v1;

import io.github.eltonhst.domain.entity.ClientEntity;
import io.github.eltonhst.domain.useCase.client.ClientSearchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientSearchUseCase searchClient;

    public ClientController(ClientSearchUseCase searchClient) {
        this.searchClient = searchClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> searchClient(@PathVariable UUID userId) {
        final var result = searchClient.execute(userId)
                .getOrElseThrow(error -> error);
        return ResponseEntity.ok(result);
    }

}
