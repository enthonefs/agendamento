package com.org.agendamento.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(
        name = "agendamento",
        indexes = {
                @Index(name = "idx_agendamento_horario", columnList = "horario"),
                @Index(name = "idx_agendamento_barbeiro_status", columnList = "barbeiro_id, status")
        }
)
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Barbeiro barbeiro;

    @NotNull
    @Column(name = "horario", nullable = false)
    private LocalDateTime horario;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false, columnDefinition = "varchar(30)")
    private StatusAtendimento status;

}
