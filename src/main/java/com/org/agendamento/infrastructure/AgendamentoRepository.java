package com.org.agendamento.infrastructure;

import com.org.agendamento.infrastructure.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;


public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Agendamento findByIdAndHorarioBetween(Long idServico, LocalDateTime dataHoraIncial,
                                                           LocalDateTime dataHoraFinal);

    List<Agendamento> findByHorarioBetween(LocalDateTime dataHoraInicial, LocalDateTime dataHoraFinal);


}
