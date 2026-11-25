package com.org.agendamento.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "servico")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 100)
    @Column(name = "servico", length = 100, nullable = false)
    private String servico;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;
}
