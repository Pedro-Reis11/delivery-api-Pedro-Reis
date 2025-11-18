package com.delivery_api.Projeto.Delivery.API.DTO.request;

import com.delivery_api.Projeto.Delivery.API.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Dados para registro de novo usuário")
public class RegisterRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome completo do usuário", example = "João Silva", required = true)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Schema(description = "Email do usuário", example = "joao@email.com", required = true)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário (mínimo 6 caracteres)", example = "123456", required = true)
    private String senha;

    @NotNull(message = "Role é obrigatória")
    @Schema(description = "Perfil de acesso", example = "CLIENTE", required = true)
    private Role role;

    @Schema(description = "ID do restaurante (obrigatório apenas para role RESTAURANTE)", example = "1")
    private Long restauranteId;
}
