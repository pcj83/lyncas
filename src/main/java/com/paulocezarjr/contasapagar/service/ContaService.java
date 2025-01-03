package com.paulocezarjr.contasapagar.service;

import com.paulocezarjr.contasapagar.dto.ContaCreateDTO;
import com.paulocezarjr.contasapagar.dto.ContaResponseDTO;
import com.paulocezarjr.contasapagar.dto.ContaUpdateDTO;
import com.paulocezarjr.contasapagar.observer.ContaObserver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ContaService {

    ContaResponseDTO criarConta(ContaCreateDTO dto);

    ContaResponseDTO atualizarConta(Long id, ContaUpdateDTO dto);

    void excluirConta(Long id);

    Page<ContaResponseDTO> listarContas(String descricao, LocalDate dataVencimento, Pageable pageable);

    ContaResponseDTO obterContaPorId(Long id);

    BigDecimal obterValorTotalPagoPorPeriodo(LocalDate inicio, LocalDate fim);

    void addObserver(ContaObserver observer);
    void removeObserver(ContaObserver observer);
}
