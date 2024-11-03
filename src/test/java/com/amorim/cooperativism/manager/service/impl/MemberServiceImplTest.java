package com.amorim.cooperativism.manager.service.impl;

import com.amorim.cooperativism.manager.feign.CpfValidatorFeignService;
import com.amorim.cooperativism.manager.feign.respose.FeignResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class MemberServiceImplTest {

    @Mock
    private CpfValidatorFeignService cpfValidator;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCanVote_WhenAbleToVote() {
        String nationalId = "12345678900";
        FeignResponse response = new FeignResponse("ABLE_TO_VOTE");

        when(cpfValidator.getValidity(nationalId)).thenReturn(response);

        Boolean result = memberService.canVote(nationalId);

        assertTrue(result);
    }

    @Test
    void testCanVote_WhenNotAbleToVote() {
        String nationalId = "12345678900";
        FeignResponse response = new FeignResponse("UNABLE_TO_VOTE");

        when(cpfValidator.getValidity(nationalId)).thenReturn(response);

        Boolean result = memberService.canVote(nationalId);

        assertFalse(result);
    }
}
