package com.delivery_api.Projeto.Delivery.API.service.impl;

import com.delivery_api.Projeto.Delivery.API.DTO.request.ClienteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.ClienteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Cliente;
import com.delivery_api.Projeto.Delivery.API.exception.BusinessException;
import com.delivery_api.Projeto.Delivery.API.exception.EntityNotFoundException;
import com.delivery_api.Projeto.Delivery.API.repository.ClienteRepository;
import com.delivery_api.Projeto.Delivery.API.service.ClienteService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    // ============================================================
    // CADASTRAR
    // ============================================================
    @Override
    public ClienteResponseDTO cadastrar(ClienteRequestDTO requestDTO) {

        if (clienteRepository.existsByEmail(requestDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + requestDTO.getEmail());
        }

        Cliente cliente = modelMapper.map(requestDTO, Cliente.class);
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());

        Cliente salvo = clienteRepository.save(cliente);
        return modelMapper.map(salvo, ClienteResponseDTO.class);
    }

    // ============================================================
    // BUSCAR POR ID
    // ============================================================
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));

        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    // ============================================================
    // BUSCAR POR EMAIL
    // ============================================================
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com email: " + email));

        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    // ============================================================
    // LISTAR ATIVOS
    // ============================================================
    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarAtivos() {
        return clienteRepository.findByAtivoTrue()
                .stream()
                .map(c -> modelMapper.map(c, ClienteResponseDTO.class))
                .toList();
    }

    // ============================================================
    // ATUALIZAR
    // ============================================================
    @Override
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO requestDTO) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));

        boolean emailMudou = !cliente.getEmail().equals(requestDTO.getEmail());

        if (emailMudou && clienteRepository.existsByEmail(requestDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + requestDTO.getEmail());
        }

        // Atualização controlada (sem sobrescrever campos críticos)
        cliente.setNome(requestDTO.getNome());
        cliente.setEmail(requestDTO.getEmail());
        cliente.setTelefone(requestDTO.getTelefone());
        cliente.setEndereco(requestDTO.getEndereco());
        cliente.setCep(requestDTO.getCep());

        Cliente atualizado = clienteRepository.save(cliente);
        return modelMapper.map(atualizado, ClienteResponseDTO.class);
    }

    // ============================================================
    // ATIVAR / DESATIVAR
    // ============================================================
    @Override
    public ClienteResponseDTO ativarDesativar(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));

        cliente.setAtivo(!cliente.getAtivo());

        Cliente atualizado = clienteRepository.save(cliente);
        return modelMapper.map(atualizado, ClienteResponseDTO.class);
    }

    // ============================================================
    // BUSCAR POR NOME
    // ============================================================
    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(c -> modelMapper.map(c, ClienteResponseDTO.class))
                .toList();
    }
}
