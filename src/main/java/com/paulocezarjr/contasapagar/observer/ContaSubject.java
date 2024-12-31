package com.paulocezarjr.contasapagar.observer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContaSubject {

    private final List<ContaObserver> observers = new ArrayList<>();

    public void addObserver(ContaObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ContaObserver observer) {
        observers.remove(observer);
    }

    public void notifyContaCreated(Long contaId) {
        for (ContaObserver observer : observers) {
            observer.onContaCreated(contaId);
        }
    }

    public void notifyContaUpdated(Long contaId) {
        for (ContaObserver observer : observers) {
            observer.onContaUpdated(contaId);
        }
    }

    public void notifyContaDeleted(Long contaId) {
        for (ContaObserver observer : observers) {
            observer.onContaDeleted(contaId);
        }
    }
}
