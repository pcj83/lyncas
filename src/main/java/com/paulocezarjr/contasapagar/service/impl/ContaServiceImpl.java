package com.paulocezarjr.contasapagar.service.impl;

import com.paulocezarjr.contasapagar.domain.Conta;
import com.paulocezarjr.contasapagar.dto.ContaCreateDTO;
import com.paulocezarjr.contasapagar.dto.ContaResponseDTO;
import com.paulocezarjr.contasapagar.dto.ContaUpdateDTO;
import com.paulocezarjr.contasapagar.mapper.ContaMapper;
import com.paulocezarjr.contasapagar.observer.ContaObserver;
import com.paulocezarjr.contasapagar.observer.ContaSubject;
import com.paulocezarjr.contasapagar.repository.ContaRepository;
import com.paulocezarjr.contasapagar.service.ContaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ContaServiceImpl implements ContaService {

    private final ContaRepository contaRepository;
    private final ContaMapper contaMapper;
    private final ContaSubject contaSubject;


    @Override
    public ContaResponseDTO criarConta(ContaCreateDTO dto) {
        Conta conta = contaMapper.toEntity(dto);
        Conta contaSalva = contaRepository.save(conta);
        contaSubject.notifyContaCreated(contaSalva.getId());
        return contaMapper.toResponseDTO(contaSalva);
    }

    @Override
    public ContaResponseDTO atualizarConta(Long id, ContaUpdateDTO dto) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada para o ID: " + id));
        contaMapper.updateEntityFromDTO(dto, conta);
        Conta contaAtualizada = contaRepository.save(conta);
        contaSubject.notifyContaUpdated(contaAtualizada.getId());
        return contaMapper.toResponseDTO(contaAtualizada);
    }

    @Override
    public void excluirConta(Long id) {
        if (!contaRepository.existsById(id)) {
            throw new EntityNotFoundException("Conta não encontrada para o ID: " + id);
        }
        contaRepository.deleteById(id);
        contaSubject.notifyContaDeleted(id);
    }

    @Override
    public Page<ContaResponseDTO> listarContas(String descricao, LocalDate dataVencimento, Pageable pageable) {
        return contaRepository.findByDescricaoContainingAndDataVencimento(descricao, dataVencimento, pageable)
                .map(contaMapper::toResponseDTO);
    }

    @Override
    public ContaResponseDTO obterContaPorId(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada para o ID: " + id));
        return contaMapper.toResponseDTO(conta);
    }

    @Override
    public BigDecimal obterValorTotalPagoPorPeriodo(LocalDate inicio, LocalDate fim) {
        return contaRepository.obterValorTotalPagoPorPeriodo(inicio, fim)
                .orElse(BigDecimal.ZERO);
    }
    @Override
    public void addObserver(ContaObserver observer) {
        contaSubject.addObserver(observer);
    }

    @Override
    public void removeObserver(ContaObserver observer) {
        contaSubject.removeObserver(observer);
    }
}
