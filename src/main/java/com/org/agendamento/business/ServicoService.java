package com.org.agendamento.business;

import com.org.agendamento.business.dto.ServicoRequest;
import com.org.agendamento.infrastructure.entity.Servico;
import com.org.agendamento.infrastructure.repository.ServicoRepository;
import com.org.agendamento.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public Page<Servico> buscarTodos(Pageable pageable){
        return servicoRepository.findAll(pageable);
    }

    public Servico criarServico(ServicoRequest request){
        Servico servico = Servico.builder()
                .servico(request.servico())
                .valor(request.valor())
                .build();
        return servicoRepository.save(servico);
    }


    public Servico atualizarServico(Long id, ServicoRequest request){
        Servico servicoAtual = servicoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Serviço não encontrado."));

        servicoAtual.setServico(request.servico());
        servicoAtual.setValor(request.valor());

        return servicoRepository.saveAndFlush(servicoAtual);

    }

    public void apagarServico(Long id){
        if (!servicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Serviço não encontrado.");
        }
        servicoRepository.deleteById(id);
    }
}
