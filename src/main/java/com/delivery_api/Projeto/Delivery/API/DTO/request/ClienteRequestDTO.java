package com.delivery_api.Projeto.Delivery.API.DTO.request;

import com.delivery_api.Projeto.Delivery.API.validation.ValidCEP;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para requisição de criação ou atualização de cliente",
        title = "Cliente Request DTO")
public class ClienteRequestDTO {

    @Schema(description = "Nome completo do cliente",
            example = "João da Silva",
            required = true)
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @Schema(description = "Email do cliente",
            example = "email@email.com",
            required = true)
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @Schema(description = "Telefone do cliente",
            example = "(11) 91234-5678",
            required = true)
    @NotBlank(message = "O telefone é obrigatório")
    @Size(min = 10, max = 20, message = "Telefone inválido")
    private String telefone;

    @Schema(description = "Endereço completo do cliente",
            example = "Rua das Flores, 123, São Paulo, SP",
            required = true)
    @NotBlank(message = "O endereço é obrigatório")
    @Size(min = 10, max = 255, message = "O endereço deve ter entre 10 e 255 caracteres")
    private String endereco;

    @Schema(description = "CEP do cliente",
            example = "01001-000",
            required = true)
    @NotBlank(message = "O CEP é obrigatório")
    @ValidCEP
    private String cep;
}
