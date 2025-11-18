package com.delivery_api.Projeto.Delivery.API.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String tipo;
    private Long expiracao;
    private UsuarioResponseDTO usuario;

}
