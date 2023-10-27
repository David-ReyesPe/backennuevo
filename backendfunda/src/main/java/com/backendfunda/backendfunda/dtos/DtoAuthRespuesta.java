package com.backendfunda.backendfunda.dtos;

import lombok.Data;

@Data
//Esta clase va a ser la que nos devolverá la información con el token y el tipo que tenga este
public class DtoAuthRespuesta{
    private String accessToken;
    private String tokenType = "Bearer ";

    public DtoAuthRespuesta(String accessToken) {
        this.accessToken = accessToken;
    }
}
