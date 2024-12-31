package com.paulocezarjr.contasapagar.repository;



import com.paulocezarjr.contasapagar.domain.Conta;
import com.paulocezarjr.contasapagar.observer.ContaSubject;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ContaRepositoryImpl {

    private final ContaRepository contaRepository;
    private final ContaSubject contaSubject;

    public ContaRepositoryImpl(ContaRepository contaRepository, ContaSubject contaSubject) {
        this.contaRepository = contaRepository;
        this.contaSubject = contaSubject;
    }

    public Conta saveAndNotify(Conta conta) {
        Conta saved = contaRepository.save(conta);
        contaSubject.notifyContaCreated(saved.getId());
        return saved;
    }

    public Optional<Conta> updateAndNotify(Long id, Conta conta) {
        Optional<Conta> updated = contaRepository.findById(id).map(existing -> {
            conta.setId(existing.getId());
            Conta saved = contaRepository.save(conta);
            contaSubject.notifyContaUpdated(saved.getId());
            return saved;
        });
        return updated;
    }

    public void deleteAndNotify(Long id) {
        contaRepository.deleteById(id);
        contaSubject.notifyContaDeleted(id);
    }
}
