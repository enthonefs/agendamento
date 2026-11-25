package com.org.agendamento.controller;

import com.org.agendamento.business.ServicoService;
import com.org.agendamento.business.dto.ServicoRequest;
import com.org.agendamento.business.dto.ServicoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/servico")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService service;

    @GetMapping
    public ResponseEntity<Page<ServicoResponse>> buscarTodos(Pageable pageable){
        return ResponseEntity.ok().body(service.buscarTodos(pageable).map(ServicoResponse::from));
    }

    @PostMapping
    public ResponseEntity<ServicoResponse> criarServico(@Valid @RequestBody ServicoRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ServicoResponse.from(service.criarServico(request)));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicoRequest request
    ){
        return ResponseEntity.ok(ServicoResponse.from(service.atualizarServico(id, request)));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id){
        service.apagarServico(id);
        return ResponseEntity.noContent().build();
    }
}
