package com.org.agendamento.business;

import com.org.agendamento.business.dto.AgendamentoRequest;
import com.org.agendamento.business.dto.AtualizarAgendamentoRequest;
import com.org.agendamento.infrastructure.entity.Barbeiro;
import com.org.agendamento.infrastructure.repository.AgendamentoRepository;
import com.org.agendamento.infrastructure.repository.BarbeiroRepository;
import com.org.agendamento.infrastructure.repository.ClienteRepository;
import com.org.agendamento.infrastructure.repository.ServicoRepository;
import com.org.agendamento.infrastructure.entity.Agendamento;
import com.org.agendamento.infrastructure.entity.Cliente;
import com.org.agendamento.infrastructure.entity.Servico;
import com.org.agendamento.infrastructure.entity.StatusAtendimento;
import com.org.agendamento.infrastructure.exceptions.ConflictException;
import com.org.agendamento.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;
    private final BarbeiroRepository barbeiroRepository;

    @Transactional
    public Agendamento agendar(AgendamentoRequest request){
        Cliente cliente = clienteRepository.findById(request.clienteId()).orElseThrow(
                () -> new ResourceNotFoundException("Cliente não encontrado."));

        Servico servico = servicoRepository.findById(request.servicoId()).orElseThrow(
                () -> new ResourceNotFoundException("Serviço indisponível."));

        Barbeiro barbeiro = barbeiroRepository.findById(request.barbeiroId()).orElseThrow(
                () -> new ResourceNotFoundException("Barbeiro não encontrado."));

        if (!barbeiro.isAtivo()) {
            throw new ConflictException("Barbeiro não está disponível para agendamentos.");
        }

        validarBarbeiroEmAtendimento(barbeiro.getId(), null);
        validarHorarioAgendamento(barbeiro.getId(), request.horario(), null);

        Agendamento agendamento = Agendamento.builder()
                .cliente(cliente)
                .servico(servico)
                .barbeiro(barbeiro)
                .horario(request.horario())
                .status(StatusAtendimento.AGUARDANDO)
                .build();
        return agendamentoRepository.save(agendamento);

    }

    public Page<Agendamento> listarAgendamentos(Pageable pageable){
        return agendamentoRepository.findAll(pageable);
    }

    public List<Agendamento> listarAgendamentosDoDia(LocalDate data){

        LocalDateTime primeiraHoraDia = data.atStartOfDay();
        LocalDateTime ultimaHoraDia = data.plusDays(1).atStartOfDay();

        return agendamentoRepository.findByHorarioBetween(primeiraHoraDia, ultimaHoraDia.minusNanos(1));
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarAgendamentosDoCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException("Cliente não encontrado.");
        }
        return agendamentoRepository.findByClienteIdOrderByHorarioAsc(clienteId);
    }

    @Transactional
    public Agendamento atualizarAgendamento(Long id, AtualizarAgendamentoRequest request){
        Agendamento agendamentoExistente = agendamentoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Agendamento não encontrado."));

        validarPodeAlterar(agendamentoExistente);

        Servico servico = agendamentoExistente.getServico();
        if (request.servicoId() != null) {
            servico = servicoRepository.findById(request.servicoId()).orElseThrow(
                () -> new ResourceNotFoundException("Serviço indisponível."));
        }

        Barbeiro barbeiro = agendamentoExistente.getBarbeiro();
        if (request.barbeiroId() != null) {
            barbeiro = barbeiroRepository.findById(request.barbeiroId()).orElseThrow(
                    () -> new ResourceNotFoundException("Barbeiro não encontrado."));
        }

        LocalDateTime horario = request.horario() != null ? request.horario() : agendamentoExistente.getHorario();
        if (!barbeiro.isAtivo()) {
            throw new ConflictException("Barbeiro não está disponível para agendamentos.");
        }
        validarBarbeiroEmAtendimento(barbeiro.getId(), agendamentoExistente.getId());
        validarHorarioAgendamento(barbeiro.getId(), horario, agendamentoExistente.getId());

        agendamentoExistente.setServico(servico);
        agendamentoExistente.setBarbeiro(barbeiro);
        agendamentoExistente.setHorario(horario);

        return agendamentoRepository.saveAndFlush(agendamentoExistente);
    }

    @Transactional
    public void deletarAgendamento(Long id){
        Agendamento agendamento = agendamentoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Agendamento não encontrado."));
        if (agendamento.getStatus() == StatusAtendimento.CANCELADO) {
            return;
        }
        if (agendamento.getStatus() == StatusAtendimento.FINALIZADO
                || agendamento.getStatus() == StatusAtendimento.EM_ATENDIMENTO) {
            throw new ConflictException("Agendamento não pode mais ser cancelado.");
        }
        agendamento.setStatus(StatusAtendimento.CANCELADO);
        agendamentoRepository.save(agendamento);
    }

    @Transactional
    public Agendamento atualizarStatus(Long id, StatusAtendimento status) {
        Agendamento agendamento = agendamentoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Agendamento não encontrado.")
        );
        validarTransicaoDeStatus(agendamento.getStatus(), status);
        agendamento.setStatus(status);
        return agendamentoRepository.saveAndFlush(agendamento);
    }

    public List<Barbeiro> listarBarbeirosDisponiveis() {
        return barbeiroRepository.findAll().stream()
                .filter(barbeiro -> barbeiro.isAtivo() && !agendamentoRepository.existsAtendimentoEmAberto(
                        barbeiro.getId(),
                        StatusAtendimento.EM_ATENDIMENTO,
                        null
                ))
                .toList();
    }

    private void validarHorarioAgendamento(Long barbeiroId, LocalDateTime horario, Long idIgnorado) {
        boolean horarioOcupado = agendamentoRepository.existsHorarioOcupado(barbeiroId, horario, idIgnorado);

        if (horarioOcupado) {
            throw new ConflictException("Horário não disponível");
        }

    }

    private void validarBarbeiroEmAtendimento(Long barbeiroId, Long idIgnorado) {
        boolean barbeiroOcupado = agendamentoRepository.existsAtendimentoEmAberto(
                barbeiroId,
                StatusAtendimento.EM_ATENDIMENTO,
                idIgnorado
        );

        if (barbeiroOcupado) {
            throw new ConflictException("Barbeiro já está em atendimento.");
        }
    }

    private void validarPodeAlterar(Agendamento agendamento) {
        if (agendamento.getStatus() != StatusAtendimento.AGUARDANDO) {
            throw new ConflictException("Somente agendamentos aguardando podem ser reagendados.");
        }
    }

    private void validarTransicaoDeStatus(StatusAtendimento atual, StatusAtendimento novo) {
        if (novo == null || atual == novo) {
            return;
        }
        boolean permitida = (atual == StatusAtendimento.AGUARDANDO
                && (novo == StatusAtendimento.EM_ATENDIMENTO || novo == StatusAtendimento.CANCELADO))
                || (atual == StatusAtendimento.EM_ATENDIMENTO
                && novo == StatusAtendimento.FINALIZADO);
        if (!permitida) {
            throw new ConflictException("Transição de status não permitida.");
        }
    }
}
