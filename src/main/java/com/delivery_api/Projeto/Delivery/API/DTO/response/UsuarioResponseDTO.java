package com.delivery_api.Projeto.Delivery.API.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private boolean ativo;
    private Long restauranteId;
    private String role;
}
