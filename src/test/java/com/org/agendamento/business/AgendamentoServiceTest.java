package com.org.agendamento.business;

import com.org.agendamento.business.dto.AgendamentoRequest;
import com.org.agendamento.business.dto.AtualizarAgendamentoRequest;
import com.org.agendamento.infrastructure.entity.Agendamento;
import com.org.agendamento.infrastructure.entity.Barbeiro;
import com.org.agendamento.infrastructure.entity.Cliente;
import com.org.agendamento.infrastructure.entity.Servico;
import com.org.agendamento.infrastructure.entity.StatusAtendimento;
import com.org.agendamento.infrastructure.exceptions.ConflictException;
import com.org.agendamento.infrastructure.repository.AgendamentoRepository;
import com.org.agendamento.infrastructure.repository.BarbeiroRepository;
import com.org.agendamento.infrastructure.repository.ClienteRepository;
import com.org.agendamento.infrastructure.repository.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class AgendamentoServiceTest {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    private Cliente cliente;
    private Servico corte;
    private Servico barba;
    private Barbeiro barbeiroCarlos;
    private Barbeiro barbeiroMarcos;

    @BeforeEach
    void setUp() {
        agendamentoRepository.deleteAll();
        clienteRepository.deleteAll();
        servicoRepository.deleteAll();
        barbeiroRepository.deleteAll();

        cliente = clienteRepository.save(Cliente.builder()
                .nome("Joao Silva")
                .telefone("85999999999")
                .senha("$2a$10$7EqJtq98hPqEX7fNZaFWoO2f8zM9O2Yq6w6KqX5u6w7V8x9y0z1a2")
                .build());

        corte = servicoRepository.save(Servico.builder()
                .servico("Corte")
                .valor(new BigDecimal("40.00"))
                .build());

        barba = servicoRepository.save(Servico.builder()
                .servico("Barba")
                .valor(new BigDecimal("25.00"))
                .build());

        barbeiroCarlos = barbeiroRepository.save(Barbeiro.builder()
                .nome("Carlos")
                .ativo(true)
                .build());

        barbeiroMarcos = barbeiroRepository.save(Barbeiro.builder()
                .nome("Marcos")
                .ativo(true)
                .build());
    }

    @Test
    void deveCriarAgendamentoComStatusAguardando() {
        LocalDateTime horario = LocalDateTime.now().plusDays(1).withNano(0);

        Agendamento agendamento = agendamentoService.agendar(new AgendamentoRequest(
                cliente.getId(),
                corte.getId(),
                barbeiroCarlos.getId(),
                horario
        ));

        assertThat(agendamento.getId()).isNotNull();
        assertThat(agendamento.getStatus()).isEqualTo(StatusAtendimento.AGUARDANDO);
        assertThat(agendamento.getCliente().getId()).isEqualTo(cliente.getId());
        assertThat(agendamento.getServico().getId()).isEqualTo(corte.getId());
        assertThat(agendamento.getBarbeiro().getId()).isEqualTo(barbeiroCarlos.getId());
    }

    @Test
    void deveBloquearNovoAtendimentoQuandoBarbeiroJaEstaEmAtendimento() {
        LocalDateTime horario = LocalDateTime.now().plusDays(1).withNano(0);

        Agendamento agendamento = agendamentoService.agendar(new AgendamentoRequest(
                cliente.getId(),
                corte.getId(),
                barbeiroCarlos.getId(),
                horario
        ));
        agendamentoService.atualizarStatus(agendamento.getId(), StatusAtendimento.EM_ATENDIMENTO);

        assertThatThrownBy(() -> agendamentoService.agendar(new AgendamentoRequest(
                cliente.getId(),
                barba.getId(),
                barbeiroCarlos.getId(),
                horario.plusMinutes(10)
        ))).isInstanceOf(ConflictException.class);
    }

    @Test
    void devePermitirMesmoHorarioParaBarbeirosDiferentes() {
        LocalDateTime horario = LocalDateTime.now().plusDays(1).withNano(0);

        agendamentoService.agendar(new AgendamentoRequest(
                cliente.getId(),
                corte.getId(),
                barbeiroCarlos.getId(),
                horario
        ));

        Agendamento agendamento = agendamentoService.agendar(new AgendamentoRequest(
                cliente.getId(),
                barba.getId(),
                barbeiroMarcos.getId(),
                horario.plusMinutes(10)
        ));

        assertThat(agendamento.getId()).isNotNull();
    }

    @Test
    void deveAtualizarAgendamentoPreservandoOMesmoRegistro() {
        LocalDateTime horario = LocalDateTime.now().plusDays(1).withNano(0);
        Agendamento agendamento = agendamentoService.agendar(
                new AgendamentoRequest(cliente.getId(), corte.getId(), barbeiroCarlos.getId(), horario)
        );

        LocalDateTime novoHorario = horario.plusHours(2);
        Agendamento atualizado = agendamentoService.atualizarAgendamento(
                agendamento.getId(),
                new AtualizarAgendamentoRequest(barba.getId(), barbeiroMarcos.getId(), novoHorario)
        );

        assertThat(atualizado.getId()).isEqualTo(agendamento.getId());
        assertThat(agendamentoRepository.count()).isEqualTo(1);
        assertThat(atualizado.getServico().getId()).isEqualTo(barba.getId());
        assertThat(atualizado.getBarbeiro().getId()).isEqualTo(barbeiroMarcos.getId());
        assertThat(atualizado.getHorario()).isEqualTo(novoHorario);
    }

    @Test
    void devePermitirAtualizarSemAlterarOMesmoHorario() {
        LocalDateTime horario = LocalDateTime.now().plusDays(1).withNano(0);
        Agendamento agendamento = agendamentoService.agendar(
                new AgendamentoRequest(cliente.getId(), corte.getId(), barbeiroCarlos.getId(), horario)
        );

        Agendamento atualizado = agendamentoService.atualizarAgendamento(
                agendamento.getId(),
                new AtualizarAgendamentoRequest(null, null, horario)
        );

        assertThat(atualizado.getId()).isEqualTo(agendamento.getId());
        assertThat(atualizado.getHorario()).isEqualTo(horario);
    }

    @Test
    void devePermitirReutilizarHorarioDeAgendamentoCancelado() {
        LocalDateTime horario = LocalDateTime.now().plusDays(1).withNano(0);

        Agendamento agendamentoCancelado = agendamentoService.agendar(
                new AgendamentoRequest(cliente.getId(), corte.getId(), barbeiroCarlos.getId(), horario)
        );
        agendamentoService.deletarAgendamento(agendamentoCancelado.getId());

        Agendamento novoAgendamento = agendamentoService.agendar(
                new AgendamentoRequest(cliente.getId(), barba.getId(), barbeiroMarcos.getId(), horario)
        );

        assertThat(novoAgendamento.getId()).isNotNull();
        assertThat(novoAgendamento.getHorario()).isEqualTo(horario);
    }

    @Test
    void deveBloquearAtualizacaoQuandoNovoHorarioJaEstiverOcupado() {
        LocalDateTime horarioOriginal = LocalDateTime.now().plusDays(1).withNano(0);
        Agendamento primeiro = agendamentoService.agendar(
                new AgendamentoRequest(cliente.getId(), corte.getId(), barbeiroCarlos.getId(), horarioOriginal)
        );

        LocalDateTime novoHorarioOcupado = horarioOriginal.plusHours(1);
        agendamentoService.agendar(
                new AgendamentoRequest(cliente.getId(), barba.getId(), barbeiroCarlos.getId(), novoHorarioOcupado)
        );

        assertThatThrownBy(() -> agendamentoService.atualizarAgendamento(
                primeiro.getId(),
                new AtualizarAgendamentoRequest(null, null, novoHorarioOcupado)
        )).isInstanceOf(ConflictException.class);
    }

    @Test
    void deveCancelarAgendamentoEmVezDeApagar() {
        Agendamento agendamento = agendamentoService.agendar(new AgendamentoRequest(
                cliente.getId(),
                corte.getId(),
                barbeiroCarlos.getId(),
                LocalDateTime.now().plusDays(1).withNano(0)
        ));

        agendamentoService.deletarAgendamento(agendamento.getId());

        Agendamento cancelado = agendamentoRepository.findById(agendamento.getId()).orElseThrow();
        assertThat(cancelado.getStatus()).isEqualTo(StatusAtendimento.CANCELADO);
    }

    @Test
    void deveRecusarAgendamentoComBarbeiroInativo() {
        barbeiroCarlos.setAtivo(false);
        barbeiroRepository.saveAndFlush(barbeiroCarlos);

        assertThatThrownBy(() -> agendamentoService.agendar(new AgendamentoRequest(
                cliente.getId(), corte.getId(), barbeiroCarlos.getId(), LocalDateTime.now().plusDays(1)
        ))).isInstanceOf(ConflictException.class);
    }

    @Test
    void devePermitirReagendamentoSomenteEnquantoAguardando() {
        Agendamento agendamento = agendamentoService.agendar(new AgendamentoRequest(
                cliente.getId(), corte.getId(), barbeiroCarlos.getId(), LocalDateTime.now().plusDays(1)
        ));
        agendamentoService.atualizarStatus(agendamento.getId(), StatusAtendimento.EM_ATENDIMENTO);

        assertThatThrownBy(() -> agendamentoService.atualizarAgendamento(
                agendamento.getId(), new AtualizarAgendamentoRequest(null, null,
                        LocalDateTime.now().plusDays(2))
        )).isInstanceOf(ConflictException.class);
    }
}
