package com.org.agendamento.infrastructure.repository;

import com.org.agendamento.infrastructure.entity.Agendamento;
import com.org.agendamento.infrastructure.entity.StatusAtendimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;


public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Override
    @EntityGraph(attributePaths = {"cliente", "servico", "barbeiro"})
    Page<Agendamento> findAll(Pageable pageable);

    @Query("""
            select count(a) > 0
            from Agendamento a
            where a.barbeiro.id = :barbeiroId
              and a.status = :status
              and (:idIgnorado is null or a.id <> :idIgnorado)
            """)
    boolean existsAtendimentoEmAberto(
            @Param("barbeiroId") Long barbeiroId,
            @Param("status") StatusAtendimento status,
            @Param("idIgnorado") Long idIgnorado
    );

    @EntityGraph(attributePaths = {"cliente", "servico", "barbeiro"})
    List<Agendamento> findByHorarioBetween(LocalDateTime dataHoraInicial, LocalDateTime dataHoraFinal);

    @EntityGraph(attributePaths = {"cliente", "servico", "barbeiro"})
    List<Agendamento> findByStatus(StatusAtendimento status);

    @EntityGraph(attributePaths = {"cliente", "servico", "barbeiro"})
    List<Agendamento> findByBarbeiroId(Long barbeiroId);

    @EntityGraph(attributePaths = {"cliente", "servico", "barbeiro"})
    List<Agendamento> findByClienteIdOrderByHorarioAsc(Long clienteId);

    @Query("""
            select count(a) > 0
            from Agendamento a
            where a.barbeiro.id = :barbeiroId
              and a.horario = :horario
              and a.status <> com.org.agendamento.infrastructure.entity.StatusAtendimento.CANCELADO
              and (:idIgnorado is null or a.id <> :idIgnorado)
            """)
    boolean existsHorarioOcupado(
            @Param("barbeiroId") Long barbeiroId,
            @Param("horario") LocalDateTime horario,
            @Param("idIgnorado") Long idIgnorado
    );


}
