package com.org.agendamento.controller;

import com.org.agendamento.business.AgendamentoService;
import com.org.agendamento.business.dto.AgendamentoRequest;
import com.org.agendamento.business.dto.AgendamentoResponse;
import com.org.agendamento.business.dto.AtualizarAgendamentoRequest;
import com.org.agendamento.infrastructure.entity.StatusAtendimento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agendamento")
public class AgendamentoController {

    private final AgendamentoService service;

    @GetMapping
    public ResponseEntity<Page<AgendamentoResponse>> listarAgendamentos(Pageable pageable){
        return ResponseEntity.ok(service.listarAgendamentos(pageable).map(AgendamentoResponse::from));
    }

    @GetMapping(params = "data")
    public ResponseEntity<List<AgendamentoResponse>> listarAgendamentosDoDia(@RequestParam LocalDate data){
        return ResponseEntity.ok(service.listarAgendamentosDoDia(data).stream()
                .map(AgendamentoResponse::from)
                .toList());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AgendamentoResponse>> listarAgendamentosDoCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.listarAgendamentosDoCliente(clienteId).stream()
                .map(AgendamentoResponse::from)
                .toList());
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponse> agendar(@Valid @RequestBody AgendamentoRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(AgendamentoResponse.from(service.agendar(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> atualizarAgendamento(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarAgendamentoRequest request
    ){
        return ResponseEntity.ok(AgendamentoResponse.from(service.atualizarAgendamento(id, request)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AgendamentoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestParam @NotNull StatusAtendimento status
    ) {
        return ResponseEntity.ok(AgendamentoResponse.from(service.atualizarStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgendamento(@PathVariable Long id){
        service.deletarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}
