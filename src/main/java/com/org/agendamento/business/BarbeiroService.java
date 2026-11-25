package com.org.agendamento.business;

import com.org.agendamento.business.dto.BarbeiroRequest;
import com.org.agendamento.infrastructure.entity.Barbeiro;
import com.org.agendamento.infrastructure.exceptions.ResourceNotFoundException;
import com.org.agendamento.infrastructure.repository.BarbeiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;

    public Page<Barbeiro> buscarTodos(Pageable pageable) {
        return barbeiroRepository.findAll(pageable);
    }

    public List<Barbeiro> buscarAtivos() {
        return barbeiroRepository.findByAtivoTrueOrderByNomeAsc();
    }

    public Barbeiro criar(BarbeiroRequest request) {
        Barbeiro barbeiro = Barbeiro.builder()
                .nome(request.nome())
                .ativo(request.ativo() == null || request.ativo())
                .build();
        return barbeiroRepository.save(barbeiro);
    }

    public Barbeiro atualizar(Long id, BarbeiroRequest request) {
        Barbeiro barbeiro = barbeiroRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Barbeiro não encontrado.")
        );

        barbeiro.setNome(request.nome());
        if (request.ativo() != null) {
            barbeiro.setAtivo(request.ativo());
        }
        return barbeiroRepository.saveAndFlush(barbeiro);
    }

    public void deletar(Long id) {
        if (!barbeiroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Barbeiro não encontrado.");
        }
        barbeiroRepository.deleteById(id);
    }
}
