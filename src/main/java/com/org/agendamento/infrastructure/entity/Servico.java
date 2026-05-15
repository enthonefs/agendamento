package com.org.agendamento.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;


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
    @Column(name = "servico", length = 100)
    private String servico;
    @Column(name = "valor", length = 10)
    private double valor;


}
