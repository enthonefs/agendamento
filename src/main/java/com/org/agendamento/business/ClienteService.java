package com.org.agendamento.business;

import com.org.agendamento.infrastructure.entity.Cliente;
import com.org.agendamento.infrastructure.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> buscarTodos(){
        return clienteRepository.findAll();
    }

    public Cliente criarCliente(Cliente cliente){
        return clienteRepository.save(cliente);

    }

    public Cliente atualizarCliente(Long id, Cliente cliente){
        Cliente clienteExistente = clienteRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Id de Cliente não encontrado"));

        Cliente clienteAtualizado = Cliente.builder()
                .nome(cliente.getNome() != null ? cliente.getNome() : clienteExistente.getNome())
                .telefone(cliente.getTelefone() != null ? cliente.getTelefone() : clienteExistente.getTelefone())
                .id(clienteExistente.getId())
                .build();

        return clienteRepository.saveAndFlush(clienteAtualizado);
    }

    public void deletarCliente(Long id){
        clienteRepository.deleteById(id);
    }
}
