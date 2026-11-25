package com.org.agendamento.controller;

import com.org.agendamento.business.BarbeiroService;
import com.org.agendamento.business.dto.BarbeiroRequest;
import com.org.agendamento.business.dto.BarbeiroResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbeiro")
@RequiredArgsConstructor
public class BarbeiroController {

    private final BarbeiroService service;

    @GetMapping
    public ResponseEntity<Page<BarbeiroResponse>> buscarTodos(Pageable pageable) {
        return ResponseEntity.ok(service.buscarTodos(pageable).map(BarbeiroResponse::from));
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<BarbeiroResponse>> buscarAtivos() {
        return ResponseEntity.ok(service.buscarAtivos().stream().map(BarbeiroResponse::from).toList());
    }

    @PostMapping
    public ResponseEntity<BarbeiroResponse> criar(@Valid @RequestBody BarbeiroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(BarbeiroResponse.from(service.criar(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarbeiroResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody BarbeiroRequest request
    ) {
        return ResponseEntity.ok(BarbeiroResponse.from(service.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
