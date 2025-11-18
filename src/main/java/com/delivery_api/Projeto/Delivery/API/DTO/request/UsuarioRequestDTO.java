package com.delivery_api.Projeto.Delivery.API.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {

    private String nome;
    private String email;
    private String senha;
    private Long restauranteId;
    private String role;
}
