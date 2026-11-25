package com.org.agendamento.infrastructure.repository;

import com.org.agendamento.infrastructure.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
}
