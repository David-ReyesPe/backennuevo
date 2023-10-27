package com.backendfunda.backendfunda.dtos;

import lombok.Data;

import java.sql.Blob;

@Data
public class DtoCursos {
    private Long id;
    private String titulo;
    private String images;
    private String description;
    private String categoria;
    private String video;
}
    