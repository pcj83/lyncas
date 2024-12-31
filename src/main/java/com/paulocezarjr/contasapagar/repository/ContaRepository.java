package com.paulocezarjr.contasapagar.repository;

import com.paulocezarjr.contasapagar.domain.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    Page<Conta> findByDescricaoContainingAndDataVencimento(String descricao, LocalDate dataVencimento, Pageable pageable);

    @Query("SELECT SUM(c.valor) FROM Conta c WHERE c.dataPagamento BETWEEN :inicio AND :fim")
    Optional<BigDecimal> obterValorTotalPagoPorPeriodo(LocalDate inicio, LocalDate fim);
}
