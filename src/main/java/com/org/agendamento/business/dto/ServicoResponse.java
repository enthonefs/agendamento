package com.org.agendamento.business.dto;

import com.org.agendamento.infrastructure.entity.Servico;

import java.math.BigDecimal;

public record ServicoResponse(
        Long id,
        String servico,
        BigDecimal valor
) {
    public static ServicoResponse from(Servico servico) {
        return new ServicoResponse(
                servico.getId(),
                servico.getServico(),
                servico.getValor()
        );
    }
}
