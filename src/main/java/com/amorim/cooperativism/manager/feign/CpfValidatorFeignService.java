package com.amorim.cooperativism.manager.feign;

import com.amorim.cooperativism.manager.feign.respose.FeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient( name = "cpf-validator", url = "${cpf.validator.url:https://user-info.herokuapp.com/}")
public interface  CpfValidatorFeignService {

    @GetMapping("/users/{cpf}")
    FeignResponse getValidity(@PathVariable("cpf") String nationalId);
}
