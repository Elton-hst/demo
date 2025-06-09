package io.github.eltonhst.api.v1;

import io.github.eltonhst.api.dto.AuthDTO;
import io.github.eltonhst.api.dto.UserDTO;
import io.github.eltonhst.domain.useCase.auth.AuthLoginUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthLoginUseCase loginUseCase;

    public AuthController(AuthLoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping
    public ResponseEntity<AuthDTO> singIn(@RequestBody @Valid UserDTO user) {
        var result = loginUseCase.execute(user).getOrElseThrow(error -> error);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<String> getToken() {
        return null;
    }

}
