package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.DTO.ClienteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.ClienteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.DTOMapper;
import com.delivery_api.Projeto.Delivery.API.entity.Cliente;
import com.delivery_api.Projeto.Delivery.API.exception.BusinessException;
import com.delivery_api.Projeto.Delivery.API.exception.EntityNotFoundException;
import com.delivery_api.Projeto.Delivery.API.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private DTOMapper dtoMapper;

    /**
     * Cadastrar novo cliente
     */
    public ClienteResponseDTO cadastrar(ClienteRequestDTO requestDTO) {
        // Validar email único
        if (clienteRepository.existsByEmail(requestDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + requestDTO.getEmail());
        }

        // Criar entidade Cliente
        Cliente cliente = new Cliente();
        cliente.setNome(requestDTO.getNome());
        cliente.setEmail(requestDTO.getEmail());
        cliente.setTelefone(requestDTO.getTelefone());
        cliente.setEndereco(requestDTO.getEndereco());
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());

        // Salvar e retornar DTO
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return dtoMapper.toClienteResponseDTO(clienteSalvo);
    }

    /**
     * Buscar cliente por ID
     */
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));

        return dtoMapper.toClienteResponseDTO(cliente);
    }

    /**
     * Buscar cliente por email
     */
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com email: " + email));

        return dtoMapper.toClienteResponseDTO(cliente);
    }

    /**
     * Listar todos os clientes ativos
     */
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarAtivos() {
        return clienteRepository.findByAtivoTrue()
                .stream()
                .map(dtoMapper::toClienteResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualizar dados do cliente
     */
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO requestDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));

        // Verificar se email não está sendo usado por outro cliente
        if (!cliente.getEmail().equals(requestDTO.getEmail()) &&
                clienteRepository.existsByEmail(requestDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + requestDTO.getEmail());
        }

        // Atualizar campos
        cliente.setNome(requestDTO.getNome());
        cliente.setEmail(requestDTO.getEmail());
        cliente.setTelefone(requestDTO.getTelefone());
        cliente.setEndereco(requestDTO.getEndereco());

        Cliente clienteAtualizado = clienteRepository.save(cliente);
        return dtoMapper.toClienteResponseDTO(clienteAtualizado);
    }

    /**
     * Inativar cliente (soft delete)
     */
    public void inativar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));

        cliente.inativar();
        clienteRepository.save(cliente);
    }

    /**
     * Buscar clientes por nome
     */
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(dtoMapper::toClienteResponseDTO)
                .collect(Collectors.toList());
    }
}