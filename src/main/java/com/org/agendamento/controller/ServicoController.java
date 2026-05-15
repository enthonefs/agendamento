package com.org.agendamento.controller;

import com.org.agendamento.infrastructure.entity.Servico;
import com.org.agendamento.business.ServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servico")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService service;

    @GetMapping
    public ResponseEntity<List<Servico>> buscarTodos(){
        return ResponseEntity.ok().body(service.buscarTodos());
    }

    @PostMapping
    public ResponseEntity<Servico> criarServico(@RequestBody Servico servico){
        return ResponseEntity.ok(service.criarServico(servico));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable Long id, @RequestBody Servico servico){
        return ResponseEntity.ok(service.atualizarAgendamento(id, servico));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id){
        service.apagarServico(id);
        return ResponseEntity.ok().build();
    }
}
