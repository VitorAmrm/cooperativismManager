package com.amorim.cooperativism.manager.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomErrorDecoder implements ErrorDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(CustomErrorDecoder.class);
    private final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            LOG.warn("ERRO 404 {}: {}", methodKey, response.body());
            return new ServiceNotAvailableException();
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
