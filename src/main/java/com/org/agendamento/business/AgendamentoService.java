package com.org.agendamento.business;

import com.org.agendamento.infrastructure.exceptions.ConflictException;
import com.org.agendamento.infrastructure.exceptions.ResourceNotFoundException;
import com.org.agendamento.infrastructure.entity.Agendamento;
import com.org.agendamento.infrastructure.entity.Cliente;
import com.org.agendamento.infrastructure.entity.Servico;
import com.org.agendamento.infrastructure.AgendamentoRepository;
import com.org.agendamento.infrastructure.ClienteRepository;
import com.org.agendamento.infrastructure.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;

    public Agendamento agendar(Long idCliente, Long idServico, Agendamento agendamento){
        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(
                () -> new ResourceNotFoundException("Cliente não encontrado."));
        agendamento.setCliente(cliente);

        Servico servico = servicoRepository.findById(idServico).orElseThrow(
                () -> new ResourceNotFoundException("Serviço indisponível."));
        agendamento.setServico(servico);

        Long id = agendamento.getServico().getId();

        LocalDateTime horaAgendamento = agendamento.getHorario();
        LocalDateTime horaFinal = agendamento.getHorario().plusMinutes(1);

        Agendamento agendados = agendamentoRepository.findByIdAndHorarioBetween(id, horaAgendamento, horaFinal);

        if (Objects.nonNull(agendados)){
            throw new ConflictException("Horário já preenchido");
        }
        return agendamentoRepository.save(agendamento);

    }

    public List<Agendamento> listarAgendamentos(){
        return agendamentoRepository.findAll();
    }

    public List<Agendamento> listarAgendamentosDoDia(LocalDate data){

        LocalDateTime primeiraHoraDia = data.atStartOfDay();
        LocalDateTime ultimaHoraDia = data.atTime(23, 59,59);

        return agendamentoRepository.findByHorarioBetween(primeiraHoraDia, ultimaHoraDia);
    }

    public Agendamento atualizarAgendamento(Long id, Agendamento agendamento){
        Agendamento agendamentoExistente = agendamentoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Agendamento não encontrado."));

        Agendamento agendamentoAtualizado = Agendamento.builder()
                .cliente(agendamentoExistente.getCliente())
                .servico(agendamentoExistente.getServico())
                .horario(agendamento.getHorario() != null ? agendamento.getHorario() : agendamentoExistente.getHorario())
                .build();

        return agendamentoRepository.saveAndFlush(agendamentoAtualizado);
    }

    public void deletarAgendamento(Long id){
        agendamentoRepository.deleteById(id);
    }
}
