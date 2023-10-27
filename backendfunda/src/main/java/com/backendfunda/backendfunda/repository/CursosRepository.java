package com.backendfunda.backendfunda.repository;

import com.backendfunda.backendfunda.model.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CursosRepository extends JpaRepository <Cursos, Long> {
    Optional<Cursos> findById(Long idCursos);

    Boolean existsByTitulo(String titulo);
}
