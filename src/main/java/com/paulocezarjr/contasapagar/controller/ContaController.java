package com.paulocezarjr.contasapagar.controller;

import com.paulocezarjr.contasapagar.dto.ContaCreateDTO;
import com.paulocezarjr.contasapagar.dto.ContaResponseDTO;
import com.paulocezarjr.contasapagar.dto.ContaUpdateDTO;
import com.paulocezarjr.contasapagar.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;

    @Operation(summary = "Criar uma nova conta", description = "Cria uma nova conta com base nos dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso", content = @Content(schema = @Schema(implementation = ContaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados fornecidos")
    })
    @PostMapping
    public ContaResponseDTO criarConta(@Valid @RequestBody ContaCreateDTO contaCreateDTO) {
        return contaService.criarConta(contaCreateDTO);
    }

    @Operation(summary = "Atualizar uma conta", description = "Atualiza os dados de uma conta existente pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso", content = @Content(schema = @Schema(implementation = ContaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados fornecidos")
    })
    @PutMapping("/{id}")
    public ContaResponseDTO atualizarConta(
            @Parameter(description = "ID da conta a ser atualizada", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ContaUpdateDTO contaUpdateDTO) {
        return contaService.atualizarConta(id, contaUpdateDTO);
    }

    @Operation(summary = "Excluir uma conta", description = "Exclui uma conta existente pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conta excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @DeleteMapping("/{id}")
    public void excluirConta(
            @Parameter(description = "ID da conta a ser excluída", example = "1")
            @PathVariable Long id) {
        contaService.excluirConta(id);
    }

    @Operation(summary = "Listar contas", description = "Lista contas com filtros opcionais e suporte a paginação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso")
    })
    @GetMapping
    public Page<ContaResponseDTO> listarContas(
            @Parameter(description = "Filtro parcial para a descrição da conta")
            @RequestParam(required = false) String descricao,
            @Parameter(description = "Filtro pela data de vencimento", example = "2024-01-01")
            @RequestParam(required = false) LocalDate dataVencimento,
            Pageable pageable) {
        return contaService.listarContas(descricao, dataVencimento, pageable);
    }

    @Operation(summary = "Obter detalhes de uma conta", description = "Busca os detalhes de uma conta pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta encontrada com sucesso", content = @Content(schema = @Schema(implementation = ContaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{id}")
    public ContaResponseDTO obterContaPorId(
            @Parameter(description = "ID da conta a ser buscada", example = "1")
            @PathVariable Long id) {
        return contaService.obterContaPorId(id);
    }

    @Operation(summary = "Obter valor total pago por período", description = "Calcula o valor total pago em um período específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valor total pago retornado com sucesso")
    })
    @GetMapping("/total-pago")
    public BigDecimal obterValorTotalPagoPorPeriodo(
            @Parameter(description = "Data de início do período", example = "2024-01-01")
            @RequestParam LocalDate inicio,
            @Parameter(description = "Data de término do período", example = "2024-12-31")
            @RequestParam LocalDate fim) {
        return contaService.obterValorTotalPagoPorPeriodo(inicio, fim);
    }
}
