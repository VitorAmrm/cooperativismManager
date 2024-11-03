package com.amorim.cooperativism.manager.filter;

import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.Semaphore;

@Component
public class RequestLimitFilter extends OncePerRequestFilter {


    private final Semaphore semaphore;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(RequestLimitFilter.class);

    public RequestLimitFilter(@Value("${request.limit.filter.value:150}") Integer qtt) {
        this.semaphore = new Semaphore(qtt);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean permit = false;

        try {
            permit = semaphore.tryAcquire();
            if (permit) {
                LOG.info("Semaforo foi adquirido");
                filterChain.doFilter(request, response);
            } else {
                LOG.info("Apliicação já chgou ao limite de {} ", semaphore.getQueueLength());
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                response.getWriter().write(mapper.writeValueAsString(new ApplicationResponse("Servidor Ocupado", HttpStatus.SERVICE_UNAVAILABLE.value())));
            }
        } finally {
            if (permit) {
                LOG.info("Semaforo liberou um lock");
                semaphore.release();
            }
        }
    }
}