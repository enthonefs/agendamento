package com.org.agendamento.infrastructure.repository;

import com.org.agendamento.infrastructure.entity.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {
    List<Barbeiro> findByAtivoTrueOrderByNomeAsc();
}
