package com.imdb.movie_api;

import com.imdb.movie_api.dto.FilmeCreateDTO;
import com.imdb.movie_api.dto.FilmeResponseDTO;
import com.imdb.movie_api.dto.LoginRequestDTO;
import com.imdb.movie_api.dto.LoginResponseDTO;
import com.imdb.movie_api.dto.UsuarioCreateDTO;
import com.imdb.movie_api.dto.VotoCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmeApiIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private String loginEAdquirirToken(String email, String senha) {
        LoginRequestDTO login = new LoginRequestDTO(email, senha);
        ResponseEntity<LoginResponseDTO> resposta = rest.postForEntity(url("/auth/login"), login, LoginResponseDTO.class);
        return resposta.getBody().token();
    }

    @Test
    void fluxoCompletoDeCadastroVotoEListagem() {
        String sufixo = String.valueOf(System.currentTimeMillis());

        // 1. login como admin (criado pelo DataSeeder)
        String tokenAdmin = loginEAdquirirToken("admin@movieapi.com", "admin123");
        assertThat(tokenAdmin).isNotBlank();

        // 2. admin cadastra um filme
        HttpHeaders headersAdmin = new HttpHeaders();
        headersAdmin.setBearerAuth(tokenAdmin);

        FilmeCreateDTO filmeDto = new FilmeCreateDTO(
                "Filme Teste " + sufixo,
                "Diretor Teste",
                "Ficção",
                java.util.List.of("Ator Teste")
        );

        ResponseEntity<FilmeResponseDTO> respostaFilme = rest.postForEntity(
                url("/filmes"),
                new HttpEntity<>(filmeDto, headersAdmin),
                FilmeResponseDTO.class
        );

        assertThat(respostaFilme.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long filmeId = respostaFilme.getBody().id();
        assertThat(filmeId).isNotNull();

        // 3. cadastra um usuario novo (endpoint publico)
        UsuarioCreateDTO usuarioDto = new UsuarioCreateDTO("Usuario Teste", "usuario" + sufixo + "@teste.com", "123456");
        ResponseEntity<Void> respostaCadastro = rest.postForEntity(url("/usuarios"), usuarioDto, Void.class);
        assertThat(respostaCadastro.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 4. login como esse usuario
        String tokenUsuario = loginEAdquirirToken(usuarioDto.email(), usuarioDto.senha());
        assertThat(tokenUsuario).isNotBlank();

        // 5. usuario vota no filme
        HttpHeaders headersUsuario = new HttpHeaders();
        headersUsuario.setBearerAuth(tokenUsuario);

        VotoCreateDTO votoDto = new VotoCreateDTO(4);
        ResponseEntity<Void> respostaVoto = rest.postForEntity(
                url("/filmes/" + filmeId + "/votos"),
                new HttpEntity<>(votoDto, headersUsuario),
                Void.class
        );
        assertThat(respostaVoto.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 6. detalhe do filme mostra media atualizada
        ResponseEntity<FilmeResponseDTO> respostaDetalhe = rest.getForEntity(url("/filmes/" + filmeId), FilmeResponseDTO.class);
        assertThat(respostaDetalhe.getBody().totalVotos()).isEqualTo(1L);
        assertThat(respostaDetalhe.getBody().mediaVotos()).isEqualTo(4.0);

        // 7. usuario comum NAO pode cadastrar filme (403)
        ResponseEntity<String> respostaBloqueada = rest.exchange(
                url("/filmes"),
                HttpMethod.POST,
                new HttpEntity<>(filmeDto, headersUsuario),
                String.class
        );
        assertThat(respostaBloqueada.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
