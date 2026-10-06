package com.imdb.movie_api.config;

import com.imdb.movie_api.entity.Administrador;
import com.imdb.movie_api.repository.AdministradorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(AdministradorRepository administradorRepository, PasswordEncoder passwordEncoder) {
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (administradorRepository.count() == 0) {
            Administrador admin = new Administrador();
            admin.setNome("Admin");
            admin.setEmail("admin@movieapi.com");
            admin.setSenha(passwordEncoder.encode("admin123"));

            administradorRepository.save(admin);

            System.out.println("Admin padrão criado: admin@movieapi.com / admin123");
        }
    }
}
