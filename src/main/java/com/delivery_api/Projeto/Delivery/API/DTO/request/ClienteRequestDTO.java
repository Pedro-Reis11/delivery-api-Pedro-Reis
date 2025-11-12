package com.delivery_api.Projeto.Delivery.API.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para requisição de criação ou atualização de cliente", title = "Cliente Request DTO")
public class ClienteRequestDTO {

    @Schema(description = "Nome completo do cliente", example = "João da Silva", required = true)
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Schema(description = "Email do cliente", example = "email@email.com", required = true)
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @Schema(description = "Telefone do cliente", example = "(11) 91234-5678", required = true)
    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @Schema(description = "Endereço do cliente", example = "Rua das Flores, 123, São Paulo, SP", required = true)
    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;

}
