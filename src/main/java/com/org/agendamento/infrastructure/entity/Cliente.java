package com.org.agendamento.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", length = 100)
    private String nome;
    @Column(name = "telefone", length = 11, unique = true)
    private String telefone;


}
