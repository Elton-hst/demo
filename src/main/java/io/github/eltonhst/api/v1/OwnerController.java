package io.github.eltonhst.api.v1;

import io.github.eltonhst.domain.entity.OwnerEntity;
import io.github.eltonhst.domain.useCase.owner.OwnerSearchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    private final OwnerSearchUseCase searchOwner;

    public OwnerController(OwnerSearchUseCase searchOwner) {
        this.searchOwner = searchOwner;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerEntity> searchOwner(@PathVariable UUID userId) {
        final var result = searchOwner.execute(userId)
                .getOrElseThrow(error -> error);
        return ResponseEntity.ok(result);
    }

}
