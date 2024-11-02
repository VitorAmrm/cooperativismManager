package com.amorim.cooperativism.manager.service;

import com.amorim.cooperativism.manager.domain.Member;
import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    Boolean canVote(String nationalId);
}
