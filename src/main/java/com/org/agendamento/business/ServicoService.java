package com.org.agendamento.business;

import com.org.agendamento.infrastructure.entity.Servico;
import com.org.agendamento.infrastructure.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public List<Servico> buscarTodos(){
        return servicoRepository.findAll();
    }

    public Servico criarServico(Servico servico){
        return servicoRepository.save(servico);
    }


    public Servico atualizarAgendamento(Long id, Servico servico){
        Servico servicoAtual = servicoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Agendamento não encontrado."));

        Servico servicoAtualizado = Servico.builder()
                .servico(servico.getServico() != null ? servico.getServico() : servicoAtual.getServico())
                .valor(servico.getValor() != 0 ? servico.getValor() : servicoAtual.getValor())
                .id(servicoAtual.getId())
                .build();

        return servicoRepository.saveAndFlush(servicoAtualizado);

    }

    public void apagarServico(Long id){
        servicoRepository.deleteById(id);
    }
}
