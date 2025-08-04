package io.github.eltonhst.api.v1;

import io.github.eltonhst.api.dto.AuthDTO;
import io.github.eltonhst.api.dto.CredentialsDTO;
import io.github.eltonhst.domain.useCase.auth.AuthLoginUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthLoginUseCase loginUseCase;

    public AuthController(AuthLoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping
    public ResponseEntity<AuthDTO> singIn(@RequestBody @Valid CredentialsDTO credentials) {
        var result = loginUseCase.execute(credentials).getOrElseThrow(error -> error);
        return ResponseEntity.ok(result);
    }

}
