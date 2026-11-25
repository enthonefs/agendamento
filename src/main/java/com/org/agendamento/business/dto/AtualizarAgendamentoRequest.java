package com.org.agendamento.business.dto;

import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record AtualizarAgendamentoRequest(
        Long servicoId,
        Long barbeiroId,
        @Future LocalDateTime horario
) {
}
