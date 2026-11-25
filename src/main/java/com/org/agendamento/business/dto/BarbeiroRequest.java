package com.org.agendamento.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BarbeiroRequest(
        @NotBlank @Size(max = 100) String nome,
        Boolean ativo
) {
}
