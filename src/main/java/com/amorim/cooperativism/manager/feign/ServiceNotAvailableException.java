package com.amorim.cooperativism.manager.feign;

public class ServiceNotAvailableException extends RuntimeException{

    public ServiceNotAvailableException() {
        super("Serviço não está disponivel");
    }
}
