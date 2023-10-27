package com.backendfunda.backendfunda.controller;

import com.backendfunda.backendfunda.dtos.DtoCursos;
import com.backendfunda.backendfunda.model.Cursos;
import com.backendfunda.backendfunda.repository.CursosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth/")
public class CursosController {

   @Autowired
    private CursosRepository cursosRepository;

   @GetMapping("/cursos")
   public List<Cursos> getCursos() {
    return cursosRepository.findAll();
    }

    @PostMapping("/editCursos")
    public ResponseEntity<String> crearCurso(@RequestBody DtoCursos dtoCursos) {
        if (cursosRepository.existsByTitulo(dtoCursos.getTitulo())) {
            return new ResponseEntity<>("el usuario ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }
        Cursos cursos = new Cursos();
        cursos.setTitulo(dtoCursos.getTitulo());
        cursos.setDescription(dtoCursos.getDescription());
        cursos.setImages(dtoCursos.getImages());
        cursos.setVideo(dtoCursos.getVideo());
        cursos.setCategoria(dtoCursos.getCategoria());
        cursosRepository.save(cursos);
        return new ResponseEntity<>("Registro de cursos exitoso", HttpStatus.OK);
    }
}
