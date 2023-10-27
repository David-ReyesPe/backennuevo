package com.backendfunda.backendfunda.controller;

import com.backendfunda.backendfunda.model.Usuarios;
import com.backendfunda.backendfunda.repository.UsuariosRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TokenExpirationChecker {

    private final UsuariosRepository usuariosRepository;

    public TokenExpirationChecker(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Scheduled(fixedRate = 120000) // 120000 ms = 2 minutos
    public void checkTokenExpiration() {
        LocalDateTime now = LocalDateTime.now();

        List<Usuarios> usuariosConTokens = usuariosRepository.findByTokenRecuperacionIsNotNull();

        for (Usuarios usuario : usuariosConTokens) {
            LocalDateTime tokenExpiracion = usuario.getTokenExpiracion();
            if (tokenExpiracion != null && now.isAfter(tokenExpiracion)) {
                // El token ha expirado, elimínalo
                usuario.setTokenRecuperacion(null);
                usuario.setTokenExpiracion(null);
                usuariosRepository.save(usuario);
            }
        }
    }
}
