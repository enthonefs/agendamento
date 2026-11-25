package com.org.agendamento.controller;

import com.org.agendamento.business.ClienteService;
import com.org.agendamento.business.dto.ClienteRequest;
import com.org.agendamento.business.dto.ClienteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;


    @GetMapping
    public ResponseEntity<Page<ClienteResponse>> buscarTodos(Pageable pageable){
        return ResponseEntity.ok().body(service.buscarTodos(pageable).map(ClienteResponse::from));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criarCliente(@Valid @RequestBody ClienteRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponse.from(service.criarCliente(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request
    ){
        return ResponseEntity.ok(ClienteResponse.from(service.atualizarCliente(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id){
        service.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }

}
