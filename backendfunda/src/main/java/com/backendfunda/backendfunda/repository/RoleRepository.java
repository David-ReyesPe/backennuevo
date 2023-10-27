package com.backendfunda.backendfunda.repository;

import com.backendfunda.backendfunda.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    //Método para buscar un role por su nombre en nuestra base de datos
    Optional<Roles> findByNombre(String nombre);

}
