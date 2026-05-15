package com.org.agendamento.controller;

import com.org.agendamento.infrastructure.entity.Agendamento;
import com.org.agendamento.business.AgendamentoService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<Agendamento>> listarAgendamentos(){
        return ResponseEntity.ok(service.listarAgendamentos());
    }

    @GetMapping(params = "data")
    public ResponseEntity<List<Agendamento>> listarAgendamentosDoDia(@RequestParam LocalDate data){
        return ResponseEntity.ok(service.listarAgendamentosDoDia(data));
    }

    @PostMapping(value = "/{idCliente}", params = "idServico")
    public ResponseEntity<Agendamento> agendar(@PathVariable Long idCliente, @RequestParam Long idServico,
                                               @RequestBody Agendamento agendamento){
        return ResponseEntity.ok(service.agendar(idCliente, idServico, agendamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizarAgendamento(@PathVariable Long id, @RequestBody Agendamento agendamento){
        return ResponseEntity.ok(service.atualizarAgendamento(id, agendamento));
    }

    @DeleteMapping(params = "id")
    public ResponseEntity<Void> deletarAgendamento(@RequestParam Long id){
        service.deletarAgendamento(id);
        return ResponseEntity.ok().build();
    }
}
