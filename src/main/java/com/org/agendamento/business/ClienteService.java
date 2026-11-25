package com.org.agendamento.business;

import com.org.agendamento.business.dto.ClienteRequest;
import com.org.agendamento.infrastructure.entity.Cliente;
import com.org.agendamento.infrastructure.repository.ClienteRepository;
import com.org.agendamento.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Page<Cliente> buscarTodos(Pageable pageable){
        return clienteRepository.findAll(pageable);
    }

    public Cliente criarCliente(ClienteRequest request){
        Cliente cliente = Cliente.builder()
                .nome(request.nome())
                .telefone(request.telefone())
                .build();
        return clienteRepository.save(cliente);

    }

    public Cliente atualizarCliente(Long id, ClienteRequest request){
        Cliente clienteExistente = clienteRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cliente não encontrado."));

        clienteExistente.setNome(request.nome());
        clienteExistente.setTelefone(request.telefone());

        return clienteRepository.saveAndFlush(clienteExistente);
    }

    public void deletarCliente(Long id){
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado.");
        }
        clienteRepository.deleteById(id);
    }
}
