package com.paulocezarjr.contasapagar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulocezarjr.contasapagar.dto.ContaCreateDTO;
import com.paulocezarjr.contasapagar.dto.ContaResponseDTO;
import com.paulocezarjr.contasapagar.dto.ContaUpdateDTO;
import com.paulocezarjr.contasapagar.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@AutoConfigureMockMvc
@SpringBootTest(properties = "spring.profiles.active=test")
class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ContaService contaService;

    @Autowired
    private ObjectMapper objectMapper;

    private ContaResponseDTO contaResponse;

    @BeforeEach
    void setUp() {
        contaResponse = new ContaResponseDTO();
        contaResponse.setId(1L);
        contaResponse.setDescricao("Teste Conta");
        contaResponse.setValor(BigDecimal.valueOf(1000));
        contaResponse.setDataVencimento(LocalDate.now());
        contaResponse.setDataPagamento(LocalDate.now());
    }

    @Test
    void criarConta_DeveRetornar201() throws Exception {
        ContaCreateDTO contaCreateDTO = new ContaCreateDTO();
        contaCreateDTO.setDescricao("Teste Conta");
        contaCreateDTO.setValor(BigDecimal.valueOf(1000));
        contaCreateDTO.setDataVencimento(LocalDate.now());

        Mockito.when(contaService.criarConta(any(ContaCreateDTO.class))).thenReturn(contaResponse);

        mockMvc.perform(post("/api/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(contaResponse.getId()))
                .andExpect(jsonPath("$.descricao").value(contaResponse.getDescricao()));
    }

    @Test
    void atualizarConta_DeveRetornar200() throws Exception {
        ContaUpdateDTO contaUpdateDTO = new ContaUpdateDTO();
        contaUpdateDTO.setDescricao("Conta Atualizada");

        Mockito.when(contaService.atualizarConta(eq(1L), any(ContaUpdateDTO.class))).thenReturn(contaResponse);

        mockMvc.perform(put("/api/contas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaResponse.getId()))
                .andExpect(jsonPath("$.descricao").value(contaResponse.getDescricao()));
    }

    @Test
    void excluirConta_DeveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/contas/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(contaService).excluirConta(1L);
    }

    @Test
    void listarContas_DeveRetornar200() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ContaResponseDTO> page = new PageImpl<>(Collections.singletonList(contaResponse));

        Mockito.when(contaService.listarContas(any(), any(), eq(pageable))).thenReturn(page);

        mockMvc.perform(get("/api/contas")
                        .param("descricao", "Teste")
                        .param("dataVencimento", LocalDate.now().toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(contaResponse.getId()))
                .andExpect(jsonPath("$.content[0].descricao").value(contaResponse.getDescricao()));
    }

    @Test
    void obterContaPorId_DeveRetornar200() throws Exception {
        Mockito.when(contaService.obterContaPorId(1L)).thenReturn(contaResponse);

        mockMvc.perform(get("/api/contas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contaResponse.getId()))
                .andExpect(jsonPath("$.descricao").value(contaResponse.getDescricao()));
    }

    @Test
    void obterValorTotalPagoPorPeriodo_DeveRetornar200() throws Exception {
        Mockito.when(contaService.obterValorTotalPagoPorPeriodo(any(), any()))
                .thenReturn(BigDecimal.valueOf(5000));

        mockMvc.perform(get("/api/contas/total-pago")
                        .param("inicio", "2024-01-01")
                        .param("fim", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("5000"));
    }
}
