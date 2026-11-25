package com.org.agendamento.business.dto;

public record BarbeiroDisponibilidadeResponse(
        BarbeiroResponse barbeiro,
        boolean disponivel
) {
}
