package br.com.carrefour.lancamentoservice.service;

import br.com.carrefour.lancamentoservice.entity.Lancamento;
import br.com.carrefour.lancamentoservice.exception.LancamentoServiceCustomException;
import br.com.carrefour.lancamentoservice.model.LancamentoRequest;
import br.com.carrefour.lancamentoservice.model.LancamentoResponse;
import br.com.carrefour.lancamentoservice.repository.LancamentoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.BeanUtils.*;

@Service
@Log4j2
public class LancamentoServiceImpl implements LancamentoService {
    @Autowired
    private LancamentoRepository lancamentoRepository;
    @Override
    public long adicionarLancamento(LancamentoRequest lancamentoRequest) {
        log.info("Adicionando Lançamento");

        Lancamento lancamento =
                Lancamento.builder()
                        .dataLancamento(lancamentoRequest.getDataLancamento())
                        .descricao(lancamentoRequest.getDescricao())
                        .valor(lancamentoRequest.getValor())
                        .build();
        lancamentoRepository.save(lancamento);

        log.info("Lançamento adicionado");

        return lancamento.getLancamentoId();
    }

    @Override
    public LancamentoResponse lerLancamentoPorId(long lancamentoId) {
        log.info("Lendo lançamento para o id {}", lancamentoId);

        Lancamento lancamento = lancamentoRepository.findById(lancamentoId)
                .orElseThrow(() -> new LancamentoServiceCustomException("Lançamento com id " + lancamentoId + " não encontrado", "LANÇAMENTO_NÃO_ENCONTRADO"));

        LancamentoResponse lancamentoResponse = new LancamentoResponse();

        copyProperties(lancamento, lancamentoResponse);

        return lancamentoResponse;
    }

    @Override
    public void alterarLancamentoPorId(long lancamentoId, LancamentoRequest lancamentoRequest) {
        log.info("Lendo lançamento para o id {}", lancamentoId);

        Lancamento lancamento = lancamentoRepository.findById(lancamentoId)
                .orElseThrow(() -> new LancamentoServiceCustomException("Lançamento com id " + lancamentoId + " não encontrado", "LANÇAMENTO_NÃO_ENCONTRADO"));

        copyProperties(lancamentoRequest, lancamento);

        lancamentoRepository.save(lancamento);

        log.info("Lançamento com id {} alterado", lancamentoId);
    }

    @Override
    public void deletarLancamentoPorId(long lancamentoId) {
        log.info("Lendo lançamento para o id {}", lancamentoId);

        Lancamento lancamento = lancamentoRepository.findById(lancamentoId)
                .orElseThrow(() -> new LancamentoServiceCustomException("Lançamento com id " + lancamentoId + " não encontrado", "LANÇAMENTO_NÃO_ENCONTRADO"));

        lancamentoRepository.delete(lancamento);

        log.info("Lançamento com id {} removido", lancamentoId);
    }

    @Override
    public List<LancamentoResponse> lerTodosLancamentos() {
        log.info("Lendo todos os lançamentos");
        List<Lancamento> lancamentos = lancamentoRepository.findAll();
        List<LancamentoResponse> lancamentoResponses = new ArrayList<>();

        for (Lancamento l : lancamentos) {
            LancamentoResponse lancamentoResponse = new LancamentoResponse();
            lancamentoResponse.setDataLancamento(l.getDataLancamento());
            lancamentoResponse.setDescricao(l.getDescricao());
            lancamentoResponse.setLancamentoId(l.getLancamentoId());
            lancamentoResponse.setValor(l.getValor());
            lancamentoResponses.add(lancamentoResponse);
        }
        return lancamentoResponses;
    }
}
