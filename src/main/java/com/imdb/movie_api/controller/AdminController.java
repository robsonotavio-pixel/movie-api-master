package com.imdb.movie_api.controller;
   
import com.imdb.movie_api.dto.AdministradorCreateDTO;
import com.imdb.movie_api.dto.AdministradorResponseDTO;
import com.imdb.movie_api.dto.AdministradorUpdateDTO;
import com.imdb.movie_api.dto.UsuarioResponseDTO;
import com.imdb.movie_api.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/admins")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public AdministradorResponseDTO criar(@RequestBody @Valid AdministradorCreateDTO dto) {
        return adminService.criar(dto);
    }

    @PutMapping("/{id}")
    public AdministradorResponseDTO editar(@PathVariable Long id, @RequestBody @Valid AdministradorUpdateDTO dto) {
        return adminService.editar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void desativar(@PathVariable Long id) {
        adminService.desativar(id);
    }

    @GetMapping("/usuarios")
    public Page<UsuarioResponseDTO> listarUsuariosAtivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return adminService.listarUsuariosAtivos(page, size);
    }
}