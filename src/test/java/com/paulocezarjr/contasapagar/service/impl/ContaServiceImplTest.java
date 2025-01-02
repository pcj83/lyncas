package com.paulocezarjr.contasapagar.service.impl;

import com.paulocezarjr.contasapagar.domain.Conta;
import com.paulocezarjr.contasapagar.dto.ContaCreateDTO;
import com.paulocezarjr.contasapagar.dto.ContaResponseDTO;
import com.paulocezarjr.contasapagar.dto.ContaUpdateDTO;
import com.paulocezarjr.contasapagar.mapper.ContaMapper;
import com.paulocezarjr.contasapagar.observer.ContaSubject;
import com.paulocezarjr.contasapagar.repository.ContaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ContaServiceImplTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ContaMapper contaMapper;

    @Mock
    private ContaSubject contaSubject;

    @InjectMocks
    private ContaServiceImpl contaService;

    private Conta conta;
    private ContaResponseDTO contaResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        conta = new Conta();
        conta.setId(1L);
        conta.setDescricao("Teste Conta");
        conta.setValor(BigDecimal.valueOf(1000));
        conta.setDataVencimento(LocalDate.now());
        conta.setDataPagamento(LocalDate.now());

        contaResponseDTO = new ContaResponseDTO();
        contaResponseDTO.setId(1L);
        contaResponseDTO.setDescricao("Teste Conta");
        contaResponseDTO.setValor(BigDecimal.valueOf(1000));
        contaResponseDTO.setDataVencimento(LocalDate.now());
        contaResponseDTO.setDataPagamento(LocalDate.now());
    }

    @Test
    void criarConta_DeveSalvarENotificar() {
        ContaCreateDTO dto = new ContaCreateDTO();
        when(contaMapper.toEntity(any(ContaCreateDTO.class))).thenReturn(conta);
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);
        when(contaMapper.toResponseDTO(any(Conta.class))).thenReturn(contaResponseDTO);

        ContaResponseDTO result = contaService.criarConta(dto);

        assertEquals(contaResponseDTO, result);
        verify(contaRepository, times(1)).save(conta);
        verify(contaSubject, times(1)).notifyContaCreated(conta.getId());
    }

    @Test
    void atualizarConta_DeveAtualizarENotificar() {
        ContaUpdateDTO dto = new ContaUpdateDTO();
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);
        when(contaMapper.toResponseDTO(any(Conta.class))).thenReturn(contaResponseDTO);

        ContaResponseDTO result = contaService.atualizarConta(1L, dto);

        assertEquals(contaResponseDTO, result);
        verify(contaRepository, times(1)).save(conta);
        verify(contaSubject, times(1)).notifyContaUpdated(conta.getId());
    }

    @Test
    void atualizarConta_DeveLancarExcecao_SeNaoEncontrada() {
        when(contaRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> contaService.atualizarConta(1L, new ContaUpdateDTO()));

        assertEquals("Conta não encontrada para o ID: 1", exception.getMessage());
    }

    @Test
    void excluirConta_DeveExcluirENotificar() {
        when(contaRepository.existsById(anyLong())).thenReturn(true);

        contaService.excluirConta(1L);

        verify(contaRepository, times(1)).deleteById(1L);
        verify(contaSubject, times(1)).notifyContaDeleted(1L);
    }

    @Test
    void excluirConta_DeveLancarExcecao_SeNaoEncontrada() {
        when(contaRepository.existsById(anyLong())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> contaService.excluirConta(1L));

        assertEquals("Conta não encontrada para o ID: 1", exception.getMessage());
    }

    @Test
    void listarContas_DeveRetornarPaginaDeContas() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Conta> page = new PageImpl<>(Collections.singletonList(conta));
        when(contaRepository.findByDescricaoContainingAndDataVencimento(anyString(), any(), eq(pageable)))
                .thenReturn(page);
        when(contaMapper.toResponseDTO(any(Conta.class))).thenReturn(contaResponseDTO);

        Page<ContaResponseDTO> result = contaService.listarContas("Teste", LocalDate.now(), pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(contaResponseDTO, result.getContent().get(0));
    }

    @Test
    void obterContaPorId_DeveRetornarConta() {
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(conta));
        when(contaMapper.toResponseDTO(any(Conta.class))).thenReturn(contaResponseDTO);

        ContaResponseDTO result = contaService.obterContaPorId(1L);

        assertEquals(contaResponseDTO, result);
    }

    @Test
    void obterContaPorId_DeveLancarExcecao_SeNaoEncontrada() {
        when(contaRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> contaService.obterContaPorId(1L));

        assertEquals("Conta não encontrada para o ID: 1", exception.getMessage());
    }

    @Test
    void obterValorTotalPagoPorPeriodo_DeveRetornarValor() {
        when(contaRepository.obterValorTotalPagoPorPeriodo(any(), any())).thenReturn(Optional.of(BigDecimal.valueOf(5000)));

        BigDecimal result = contaService.obterValorTotalPagoPorPeriodo(LocalDate.now(), LocalDate.now());

        assertEquals(BigDecimal.valueOf(5000), result);
    }

    @Test
    void obterValorTotalPagoPorPeriodo_DeveRetornarZero_SeNaoEncontrado() {
        when(contaRepository.obterValorTotalPagoPorPeriodo(any(), any())).thenReturn(Optional.empty());

        BigDecimal result = contaService.obterValorTotalPagoPorPeriodo(LocalDate.now(), LocalDate.now());

        assertEquals(BigDecimal.ZERO, result);
    }
}
