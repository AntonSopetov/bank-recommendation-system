package ru.star.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.star.bank.dto.RecommendationResponseDto;
import ru.star.bank.entity.QueryConditionEntity;
import ru.star.bank.entity.RuleEntity;
import ru.star.bank.repository.*;
import ru.star.bank.rule.RecommendationRuleSet;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecommendationServiceTest {

    private RecommendationService recommendationService;
    private RuleRepository ruleRepository;
    private RecommendationRepository recommendationRepository;
    private List<RecommendationRuleSet> staticRules;

    @BeforeEach
    void setUp() {
        ruleRepository = mock(RuleRepository.class);
        recommendationRepository = mock(RecommendationRepository.class);
        staticRules = new ArrayList<>();
        recommendationService = new RecommendationService(staticRules, ruleRepository, recommendationRepository);
    }

    @Test
    void testDynamicRule_UserOf_Success() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        RuleEntity ruleEntity = new RuleEntity();
        ruleEntity.setProductId(productId);
        ruleEntity.setProductName("Test Product");
        ruleEntity.setProductText("Test Text");

        QueryConditionEntity condition = new QueryConditionEntity();
        condition.setQuery("USER_OF");
        condition.setArguments(List.of("CREDIT"));
        condition.setNegate(false);
        condition.setRuleEntity(ruleEntity);
        ruleEntity.setRule(List.of(condition));

        when(ruleRepository.findAll()).thenReturn(List.of(ruleEntity));
        when(recommendationRepository.hasProductType(userId, ProductType.CREDIT)).thenReturn(true);

        RecommendationResponseDto response = recommendationService.getRecommendations(userId);

        assertEquals(1, response.getRecommendations().size());
        assertEquals(productId, response.getRecommendations().get(0).getId());
    }

    @Test
    void testDynamicRule_TransactionSumCompare_WithNegate() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        RuleEntity ruleEntity = new RuleEntity();
        ruleEntity.setProductId(productId);
        ruleEntity.setProductName("Credit Product");

        QueryConditionEntity condition = new QueryConditionEntity();
        condition.setQuery("TRANSACTION_SUM_COMPARE");
        condition.setArguments(List.of("DEBIT", "DEPOSIT", ">", "100000"));
        condition.setNegate(true);
        condition.setRuleEntity(ruleEntity);
        ruleEntity.setRule(List.of(condition));

        when(ruleRepository.findAll()).thenReturn(List.of(ruleEntity));
        when(recommendationRepository.getTransactionSum(userId, ProductType.DEBIT, TransactionType.DEPOSIT)).thenReturn(50000.0);

        RecommendationResponseDto response = recommendationService.getRecommendations(userId);

        assertEquals(1, response.getRecommendations().size());
    }
}