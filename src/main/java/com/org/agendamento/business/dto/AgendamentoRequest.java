package com.org.agendamento.business.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoRequest(
        @NotNull Long clienteId,
        @NotNull Long servicoId,
        @NotNull Long barbeiroId,
        @NotNull @Future LocalDateTime horario
) {
}
