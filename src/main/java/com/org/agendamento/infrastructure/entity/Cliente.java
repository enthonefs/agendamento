package com.org.agendamento.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @Size(max = 100)
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;
    @NotBlank
    @Pattern(regexp = "\\d{10,11}")
    @Column(name = "telefone", length = 11, unique = true, nullable = false)
    private String telefone;


}
