package it.exprivia.Scuola.controller;

import it.exprivia.Scuola.models.dto.LoginRequest;
import it.exprivia.Scuola.models.dto.LoginResponse;
import it.exprivia.Scuola.services.ILogin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {

    private final ILogin loginService;

    @PostMapping("")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
       return ResponseEntity.ok(loginService.login(loginRequest));
    }
    }


