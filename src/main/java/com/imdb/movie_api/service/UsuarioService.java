package com.imdb.movie_api.service;
import com.imdb.movie_api.dto.UsuarioCreateDTO;
import com.imdb.movie_api.dto.UsuarioResponseDTO;
import com.imdb.movie_api.dto.UsuarioUpdateDTO;
import com.imdb.movie_api.entity.Usuario;
import com.imdb.movie_api.exception.ResourceNotFoundException;
import com.imdb.movie_api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
       private final UsuarioRepository usuarioRepository;
       private final PasswordEncoder passwordEncoder;

       public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
              this.usuarioRepository = usuarioRepository;
              this.passwordEncoder = passwordEncoder;
       }
    
      public UsuarioResponseDTO criar(UsuarioCreateDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Email ja cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));

        Usuario salvo = usuarioRepository.save(usuario);
        return toResponseDTO(salvo);
      }

       public UsuarioResponseDTO editar(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));


                usuario.setNome(dto.nome());
                usuario.setEmail(dto.email());

                Usuario salvo = usuarioRepository.save(usuario);
                return toResponseDTO(salvo);
       }
       public void desativar(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
         
       usuario.setAtivo(false);
       usuarioRepository.save(usuario);
       }
       private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
              return new UsuarioResponseDTO(usuario.getId(),usuario.getNome(),usuario.getEmail(),usuario.getAtivo());
       }

       


}
