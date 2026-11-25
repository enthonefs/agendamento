package com.org.agendamento.business.dto;

import com.org.agendamento.infrastructure.entity.Barbeiro;

public record BarbeiroResponse(
        Long id,
        String nome,
        boolean ativo
) {
    public static BarbeiroResponse from(Barbeiro barbeiro) {
        return new BarbeiroResponse(barbeiro.getId(), barbeiro.getNome(), barbeiro.isAtivo());
    }
}
