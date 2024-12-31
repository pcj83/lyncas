package com.paulocezarjr.contasapagar.observer;

public interface ContaObserver {
    void onContaCreated(Long contaId);
    void onContaUpdated(Long contaId);
    void onContaDeleted(Long contaId);
}
