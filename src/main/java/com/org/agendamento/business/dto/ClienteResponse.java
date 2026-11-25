package com.org.agendamento.business.dto;

import com.org.agendamento.infrastructure.entity.Cliente;

public record ClienteResponse(
        Long id,
        String nome,
        String telefone
) {
    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getTelefone());
    }
}
