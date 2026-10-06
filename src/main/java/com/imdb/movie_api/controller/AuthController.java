package com.imdb.movie_api.controller;
import com.imdb.movie_api.dto.LoginRequestDTO;
import com.imdb.movie_api.dto.LoginResponseDTO;
import com.imdb.movie_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
   public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO request) {

     return authService.login(request);
    }
    
}
