package com.org.agendamento.business.dto;

import com.org.agendamento.infrastructure.entity.Agendamento;
import com.org.agendamento.infrastructure.entity.StatusAtendimento;

public record AgendamentoResponse(
        Long id,
        ClienteResponse cliente,
        ServicoResponse servico,
        BarbeiroResponse barbeiro,
        java.time.LocalDateTime horario,
        StatusAtendimento status
) {
    public static AgendamentoResponse from(Agendamento agendamento) {
        return new AgendamentoResponse(
                agendamento.getId(),
                ClienteResponse.from(agendamento.getCliente()),
                ServicoResponse.from(agendamento.getServico()),
                BarbeiroResponse.from(agendamento.getBarbeiro()),
                agendamento.getHorario(),
                agendamento.getStatus()
        );
    }
}
