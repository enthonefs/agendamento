package com.org.agendamento.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Pattern(regexp = "\\d{10,11}") String telefone
) {
}
