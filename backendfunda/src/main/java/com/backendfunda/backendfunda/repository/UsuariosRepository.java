package com.backendfunda.backendfunda.repository;

import com.backendfunda.backendfunda.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByNombre(String nombre);
    Boolean existsByNombre(String nombre);

    // Método para buscar un usuario por su correo electrónico
    Optional<Usuarios> findByCorreo(String correo);

    // Método para buscar un usuario por su token de recuperación
    Optional<Usuarios> findByTokenRecuperacion(String token);

    List<Usuarios> findByTokenRecuperacionIsNotNull(); // Agregar este método

}