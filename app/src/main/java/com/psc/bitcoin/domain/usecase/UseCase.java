package com.psc.bitcoin.domain.usecase;

public interface UseCase<P, T> {

    T execute(P parameter);
}
