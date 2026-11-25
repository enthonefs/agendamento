package com.org.agendamento.business.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ServicoRequest(
        @NotBlank @Size(max = 100) String servico,
        @NotNull @DecimalMin("0.00") BigDecimal valor
) {
}
