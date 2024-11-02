package com.amorim.cooperativism.manager.service.impl;

import com.amorim.cooperativism.manager.feign.CpfValidatorFeignService;
import com.amorim.cooperativism.manager.feign.respose.FeignResponseEnum;
import com.amorim.cooperativism.manager.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final CpfValidatorFeignService cpfValidator;

    public MemberServiceImpl(CpfValidatorFeignService cpfValidator) {
        this.cpfValidator = cpfValidator;
    }

    @Override
    public Boolean canVote(String nationalId) {
        return FeignResponseEnum.ABLE_TO_VOTE.name().equals(this.cpfValidator.getValidity(nationalId).status());
    }
}
